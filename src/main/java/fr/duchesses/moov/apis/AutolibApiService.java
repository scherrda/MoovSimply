package fr.duchesses.moov.apis;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.NotFoundException;
import fr.duchesses.moov.models.*;
import fr.duchesses.moov.models.autolib.AutolibStationModel;
import fr.duchesses.moov.models.velib.ApiVelibStationModel;
import org.apache.log4j.Logger;

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


public class AutolibApiService implements ApiService {

    private static final Logger logger = Logger.getLogger(AutolibApiService.class);
    private static final String BASE_URL = "http://datastore.opendatasoft.com/opendata.paris.fr/api/records/1.0/search?";

    private Map<String, Station> allStations = Maps.newHashMap();

    public AutolibApiService(){
        Collection<Station> stations = getAutolibsParis();
        for(Station station : stations ){
            allStations.put(station.getStationId(), station);
        }
    }


    //all in ile de france
    private String buildUrl(String criteria) {
        StringBuilder url = new StringBuilder(BASE_URL)
                .append("dataset=stations_et_espaces_autolib_de_la_metropole_parisienne");
        if(criteria != null){
            url.append("&" + criteria);
        }
        return url.toString();
    }


    //All
    public Collection<Station> getAutolibsParis() {
        List<Station> transports = Lists.newArrayList();

        Collection<AutolibStationModel> stationModels = getTransportsFromUrl(buildUrl("rows=10000"));
        for(AutolibStationModel stationModel : stationModels){
            transports.add(new Station(ServiceType.AUTOLIB, StationType.AUTOLIB, stationModel.getIdentifiant_dsp(), new Coordinates(stationModel.getLatitude(), stationModel.getLongitude()), null, stationModel.getRue()));
        }
        return transports;
    }

    //Arround
    public Collection<Station> getAutolibs(double searchLatitude, double searchLongitude, double distanceMax) {
        List<Station> transports = Lists.newArrayList();
        String recordsUrl = "http://datastore.opendatasoft.com/opendata.paris.fr/api/records/1.0/search?dataset=stations_et_espaces_autolib_de_la_metropole_parisienne&geofilter.distance=" + searchLatitude + "," + searchLongitude + "," + distanceMax;
        Collection<AutolibStationModel> stationModels = getTransportsFromUrl(recordsUrl);
        for(AutolibStationModel stationModel : stationModels){
            Station transport = new Station(ServiceType.AUTOLIB, StationType.AUTOLIB, stationModel.getIdentifiant_dsp(), new Coordinates(stationModel.getLatitude(), stationModel.getLongitude()), null, stationModel.getRue());
            double distance = distance(searchLatitude, searchLongitude, stationModel.getLatitude(), stationModel.getLongitude());
            if(distance <= distanceMax){
                transport.setDistance(distance);
                transports.add(transport);
            }
        }
        return transports;
    }


    private List<AutolibStationModel> getTransportsFromUrl(String recordsUrl) {
        List<AutolibStationModel> transports = Lists.newArrayList();
        try {
            Type listType = new TypeToken<ArrayList<JsonObject>>() {
                // do nothing here.
            }.getType();
            JsonObject searchResponse = new JsonParser().parse(readUrl(recordsUrl)).getAsJsonObject();
            List<JsonObject> rawStations = new Gson().fromJson(searchResponse.get("records"), listType);
            for (JsonObject rawStation : rawStations) {
                transports.add(new Gson().fromJson(rawStation.get("fields"), AutolibStationModel.class));
            }
        } catch (IOException e) {
            logger.error("Autolib : service not responding");
        }
        logger.info("Autolibs loaded" + transports.size() + " stations");
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
            if (null != reader)
                reader.close();
        }
    }

    public DetailStation getDetailStation(String stationId) {
        Station station = allStations.get(stationId);
        if(station == null){
            throw new NotFoundException("no autolib station found : " + stationId);
        }
        AutolibStationModel stationModel = queryForRealTimeData(station.getStationId());
        String address = stationModel.getRue() + " " + stationModel.getCode_postal();
        int nbStands = stationModel.getBornes_de_charge_autolib();
        int nbVehicles = stationModel.getNombre_total_de_bornes_de_charge()- stationModel.getBornes_de_charge_autolib();
        logger.debug("status " + stationModel.getEtat_actuel());
        StationStatus status = stationModel.getEtat_actuel().equals("Ouverte")? StationStatus.OPEN : StationStatus.CLOSED;
        return new DetailStation(address, nbStands, nbVehicles, status, 0);
    }

    private AutolibStationModel queryForRealTimeData(String stationNumber) {
        ApiVelibStationModel station = null;

        String recordsUrl = buildUrl("q=identifiant_dsp=" + stationNumber);
        List<AutolibStationModel> stationsModel = getTransportsFromUrl(recordsUrl);

        return stationsModel.get(0);
    }



}
