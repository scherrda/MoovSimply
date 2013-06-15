package fr.duchesses.moov.apis;

import au.com.bytecode.opencsv.CSVReader;
import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Transport;
import fr.duchesses.moov.models.TransportType;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static fr.duchesses.moov.apis.DistanceHelper.distance;

@Component
public class RatpApiService implements ApiService {

    private static final Logger logger = Logger.getLogger(RatpApiService.class);

    public Collection<Transport> getAllStops() {

        List<Transport> result = new ArrayList<Transport>();

        try {

            InputStreamReader stopCoordInputStreamReader = new InputStreamReader(this.getClass().getClassLoader()
                    .getResourceAsStream("ratp_arret_graphique.csv"));
            CSVReader stopCoordinatesReader = new CSVReader(stopCoordInputStreamReader, '#');

            InputStreamReader lineStopsInputStreamReader = new InputStreamReader(this.getClass().getClassLoader()
                    .getResourceAsStream("ratp_arret_ligne.csv"));
            CSVReader stopLinesReader = new CSVReader(lineStopsInputStreamReader, '#');

            // TODO load data in memory
            List<String[]> stopsCoordinates = stopCoordinatesReader.readAll();
            List<String[]> stopLines = stopLinesReader.readAll();

            for (String[] stop : stopsCoordinates) {
                addTransports(result, stopLines, stop, null);

            }

        } catch (FileNotFoundException e) {
            logger.error("RATP : File not found", e);
        } catch (IOException e) {
            logger.error("RATP : I/O error", e);
        }

        return result;
    }


    public Collection<Transport> getStopsForCoordinates(double latitude, double longitude, double distanceMax) {

        List<Transport> result = new ArrayList<Transport>();

        try {

            InputStreamReader stopCoordInputStreamReader = new InputStreamReader(this.getClass().getClassLoader()
                    .getResourceAsStream("ratp_arret_graphique.csv"));
            CSVReader stopCoordinatesReader = new CSVReader(stopCoordInputStreamReader, '#');

            InputStreamReader lineStopsInputStreamReader = new InputStreamReader(this.getClass().getClassLoader()
                    .getResourceAsStream("ratp_arret_ligne.csv"));
            CSVReader stopLinesReader = new CSVReader(lineStopsInputStreamReader, '#');

            // TODO load data in memory
            List<String[]> stopsCoordinates = stopCoordinatesReader.readAll();
            List<String[]> stopLines = stopLinesReader.readAll();

            for (String[] stop : stopsCoordinates) {
                double distanceFromPoint = distance(latitude, longitude, Double.valueOf(stop[2]), Double.valueOf(stop[1]));
                if (distanceFromPoint < distanceMax) {
                    addTransports(result, stopLines, stop, distanceFromPoint);
                }
            }

        } catch (FileNotFoundException e) {
            logger.error("File not found", e);
        } catch (IOException e) {
            logger.error("I/O error", e);
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
