package fr.duchesses.moov.rest;

import com.google.common.collect.Lists;
import fr.duchesses.moov.apis.AutolibApiService;
import fr.duchesses.moov.apis.RatpApiService;
import fr.duchesses.moov.apis.VelibApiService;
import fr.duchesses.moov.models.Transport;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;

@Path("/moovin")
public class HelloMoovResource {

    private static final double distanceAroundMax = 500d;

    private VelibApiService velibServiceApi;

    private RatpApiService ratpApiService;

    private AutolibApiService autolibApiService;

    public HelloMoovResource() {
        this.velibServiceApi = new VelibApiService();// TODO inject
        this.ratpApiService = new RatpApiService();// TODO inject
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
        List<Transport> transports = Lists.newArrayList();
        final Double latitude = Double.valueOf(lat);
        final Double longitude = Double.valueOf(lng);
        transports.addAll(ratpApiService.getStopsForCoordinates(latitude, longitude, distanceAroundMax));
        transports.addAll(velibServiceApi.getVelibStationsForCoordinates(latitude, longitude, distanceAroundMax));
        transports.addAll(autolibApiService.getAutolibs(latitude, longitude, distanceAroundMax));
        return transports;
    }

    @GET
    @Path("/transports")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Transport> getAllTransports() {
        List<Transport> transports = Lists.newArrayList();
        transports.addAll(ratpApiService.getAllStops());
        transports.addAll(velibServiceApi.getAllVelibStations());
        transports.addAll(autolibApiService.getAutolibsParis());
        return transports;
    }
}