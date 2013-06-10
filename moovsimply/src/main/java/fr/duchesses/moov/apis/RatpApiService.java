package fr.duchesses.moov.apis;

import static fr.duchesses.moov.apis.DistanceHelper.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import au.com.bytecode.opencsv.CSVReader;
import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Transport;
import fr.duchesses.moov.models.TransportType;

public class RatpApiService implements ApiService {

    Logger logger = Logger.getLogger(RatpApiService.class);

    public Collection<Transport> getAllStops() {

        List<Transport> result = new ArrayList<Transport>();

        try {

            InputStreamReader reader = new InputStreamReader(this.getClass().getClassLoader()
                    .getResourceAsStream("ratp_arret_graphique.csv"));
            CSVReader stopCoordinates = new CSVReader(reader, '#');

            // TODO load data in memory
            List<String[]> stops = stopCoordinates.readAll();

            for (String[] stop : stops) {
                Transport transport = new Transport(TransportType.valueOf(stop[5].toUpperCase()), new Coordinates(
                        Double.parseDouble(stop[2]), Double.parseDouble(stop[1])));
                result.add(transport);
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

            InputStreamReader reader = new InputStreamReader(this.getClass().getClassLoader()
                    .getResourceAsStream("ratp_arret_graphique.csv"));
            CSVReader stopCoordinates = new CSVReader(reader, '#');

            // TODO load data in memory
            List<String[]> stops = stopCoordinates.readAll();

            for (String[] stop : stops) {
                if (distance(latitude, longitude, Double.valueOf(stop[2]), Double.valueOf(stop[1])) < 0.5) {
                    Transport transport = new Transport(TransportType.valueOf(stop[5].toUpperCase()), new Coordinates(
                            Double.parseDouble(stop[2]), Double.parseDouble(stop[1])));
                    result.add(transport);
                }
            }

        } catch (FileNotFoundException e) {
            logger.error("File not found", e);
        } catch (IOException e) {
            logger.error("I/O error", e);
        }

        return result;
    }

}
