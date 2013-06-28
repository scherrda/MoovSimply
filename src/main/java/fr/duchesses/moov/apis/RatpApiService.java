package fr.duchesses.moov.apis;

import com.google.common.base.Charsets;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.ServiceType;
import fr.duchesses.moov.models.Station;
import fr.duchesses.moov.models.StationType;
import fr.duchesses.moov.models.ratp.RatpLineModel;
import fr.duchesses.moov.models.ratp.RatpStopModel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static fr.duchesses.moov.apis.DistanceHelper.distance;

@Component
public class RatpApiService implements ApiService {

    private static final Logger logger = Logger.getLogger(RatpApiService.class);

    private Map<String, Station> allStations = Maps.newHashMap();

    @Inject
    public RatpApiService(FileReader fileReader) {
        // File reading
        List<String[]> rawStops = fileReader.getLines("ratp_arret_graphique.csv", Charsets.UTF_8, '#', 0);
        List<String[]> rawStopLines = fileReader.getLines("ratp_arret_ligne.csv", Charsets.UTF_8, '#', 0);

        // Conversion
        List<RatpStopModel> allStops = Lists.newArrayList();
        HashMultimap<Integer, RatpLineModel> allStopLines = HashMultimap.create();

        for (String[] rawStop : rawStops) {
            allStops.add(new RatpStopModel(rawStop));
        }
        for (String[] rawLine : rawStopLines) {
            RatpLineModel stopLine = new RatpLineModel(rawLine);
            allStopLines.put(stopLine.getStopId(), stopLine);
        }

        // Data aggregration
        for (RatpStopModel stop : allStops) {
            Collection<RatpLineModel> stopLines = allStopLines.get(stop.getId());
            for (RatpLineModel lineStop : stopLines) {
                String stationId = lineStop.getNumber() + "-" + stop.getId();
                allStations.put(stationId, toStation(stationId, stop.getType().toUpperCase(), stop.getLatitude(), stop.getLongitude(), lineStop.getNumber(), stop.getName()));
            }
        }
        logger.info("RATP loaded stations : " + allStations.size());
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
        logger.debug("RATP near stations size: " + stationsAround.size());
        return stationsAround;
    }

    private Station toStation(String id, String type, double lat, double lng, String number, String name) {
        Station station = new Station(ServiceType.RATP, StationType.valueOf(type), id, new Coordinates(lat, lng), number, name);
        return station;
    }

    public Station getStation(String stationId) {
        //TODO should get detail station : with realtime data
        return allStations.get(stationId);
    }
}
