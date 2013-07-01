package fr.duchesses.moov.apis;

import fr.duchesses.moov.models.MetroDirection;
import fr.duchesses.moov.models.Station;
import fr.duchesses.moov.models.StationType;
import fr.duchesses.moov.models.ratp.RatpStopModel;

import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collection;

import static org.fest.assertions.Assertions.assertThat;

public class RatpApiServiceTest {

    private static RatpApiService service;

    @BeforeClass
    public static void setUp() {
        service = new RatpApiService();
    }

    @Test
    public void shouldReturnAllStops() throws Exception {
        Collection<Station> results = service.getAllStops();
        assertThat(results).hasSize(16858);
    }
    
    @Test
    public void shouldGetRealTimeStationInfo() throws Exception{
    	assertThat(service.getStationWithRealTimeInfo("solferino","12",MetroDirection.A).getDirection()).contains("Direction : Front Populaire");
    	assertThat(service.getStationWithRealTimeInfo("solferino","12",MetroDirection.R).getDirection()).contains("Direction : Mairie d'Issy");
    }
    
    @Test
    @Ignore //To test all existing metro station real time informations.
    public void shouldGetRealTimeInfoForAllMetroStation(){
    	for(Station stop : service.getAllStops()){
    		if(stop.getType()==StationType.METRO && StringUtils.isNotBlank(stop.getFormattedName())){
    	    	service.getStationWithRealTimeInfo(stop.getFormattedName(),stop.getLineNumber(),MetroDirection.A); 
    	    	service.getStationWithRealTimeInfo(stop.getFormattedName(),stop.getLineNumber(),MetroDirection.R);    			
    		}
    	}
    }
}
