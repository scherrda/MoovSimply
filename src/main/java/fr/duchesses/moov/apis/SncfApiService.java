package fr.duchesses.moov.apis;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.ServiceType;
import fr.duchesses.moov.models.Station;
import fr.duchesses.moov.models.StationType;
import fr.duchesses.moov.models.sncf.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static fr.duchesses.moov.apis.DistanceHelper.distance;

@Component
public class SncfApiService implements ApiService {

    private static final Logger logger = Logger.getLogger(SncfApiService.class);

    private Map<String, Station> allStations = Maps.newHashMap();
    HashMultimap<String, ApiSncfStopTime> allStopTimes = HashMultimap.create();

    public SncfApiService() {
        // File reading
        List<String[]> rawGares = readFileAndGetContents("gare_20120319.csv", "ISO-8859-1", ';');
        List<String[]> rawLignesGare = readFileAndGetContents("ligne_par_gare_IDF.csv", "ISO-8859-1", ';');
        List<String[]> rawStops = readFileAndGetContents("stops.txt", "UTF-8", ',');
        List<String[]> rawStopTimes = readFileAndGetContents("stop_times.txt", "UTF-8", ',');

        // Conversion
        List<ApiSncfGare> allGares = Lists.newArrayList();
        HashMultimap<Integer, ApiSncfLigneGare> allLignesGare = HashMultimap.create();
        HashMultimap<String, ApiSncfStop> allStops = HashMultimap.create();

        for (String[] rawGare : rawGares) {
            allGares.add(new ApiSncfGare(rawGare));
        }
        for (String[] rawLine : rawLignesGare) {
            ApiSncfLigneGare ligneGare = new ApiSncfLigneGare(rawLine);
            allLignesGare.put(ligneGare.getUic(), ligneGare);
        }
        for (String[] rawStop : rawStops) {
            ApiSncfStop stop = new ApiSncfStop(rawStop);
            allStops.put(stop.getName(), stop);
        }
        for (String[] rawStopTime : rawStopTimes) {
            ApiSncfStopTime stopTime = new ApiSncfStopTime(rawStopTime);
            allStopTimes.put(stopTime.getStopId(), stopTime);
        }

        // Aggregation
        for (ApiSncfGare gare : allGares) {
            for (ApiSncfLigneGare ligneGare : allLignesGare.get(gare.getUic())) {
                Set<ApiSncfStop> stopsSet = allStops.get(gare.getLabel());
                if (!stopsSet.isEmpty()) {
                    ApiSncfStop stop = (ApiSncfStop) stopsSet.toArray()[0];
                    String stopId = stop.getStopId();
                    allStations.put(stopId, new Station(ServiceType.SNCF, StationType.valueOf(ligneGare.getType()), stopId, new Coordinates(stop.getLatitude(), stop.getLongitude()), ligneGare.getLineNumber(), stop.getName()));
                } else {
                    logger.warn("Pas de gare correspondante pour : " + gare.getLabel());
                }
            }
        }

    }

    private List<String[]> readFileAndGetContents(String fileName, String charset, char separator) {
        List<String[]> rawContents = Lists.newArrayList();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(SncfApiService.class.getClassLoader().getResourceAsStream(fileName), charset);
            CSVReader reader = new CSVReader(inputStreamReader, separator);
            rawContents = reader.readAll();
            rawContents = rawContents.subList(1, rawContents.size());
        } catch (IOException e) {
            logger.error("SNCF : I/O error on file " + fileName, e);
        }
        return rawContents;
    }

    public Collection<Station> getAllStops() {
        return allStations.values();
    }

    public Collection<Station> getStopsAround(double latitude, double longitude, double distanceMax) {
        List<Station> stationsAround = Lists.newArrayList();
        for (Station station : allStations.values()) {
            double distance = distance(latitude, longitude, station.getLatitude(), station.getLongitude());
            if (distance <= distanceMax) {
                station.setDistance(distance);
                stationsAround.add(station);
            }
        }
        return stationsAround;
    }

    public SncfStation getStation(String stopId) {
        Station station = allStations.get(stopId);
        if (station == null) return null;

        SncfStation sncfStation = new SncfStation(station);
        for (ApiSncfStopTime stopTime : allStopTimes.get(stopId)) {
            sncfStation.addTime(stopTime.getArrivalTime());
        }
        sncfStation.sortAndTruncateNextStopTimes();
        return sncfStation;
    }
}
