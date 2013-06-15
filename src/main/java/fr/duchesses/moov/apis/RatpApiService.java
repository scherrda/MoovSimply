package fr.duchesses.moov.apis;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Transport;
import fr.duchesses.moov.models.TransportType;
import fr.duchesses.moov.models.ratp.RatpLineModel;
import fr.duchesses.moov.models.ratp.RatpStopModel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

import static fr.duchesses.moov.apis.DistanceHelper.distance;

@Component
public class RatpApiService implements ApiService {

    private static final Logger logger = Logger.getLogger(RatpApiService.class);

    private List<RatpStopModel> allStops = Lists.newArrayList();
    private HashMultimap<Integer, RatpLineModel> allStopLines = HashMultimap.create();

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
        logger.info("RATP stop lines : " + allStopLines.size());
    }

    public Collection<Transport> getAllStops() {
        List<Transport> result = Lists.newArrayList();
        for (RatpStopModel stop : allStops) {
            addTransports(result, allStopLines.get(stop.getId()), stop, null);
        }

        return result;
    }


    public Collection<Transport> getStopsForCoordinates(double latitude, double longitude, double distanceMax) {
        List<Transport> result = Lists.newArrayList();
        for (RatpStopModel stop : allStops) {
            double distanceFromPoint = distance(latitude, longitude, stop.getLatitude(), stop.getLongitude());
            if (distanceFromPoint < distanceMax) {
                addTransports(result, allStopLines.get(stop.getId()), stop, distanceFromPoint);
            }
        }

        return result;
    }

    private void addTransports(List<Transport> result, Collection<RatpLineModel> stopLines, RatpStopModel stop, Double distanceFromPoint) {
        for (RatpLineModel stopLine : stopLines) {
            Transport transport = new Transport(TransportType.valueOf(stop.getType().toUpperCase()), new Coordinates(
                    stop.getLatitude(), stop.getLongitude()), stopLine.getNumber(), stop.getName() + " " + stopLine.getName());

            if (distanceFromPoint != null) {
                result.add(transport.withDistance(distanceFromPoint));
            } else {
                result.add(transport);
            }
        }
    }

}
