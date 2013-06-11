package fr.duchesses.moov.rest;


import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import fr.duchesses.moov.apis.AutolibApiService;
import fr.duchesses.moov.apis.RatpApiService;
import fr.duchesses.moov.apis.VelibApiService;
import fr.duchesses.moov.models.Transport;

@Path("/moovin")
public class HelloMoovResource {
	
	private VelibApiService velibApiService ;
	
    private RatpApiService ratpApiService;
    
    private AutolibApiService autolibApiService;

    public HelloMoovResource() {
        this.velibApiService = new VelibApiService();//TODO inject
        this.ratpApiService = new RatpApiService();//TODO inject
        this.autolibApiService = new AutolibApiService();//TODO inject
    }

    @GET
    @Path("/hello")
    @Produces("text/plain")
    public String sayHello() {
        return "Hello";
    }

    @GET
    @Path("/where")
    @Produces("text/plain")
    public String getWhereIAm(@QueryParam("LAT") String lat, @QueryParam("LNG") String lng) {
        return "Hello, you are located @ :  LAT:" + lat + " LNG : " + lng;
    }

    @GET
    @Path("/around")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Transport> getTransportsAroundMe(@QueryParam("LAT") String lat, @QueryParam("LNG") String lng) {
    	Collection<Transport> result= new ArrayList<Transport>();
    	result.addAll(ratpApiService.getStopsForCoordinates(Double.valueOf(lat), Double.valueOf(lng)));
    	result.addAll(autolibApiService.getAutolibs(Double.valueOf(lat), Double.valueOf(lng), 1000));
    	 return result;
    }

    @GET
    @Path("/transports")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Transport> getAllTransports() {
    	Collection<Transport> result= new ArrayList<Transport>();
    	result.addAll(ratpApiService.getAllStops());
    	result.addAll(autolibApiService.getAutolibsParis());
        return result;
    }
}