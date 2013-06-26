package fr.duchesses.moov.apis;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.ServiceType;
import fr.duchesses.moov.models.Station;
import fr.duchesses.moov.models.StationType;
import fr.duchesses.moov.models.sncf.SncfGareModel;
import fr.duchesses.moov.models.sncf.SncfLigneGareModel;
import fr.duchesses.moov.models.sncf.SncfStopModel;
import org.apache.log4j.Logger;
import org.joda.time.LocalTime;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
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
    private Map<String, Station> allTransilienStations = Maps.newHashMap();


    public SncfApiService() {
        // File reading
        List<String[]> rawGares = Lists.newArrayList();
        List<String[]> rawLignesGare = Lists.newArrayList();
        List<String[]> rawStops = Lists.newArrayList();
        List<String[]> rawStopTimes = Lists.newArrayList();
        try {
            InputStreamReader garesISReader = new InputStreamReader(SncfApiService.class.getClassLoader().getResourceAsStream("gare_20120319.csv"), "ISO-8859-1");
            CSVReader garesReader = new CSVReader(garesISReader, ';');

            InputStreamReader lignesGareISReader = new InputStreamReader(SncfApiService.class.getClassLoader().getResourceAsStream("ligne_par_gare_IDF.csv"), "ISO-8859-1");
            CSVReader lignesGareReader = new CSVReader(lignesGareISReader, ';');

            InputStreamReader stopsISReader = new InputStreamReader(SncfApiService.class.getClassLoader().getResourceAsStream("stops.txt"), "UTF-8");
            CSVReader stopsReader = new CSVReader(stopsISReader, ',');

            InputStreamReader stopTimesISReader = new InputStreamReader(SncfApiService.class.getClassLoader().getResourceAsStream("stop_times.txt"), "UTF-8");
            CSVReader stopTimesReader = new CSVReader(stopTimesISReader, ',');

            rawGares = garesReader.readAll();
            rawGares = rawGares.subList(1, rawGares.size());
            rawLignesGare = lignesGareReader.readAll();
            rawLignesGare = rawLignesGare.subList(1, rawLignesGare.size());
            rawStops = stopsReader.readAll();
            rawStops = rawStops.subList(1, rawStops.size());
            rawStopTimes = stopTimesReader.readAll();
            rawStopTimes = rawStopTimes.subList(1, rawStopTimes.size());
        } catch (FileNotFoundException e) {
            logger.error("SNCF : File not found", e);
        } catch (IOException e) {
            logger.error("SNCF : I/O error", e);
        }

        // Conversion
        List<SncfGareModel> allGares = Lists.newArrayList();
        HashMultimap<Integer, SncfLigneGareModel> allLignesGare = HashMultimap.create();
        HashMultimap<String, SncfStopModel> allStops = HashMultimap.create();

        for (String[] rawGare : rawGares) {
            allGares.add(new SncfGareModel(rawGare));
        }
        for (String[] rawLine : rawLignesGare) {
            SncfLigneGareModel ligneGare = new SncfLigneGareModel(rawLine);
            allLignesGare.put(ligneGare.getUic(), ligneGare);
        }
        for (String[] rawStop : rawStops) {
            SncfStopModel stop = new SncfStopModel(rawStop);
            allStops.put(stop.getName(), stop);
        }

        // Liaison des 2 systèmes
        for (SncfGareModel gare : allGares) {
            for (SncfLigneGareModel ligneGare : allLignesGare.get(gare.getUic())) {
                Set<SncfStopModel> stopsSet = allStops.get(gare.getLabel());
                if (!stopsSet.isEmpty()) {
                    SncfStopModel stop = (SncfStopModel) stopsSet.toArray()[0];
                    String stationId = ligneGare.getLineNumber() + "-" + gare.getUic();
                    allStations.put(stationId, new Station(ServiceType.SNCF, StationType.valueOf(ligneGare.getType()), stationId, new Coordinates(stop.getLatitude(), stop.getLongitude()), ligneGare.getLineNumber(), stop.getName()));
                } else {
                    logger.warn("Pas de gare correspondante pour : " + gare.getLabel());
                }
            }
        }

    }

    public Collection<Station> getAllStops() {
        return allStations.values();
    }

    public Collection<Station> getStopsArround(double latitude, double longitude, double distanceMax) {
        List<Station> stationsArround = Lists.newArrayList();
        for (Station station : allStations.values()) {
            double distance = distance(latitude, longitude, station.getLatitude(), station.getLongitude());
            if (distance <= distanceMax) {
                station.setDistance(distance);
                stationsArround.add(station);
            }
        }
        return stationsArround;
    }

    public Station getStation(String stationId) {
        //TODO should get detail station : with realtime data
        return allStations.get(stationId);
    }

    public Collection<LocalTime> getNextStopTimes(String stopId) {
        List<LocalTime> nextTimes = Lists.newArrayList();

        return nextTimes;
    }
}
