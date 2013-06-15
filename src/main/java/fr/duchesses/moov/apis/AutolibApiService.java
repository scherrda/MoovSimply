package fr.duchesses.moov.apis;

import com.google.common.collect.Lists;
import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Transport;
import fr.duchesses.moov.models.TransportType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static fr.duchesses.moov.apis.DistanceHelper.distance;

@SuppressWarnings("unchecked")
@Component
public class AutolibApiService implements ApiService {

    private static final Logger logger = Logger.getLogger(AutolibApiService.class);

    @Autowired
    private RestTemplate restTemplate;

    public Collection<Transport> getAutolibs(double latitude, double longitude, double distanceMax) {
        List<Transport> transports = Lists.newArrayList();
        String recordsUrl = "http://datastore.opendatasoft.com/opendata.paris.fr/api/records/1.0/search?dataset=stations_et_espaces_autolib_de_la_metropole_parisienne&geofilter.distance=" + latitude + "," + longitude + "," + distanceMax;
        Map<String, Object> response = restTemplate().getForObject(recordsUrl, Map.class);
        extractData(transports, response, latitude, longitude);

        return transports;
    }

    public Collection<Transport> getAutolibsParis() {
        List<Transport> transports = Lists.newArrayList();
        String recordsUrl = "http://datastore.opendatasoft.com/opendata.paris.fr/api/records/1.0/search?dataset=stations_et_espaces_autolib_de_la_metropole_parisienne&refine.ville=paris&rows=1000";

        Map<String, Object> response = restTemplate().getForObject(recordsUrl, Map.class);
        extractData(transports, response, null, null);

        return transports;
    }

    private void extractData(List<Transport> transports, Map<String, Object> response, Double searchLatitude, Double searchLongitude) {
        List<Map<String, Object>> records = (List<Map<String, Object>>) response.get("records");
        for (Map<String, Object> record : records) {
            Map<String, Object> fields = (Map<String, Object>) record.get("fields");
            List<String> geometry = (List<String>) fields.get("field13");
            Double stationLatitude = new Double(geometry.get(0));
            Double stationLongitude = new Double(geometry.get(1));
            Transport transport = new Transport(TransportType.AUTOLIB, new Coordinates(stationLatitude, stationLongitude), null, null);
            if (searchLatitude != null && searchLongitude != null) {
                double distanceFromPoint = distance(searchLatitude, searchLongitude, stationLatitude, stationLongitude);
                transports.add(transport.withDistance(distanceFromPoint));
            } else {
                transports.add(transport);
            }
        }
    }

    // TODO nassima supprimer une fois la conf fini
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        return restTemplate;
    }

}
