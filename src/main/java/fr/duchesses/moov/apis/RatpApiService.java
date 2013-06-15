package fr.duchesses.moov.apis;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.Lists;
import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Transport;
import fr.duchesses.moov.models.TransportType;
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

    private List<String[]> stopsCoordinates = Lists.newArrayList();
    private List<String[]> stopLines = Lists.newArrayList();

    public RatpApiService() {
        try {
            InputStreamReader stopCoordInputStreamReader = new InputStreamReader(RatpApiService.class.getClassLoader().getResourceAsStream("ratp_arret_graphique.csv"));
            CSVReader stopCoordinatesReader = new CSVReader(stopCoordInputStreamReader, '#');

            InputStreamReader lineStopsInputStreamReader = new InputStreamReader(RatpApiService.class.getClassLoader().getResourceAsStream("ratp_arret_ligne.csv"));
            CSVReader stopLinesReader = new CSVReader(lineStopsInputStreamReader, '#');

            stopsCoordinates = stopCoordinatesReader.readAll();
            stopLines = stopLinesReader.readAll();
        } catch (FileNotFoundException e) {
            logger.error("RATP : File not found", e);
        } catch (IOException e) {
            logger.error("RATP : I/O error", e);
        }
        logger.info("RATP coordinates and lines loaded");
    }

    public Collection<Transport> getAllStops() {
        List<Transport> result = Lists.newArrayList();
        for (String[] stop : stopsCoordinates) {
            addTransports(result, stopLines, stop, null);
        }

        return result;
    }


    public Collection<Transport> getStopsForCoordinates(double latitude, double longitude, double distanceMax) {
        List<Transport> result = Lists.newArrayList();
        for (String[] stop : stopsCoordinates) {
            double distanceFromPoint = distance(latitude, longitude, Double.valueOf(stop[2]), Double.valueOf(stop[1]));
            if (distanceFromPoint < distanceMax) {
                addTransports(result, stopLines, stop, distanceFromPoint);
            }
        }

        return result;
    }

    private void addTransports(List<Transport> result, List<String[]> stopLines, String[] stop, Double distanceFromPoint) {
        for (String[] stopLine : stopLines) {
            if (stopLine[0].equals(stop[0])) {
                String[] lines = stopLine[1].split(" ", 2);
                Transport transport;

                if (lines.length == 2) {
                    transport = new Transport(TransportType.valueOf(stop[5].toUpperCase()), new Coordinates(
                            Double.parseDouble(stop[2]), Double.parseDouble(stop[1])), lines[0], lines[1]);
                } else {
                    transport = new Transport(TransportType.valueOf(stop[5].toUpperCase()), new Coordinates(
                            Double.parseDouble(stop[2]), Double.parseDouble(stop[1])), lines[0], null);
                }

                if (distanceFromPoint != null) {
                    result.add(transport.withDistance(distanceFromPoint));
                } else {
                    result.add(transport);
                }
            }
        }
    }

}
