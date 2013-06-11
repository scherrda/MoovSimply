package fr.duchesses.moov.apis;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;

import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Transport;
import fr.duchesses.moov.models.TransportType;
@SuppressWarnings("unchecked")
@Component
public class AutolibApiService implements ApiService{
  
	@Autowired
	private RestTemplate restTemplate;
	
	public Collection<Transport> getAutolibs(double lat, double lan, double distance){
		 List<Transport> transports = Lists.newArrayList();
		 String recordsUrl = "http://datastore.opendatasoft.com/opendata.paris.fr/api/records/1.0/search?dataset=stations_et_espaces_autolib_de_la_metropole_parisienne&geofilter.distance="+lat+","+lan+","+distance;
			Map<String, Object> response = restTemplate().getForObject(recordsUrl, Map.class);
			extractData(transports, response);
			
		 return transports;
	}
	
	public Collection<Transport> getAutolibsParis(){
		 List<Transport> transports = Lists.newArrayList();
		 String recordsUrl = "http://datastore.opendatasoft.com/opendata.paris.fr/api/records/1.0/search?dataset=stations_et_espaces_autolib_de_la_metropole_parisienne&refine.ville=paris&rows=1000";
			
			Map<String, Object> response = restTemplate().getForObject(recordsUrl, Map.class);
			extractData(transports, response);
			
		 return transports;
	}

	private void extractData(List<Transport> transports,
			Map<String, Object> response) {
		List<Map<String, Object> > records = (List<Map<String, Object> > ) response.get("records");
		for (Map<String, Object>  record : records) {
			Map<String, Object> feilds = (Map<String, Object>) record.get("fields");
			List<String> geometry = (List<String>) feilds.get("field13");
			Double latitude = new Double (geometry.get(0));
			Double longitude = new Double( geometry.get(1));
			transports.add(new Transport(TransportType.AUTOLIB, new Coordinates(latitude, longitude)));
		}
	}
	
	
	// TODO nassima supprimer une fois la conf fini
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
		return restTemplate;
	}
	
	
	
}
