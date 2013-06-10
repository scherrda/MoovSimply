package fr.duchesses.moov.rest;


import fr.duchesses.moov.apis.RatpApiService;
import fr.duchesses.moov.apis.VelibApiService;
import fr.duchesses.moov.models.Transport;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

@Path("/moovin")
public class HelloMoovResource {
    private VelibApiService velibServiceApi ;

    private RatpApiService ratpApiService;

    public HelloMoovResource() {
        this.velibServiceApi = new VelibApiService();//TODO inject
        this.ratpApiService = new RatpApiService();//TODO inject
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
        return ratpApiService.getStopsForCoordinates(Double.valueOf(lat), Double.valueOf(lng));
    }

    @GET
    @Path("/transports")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Transport> getAllTransports() {
        return ratpApiService.getAllStops();
    }
}