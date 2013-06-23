package fr.duchesses.moov.apis;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Station;
import fr.duchesses.moov.models.StationType;
import fr.duchesses.moov.models.sncf.SncfLineModel;
import fr.duchesses.moov.models.sncf.SncfStopModel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static fr.duchesses.moov.apis.DistanceHelper.distance;

@Component
public class SncfApiService implements ApiService {

    private static final Logger logger = Logger.getLogger(SncfApiService.class);

    private List<SncfStopModel> allStops = Lists.newArrayList();
    private HashMultimap<Integer, SncfLineModel> allStopLines = HashMultimap.create();

    private Map<String, Station> allStations = Maps.newHashMap();


    public SncfApiService() {
        // File reading
        List<String[]> rawStops = Lists.newArrayList();
        List<String[]> rawStopLines = Lists.newArrayList();
        try {
            InputStreamReader stopCoordInputStreamReader = new InputStreamReader(SncfApiService.class.getClassLoader().getResourceAsStream("gare_20120319.csv"), "ISO-8859-1");
            CSVReader stopCoordinatesReader = new CSVReader(stopCoordInputStreamReader, ';');

            InputStreamReader lineStopsInputStreamReader = new InputStreamReader(SncfApiService.class.getClassLoader().getResourceAsStream("ligne_par_gare_IDF.csv"), "ISO-8859-1");
            CSVReader stopLinesReader = new CSVReader(lineStopsInputStreamReader, ';');

            rawStops = stopCoordinatesReader.readAll();
            rawStops = rawStops.subList(1, rawStops.size());
            rawStopLines = stopLinesReader.readAll();
            rawStopLines = rawStopLines.subList(1, rawStopLines.size());
        } catch (FileNotFoundException e) {
            logger.error("SNCF : File not found", e);
        } catch (IOException e) {
            logger.error("SNCF : I/O error", e);
        }
        logger.info("SNCF coordinates and lines loaded");

        // Conversion
        for (String[] rawStop : rawStops) {
            allStops.add(new SncfStopModel(rawStop));
        }
        logger.info("SNCF stops : " + allStops.size());

        for (String[] rawLine : rawStopLines) {
            SncfLineModel stopLine = new SncfLineModel(rawLine);
            allStopLines.put(stopLine.getUic(), stopLine);
        }
        logger.info("SNCF stop lines : " + allStopLines.size());


        for (SncfStopModel stop : allStops) {
            for (SncfLineModel lineStop : allStopLines.get(stop.getUic())) {
                String stationId = lineStop.getLineNumber() + "-" + stop.getUic();
                allStations.put( stationId, new Station(StationType.valueOf(lineStop.getType()), stationId, new Coordinates(stop.getLatitude(), stop.getLongitude()), lineStop.getLineNumber(), lineStop.getName()));
            }
        }
        logger.info("RATP loaded stops : " + allStations.size());

    }

    public Collection<Station> getAllStops() {
        return allStations.values();
    }

    public Collection<Station> getStopsArround(double latitude, double longitude, double distanceMax) {
        List<Station> stationsArround = Lists.newArrayList();
        for(Station station : allStations.values()){
            double distance = distance(latitude, longitude, station.getLatitude(), station.getLongitude());
            if(distance <= distanceMax){
                station.setDistance(distance);
                stationsArround.add(station);
            }
        }
        return stationsArround;
    }
}
