package fr.duchesses.moov.apis;

import au.com.bytecode.opencsv.CSVReader;
import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Transport;
import fr.duchesses.moov.models.TransportType;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Component
public class RatpApiService implements ApiService {

    Logger logger = Logger.getLogger(RatpApiService.class);

    public Collection<Transport> getAllStops() {

        List<Transport> result = new ArrayList<Transport>();

        try {

            InputStreamReader reader = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("ratp_arret_graphique.csv"));
            CSVReader stopCoordinates = new CSVReader(reader, '#');

        // TODO load data in memory
        List<String[]> stops = stopCoordinates.readAll();


        for (String[] stop : stops) {
            Transport transport = new Transport(TransportType.valueOf(stop[5].toUpperCase()), new Coordinates(Double.parseDouble(stop[2]), Double.parseDouble(stop[1])));
            result.add(transport);
        }

        } catch (FileNotFoundException e) {
            logger.error("File not found", e);
        }
        catch (IOException e) {
            logger.error("I/O error", e);
        }

        return result;
    }

    public Collection<Transport> getStopsForCoordinates(double latitude, double longitude) {

        List<Transport> result = new ArrayList<Transport>();

        try {

            InputStreamReader reader = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("ratp_arret_graphique.csv"));
            CSVReader stopCoordinates = new CSVReader(reader, '#');

            // TODO load data in memory
            List<String[]> stops = stopCoordinates.readAll();


            for (String[] stop : stops) {
                if (distance(latitude, longitude, Double.valueOf(stop[2]), Double.valueOf(stop[1])) < 0.5) {
                    Transport transport = new Transport(TransportType.valueOf(stop[5].toUpperCase()), new Coordinates(Double.parseDouble(stop[2]), Double.parseDouble(stop[1])));
                    result.add(transport);
                }
            }

        } catch (FileNotFoundException e) {
            logger.error("File not found", e);
        }
        catch (IOException e) {
            logger.error("I/O error", e);
        }

        return result;
    }

    private double distance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double radianLatitude1 = Math.PI * latitude1/180;
        double radianLatitude2 = Math.PI * latitude2/180;
        double theta = longitude1 - longitude2;
        double radianTheta = Math.PI * theta/180;
        double dist = Math.sin(radianLatitude1) * Math.sin(radianLatitude2) + Math.cos(radianLatitude1) * Math.cos(radianLatitude2) * Math.cos(radianTheta);
        dist = Math.acos(dist);
        dist = dist * 180/Math.PI;
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344; // in km
        return dist;
    }

}
