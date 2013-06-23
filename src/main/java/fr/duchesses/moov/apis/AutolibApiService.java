package fr.duchesses.moov.apis;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.NotFoundException;
import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Station;
import fr.duchesses.moov.models.StationType;
import fr.duchesses.moov.models.autolib.AutolibStationModel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static fr.duchesses.moov.apis.DistanceHelper.distance;

@Component
public class AutolibApiService implements ApiService {

    private static final Logger logger = Logger.getLogger(AutolibApiService.class);
    private Map<String, Station> allStations = Maps.newHashMap();

    public AutolibApiService(){
        Collection<Station> stations = getAutolibsParis();
        for(Station station : stations ){
            allStations.put(station.getStationId(), station);
        }
    }


    public Collection<Station> getAutolibs(double searchLatitude, double searchLongitude, double distanceMax) {
        String recordsUrl = "http://datastore.opendatasoft.com/opendata.paris.fr/api/records/1.0/search?dataset=stations_et_espaces_autolib_de_la_metropole_parisienne&geofilter.distance=" + searchLatitude + "," + searchLongitude + "," + distanceMax;
        return getTransportsFromUrl(recordsUrl, searchLatitude, searchLongitude);
    }

    public Collection<Station> getAutolibsParis() {
        String recordsUrl = "http://datastore.opendatasoft.com/opendata.paris.fr/api/records/1.0/search?dataset=stations_et_espaces_autolib_de_la_metropole_parisienne&refine.ville=paris&rows=1000";
        return getTransportsFromUrl(recordsUrl, null, null);
    }

    private List<Station> getTransportsFromUrl(String recordsUrl, Double searchLatitude, Double searchLongitude) {
        List<Station> transports = Lists.newArrayList();
        try {
            Type listType = new TypeToken<ArrayList<JsonObject>>() {
                // do nothing here.
            }.getType();
            JsonObject searchResponse = new JsonParser().parse(readUrl(recordsUrl)).getAsJsonObject();
            List<JsonObject> rawStations = new Gson().fromJson(searchResponse.get("records"), listType);
            for (JsonObject rawStation : rawStations) {
                AutolibStationModel stationModel = new Gson().fromJson(rawStation.get("fields"), AutolibStationModel.class);
                Station transport = new Station(StationType.AUTOLIB, stationModel.getIdentifiant_autolib(), new Coordinates(stationModel.getLatitude(), stationModel.getLongitude()), null, stationModel.getRue());
                if (searchLatitude != null && searchLongitude != null) {
                    double distanceFromPoint = distance(searchLatitude, searchLongitude, stationModel.getLatitude(), stationModel.getLongitude());
                    transports.add(transport.withDistance(distanceFromPoint));
                } else {
                    transports.add(transport);
                }
            }
        } catch (IOException e) {
            logger.error("Autolib : service not responding");
        }
        logger.info("Autolibs loaded");
        return transports;
    }

    private static String readUrl(String urlString) throws IOException {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    public Station getStation(String stationId) {
        Station station = allStations.get(stationId);
        if(station == null){
            throw new NotFoundException("no autolib station found : " + stationId);
        }

        //TODO should get detail station : with realtime data
        return station;
    }
}
