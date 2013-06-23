package fr.duchesses.moov.apis;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Station;
import fr.duchesses.moov.models.StationType;
import fr.duchesses.moov.models.ratp.RatpLineModel;
import fr.duchesses.moov.models.ratp.RatpStopModel;
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
public class RatpApiService implements ApiService {

    private static final Logger logger = Logger.getLogger(RatpApiService.class);

    private List<RatpStopModel> allStops = Lists.newArrayList();
    private HashMultimap<Integer, RatpLineModel> allStopLines = HashMultimap.create();

    private Map<String, Station> allStations = Maps.newHashMap();

    public RatpApiService() {
        // File reading
        List<String[]> rawStops = Lists.newArrayList();
        List<String[]> rawStopLines = Lists.newArrayList();
        try {
            InputStreamReader stopCoordInputStreamReader = new InputStreamReader(RatpApiService.class.getClassLoader().getResourceAsStream("ratp_arret_graphique.csv"), "UTF-8");
            CSVReader stopCoordinatesReader = new CSVReader(stopCoordInputStreamReader, '#');

            InputStreamReader lineStopsInputStreamReader = new InputStreamReader(RatpApiService.class.getClassLoader().getResourceAsStream("ratp_arret_ligne.csv"), "UTF-8");
            CSVReader stopLinesReader = new CSVReader(lineStopsInputStreamReader, '#');

            rawStops = stopCoordinatesReader.readAll();
            rawStopLines = stopLinesReader.readAll();
        } catch (FileNotFoundException e) {
            logger.error("RATP : File not found", e);
        } catch (IOException e) {
            logger.error("RATP : I/O error", e);
        }
        logger.info("RATP coordinates and lines loaded");


        // Conversion
        for (String[] rawStop : rawStops) {
            allStops.add(new RatpStopModel(rawStop));
        }
        logger.info("RATP stops : " + allStops.size());
        for (String[] rawLine : rawStopLines) {
            RatpLineModel stopLine = new RatpLineModel(rawLine);
            allStopLines.put(stopLine.getStopId(), stopLine);
        }


        for (RatpStopModel stop : allStops) {
            Collection<RatpLineModel> stopLines = allStopLines.get(stop.getId());
            for(RatpLineModel lineStop : stopLines){
                String stationId = lineStop.getNumber() + "-" + stop.getId();
                allStations.put( stationId, toStation(stationId, stop.getType().toUpperCase(), stop.getLatitude(), stop.getLongitude(), lineStop.getNumber(), stop.getName()));
            }
        }

        logger.info("RATP stop lines : " + allStopLines.size());
        logger.info("RATP loaded stops : " + allStations.size());
    }

    public Collection<Station> getAllStops() {
        logger.debug("all stations size" + allStations.size());

        //TODO unused but pb with different size ! Delete when difference FIX
        List<Station> result = Lists.newArrayList();
        for (RatpStopModel stop : allStops) {
            addTransports(result, allStopLines.get(stop.getId()), stop, null);
        }
        logger.debug("all result size" + result.size());
        //TODO end

        return allStations.values();
    }


    public Collection<Station> getStopsForArround(double latitude, double longitude, double distanceMax) {
        List<Station> stationsArround = Lists.newArrayList();
        for(Station station : allStations.values()){
            double distance = distance(latitude, longitude, station.getLatitude(), station.getLongitude());
            if(distance <= distanceMax){
                station.setDistance(distance);
                stationsArround.add(station);
            }
        }
        logger.debug("nearstations size" + stationsArround.size());
        return stationsArround;
    }



    //TODO unused but pb with different size ! Delete when difference FIX
    private void addTransports(List<Station> result, Collection<RatpLineModel> stopLines, RatpStopModel stop, Double distanceFromPoint) {
        for (RatpLineModel stopLine : stopLines) {
            Station transport = new Station(StationType.valueOf(stop.getType().toUpperCase()), null, new Coordinates(
                    stop.getLatitude(), stop.getLongitude()), stopLine.getNumber(), stop.getName() + " " + stopLine.getName());

            if (distanceFromPoint != null) {
                result.add(transport.withDistance(distanceFromPoint));
            } else {
                result.add(transport);
            }
        }
    }

    private Station toStation(String id, String type, double lat, double lng, String number, String name){
        Station station = new Station(StationType.valueOf(type), id, new Coordinates(lat, lng), number, name);
        return station;
    }

    public Station getStation(String stationId) {
        //TODO should get detail station : with realtime data
        return allStations.get(stationId);
    }
}
