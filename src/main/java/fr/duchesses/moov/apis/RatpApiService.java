package fr.duchesses.moov.apis;

import static fr.duchesses.moov.apis.DistanceHelper.distance;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import au.com.bytecode.opencsv.CSVReader;
import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Transport;
import fr.duchesses.moov.models.TransportType;
@Component
public class RatpApiService implements ApiService {

    Logger logger = Logger.getLogger(RatpApiService.class);

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
                addTransports(result, stopLines, stop);

            }

        } catch (FileNotFoundException e) {
            logger.error("File not found", e);
        } catch (IOException e) {
            logger.error("I/O error", e);
        }

        return result;
    }


    public Collection<Transport> getStopsForCoordinates(double latitude, double longitude) {

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
                if (distance(latitude, longitude, Double.valueOf(stop[2]), Double.valueOf(stop[1])) < 0.5) {
                    addTransports(result, stopLines, stop);
                }
            }

        } catch (FileNotFoundException e) {
            logger.error("File not found", e);
        } catch (IOException e) {
            logger.error("I/O error", e);
        }

        return result;
    }

    private void addTransports(List<Transport> result, List<String[]> stopLines, String[] stop) {
        for (String[] stopLine : stopLines) {
            if (stopLine[0].equals(stop[0])) {
                String[] lines = stopLine[1].split(" ", 2);
                if (lines.length == 2) {
                    Transport transport = new Transport(TransportType.valueOf(stop[5].toUpperCase()), new Coordinates(
                            Double.parseDouble(stop[2]), Double.parseDouble(stop[1])), lines[0], lines[1]);
                    result.add(transport);
                } else {
                    Transport transport = new Transport(TransportType.valueOf(stop[5].toUpperCase()), new Coordinates(
                            Double.parseDouble(stop[2]), Double.parseDouble(stop[1])), lines[0], null);
                    result.add(transport);
                }
            }
        }
    }

}
