package fr.duchesses.moov.apis;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
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

import static fr.duchesses.moov.apis.DistanceHelper.distance;

@Component
public class SncfApiService implements ApiService {

    private static final Logger logger = Logger.getLogger(SncfApiService.class);

    private List<SncfStopModel> allStops = Lists.newArrayList();
    private HashMultimap<Integer, SncfLineModel> allStopLines = HashMultimap.create();

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
    }

    public Collection<Station> getAllStops() {
        List<Station> result = Lists.newArrayList();
        for (SncfStopModel stop : allStops) {
            for (SncfLineModel line : allStopLines.get(stop.getUic())) {
                result.add(new Station(StationType.valueOf(line.getType()), new Coordinates(stop.getLatitude(), stop.getLongitude()), line.getLineNumber(), line.getName()));
            }
        }

        return result;
    }

    public Collection<Station> getStopsForCoordinates(double latitude, double longitude, double distanceMax) {
        List<Station> result = Lists.newArrayList();
        for (SncfStopModel stop : allStops) {
            double distanceFromPoint = distance(latitude, longitude, stop.getLatitude(), stop.getLongitude());
            if (distanceFromPoint <= distanceMax) {
                for (SncfLineModel line : allStopLines.get(stop.getUic())) {
                    result.add(new Station(StationType.valueOf(line.getType()), new Coordinates(stop.getLatitude(), stop.getLongitude()), line.getLineNumber(), stop.getName()).withDistance(distanceFromPoint));
                }
            }
        }

        return result;
    }
}
