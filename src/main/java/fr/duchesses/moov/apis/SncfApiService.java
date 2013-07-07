package fr.duchesses.moov.apis;

import com.google.common.base.Charsets;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.ServiceType;
import fr.duchesses.moov.models.Station;
import fr.duchesses.moov.models.StationType;
import fr.duchesses.moov.models.sncf.*;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.xml.bind.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static fr.duchesses.moov.apis.DistanceHelper.distance;

public class SncfApiService implements ApiService {

    private static final Logger logger = Logger.getLogger(SncfApiService.class);

    private Map<String, Station> allStations = Maps.newHashMap();
    HashMultimap<String, ApiSncfStopTime> allStopTimes = HashMultimap.create();

    private static final String BASE_URL = "http://api.transilien.com/gare/";

    @Inject
    public SncfApiService() {
        // File reading
        List<String[]> rawGares = FileReader.getLines("sncf/gare_20120319.csv",
                Charsets.ISO_8859_1, ';', 1);
        List<String[]> rawLignesGare = FileReader.getLines(
                "sncf/ligne_par_gare_IDF.csv", Charsets.ISO_8859_1, ';', 1);
        List<String[]> rawStops = FileReader.getLines("sncf/stops.txt",
                Charsets.UTF_8, ',', 1);
        List<String[]> rawStopTimes = FileReader.getLines(
                "sncf/stop_times.txt", Charsets.US_ASCII, ',', 1);
        List<String[]> rawTrips = FileReader.getLines(
                "sncf/trips.txt", Charsets.US_ASCII, ',', 1);
        List<String[]> rawRoutes = FileReader.getLines(
                "sncf/routes.txt", Charsets.UTF_8, ',', 1);

        // Conversion
        List<ApiSncfGare> allGares = Lists.newArrayList();
        HashMultimap<Integer, ApiSncfLigneGare> allLignesGare = HashMultimap
                .create();
        HashMultimap<String, ApiSncfStop> allStops = HashMultimap.create();
        HashMap<String, ApiSncfTrip> allTrips = Maps.newHashMap();
        HashMap<String, ApiSncfRoute> allRoutes = Maps.newHashMap();

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
        for (String[] rawTrip : rawTrips) {
            ApiSncfTrip trip = new ApiSncfTrip(rawTrip);
            allTrips.put(trip.getTripId(), trip);
        }
        for (String[] rawRoute : rawRoutes) {
            ApiSncfRoute route = new ApiSncfRoute(rawRoute);
            allRoutes.put(route.getRouteId(), route);
        }

        // Data aggregration
        for (ApiSncfGare gare : allGares) {
            for (ApiSncfLigneGare ligneGare : allLignesGare.get(gare.getUic())) {
                Set<ApiSncfStop> stopsSet = allStops.get(gare.getLabel());
                if (!stopsSet.isEmpty()) {
                    ApiSncfStop stop = (ApiSncfStop) stopsSet.toArray()[0];
                    String stopId = stop.getStopId();
                    allStations.put(
                            stopId,
                            new Station(ServiceType.SNCF, StationType
                                    .valueOf(ligneGare.getType()), stopId,
                                    new Coordinates(stop.getLatitude(), stop
                                            .getLongitude()), ligneGare
                                    .getLineNumber(), stop.getName(), gare.getUic()));
                } else {
                    logger.warn("Pas de gare correspondante pour : "
                            + gare.getLabel());
                }
            }
        }
        logger.info("SNCF loaded stations : " + allStations.size());

        for (ApiSncfStopTime stopTime : allStopTimes.values()) {
            ApiSncfTrip trip = allTrips.get(stopTime.getTripId());
            ApiSncfRoute route = allRoutes.get(trip.getRouteId());
            stopTime.setRouteLongName(route.getRouteLongName());
        }
    }

    public Collection<Station> getAllStops() {
        return allStations.values();
    }

    public Collection<Station> getStopsAround(double latitude,
                                              double longitude, double distanceMax) {
        List<Station> stationsAround = Lists.newArrayList();
        for (Station station : allStations.values()) {
            double distance = distance(latitude, longitude,
                    station.getLatitude(), station.getLongitude());
            if (distance <= distanceMax) {
                station.setDistance(distance);
                stationsAround.add(station);
            }
        }
        return stationsAround;
    }

    public SncfStation getStation(String stopId) {
        Set<ApiSncfStopTime> realTimeData = null;
        Station station = allStations.get(stopId);
        if (station == null)
            return null;

        SncfStation sncfStation = new SncfStation(station);

        // TODO get
        if ("L".equals(sncfStation.getLineNumber()) || "C".equals(sncfStation.getLineNumber())) {
            realTimeData = queryForRealTimeData(String.valueOf(station.getCodeUIC()));
        }
        if (realTimeData == null) {
            realTimeData = allStopTimes.get(stopId);
        }

        for (ApiSncfStopTime stopTime : realTimeData) {
            sncfStation.addTime(stopTime.getRouteLongName(), stopTime.getArrivalTime());
        }

        sncfStation.sortAndTruncateNextStopTimes();
        return sncfStation;
    }

    private Set<ApiSncfStopTime> queryForRealTimeData(String stationNumber) {
        Set<ApiSncfStopTime> results = null;
        try {
            InputStream in = buildConnection(stationNumber);
            if (in != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                reader.readLine();
                InputStream aInputStream = new ReaderInputStream(reader);
                final JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
                final Unmarshaller unmarshaller = context.createUnmarshaller();
                final Passages passages = (Passages) unmarshaller.unmarshal(aInputStream);
                results = Sets.newHashSet();
                for (Serializable elem : passages.getContent()) {
                    if (elem instanceof JAXBElement) {
                        TrainType aTrainType = (TrainType) ((JAXBElement) elem).getValue();
                        results.add(new ApiSncfStopTime(aTrainType.getDate().getValue(), aTrainType.getTerm()));
                    }
                }
                return new TreeSet(results);
            }
        } catch (IOException e) {
            logger.error("IOException : " + e);
        } catch (JAXBException e) {
            logger.error("JAXBException : " + e);
        }
        return results;
    }

    private URL buildUrl(String stationNumber) {
        String url = new StringBuilder(BASE_URL).append(stationNumber)
                .append("/depart").toString();
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            logger.error("bad url");
        }
        return null;
    }

    private InputStream buildConnection(String stationNumber) {
        String id = "tnhtn071:AYAS$R%T";
        String encoding = DatatypeConverter.printBase64Binary(id.getBytes());
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) buildUrl(stationNumber)
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            connection.setRequestProperty("Accept", "application/xml");
            connection.setRequestProperty("vers", "1.0");
            if (200 == connection.getResponseCode()) {
                return (InputStream) connection.getInputStream();
            }

        } catch (IOException e) {
            logger.error("Error on create connection.");
        } finally {
            //connection.disconnect();
        }
        return null;
    }

//	public static void main(String[] args) {
//		queryForRealTimeData("87393009");
//		Set<ApiSncfStopTime> realTimeData = queryForRealTimeData("87393009");
//		System.out.println(realTimeData);
//	}

}
