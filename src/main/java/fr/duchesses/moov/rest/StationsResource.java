package fr.duchesses.moov.rest;

import com.google.common.collect.Lists;
import fr.duchesses.moov.apis.AutolibApiService;
import fr.duchesses.moov.apis.RatpApiService;
import fr.duchesses.moov.apis.SncfApiService;
import fr.duchesses.moov.apis.VelibApiService;
import fr.duchesses.moov.models.Station;
import fr.duchesses.moov.models.velib.VelibStation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;

@Path("/moovin")
@Component
public class StationsResource {

    private static final double DISTANCE_AROUND_MAX = 500d;

    @Autowired
    private VelibApiService velibServiceApi;

    @Autowired
    private RatpApiService ratpApiService;

    @Autowired
    private AutolibApiService autolibApiService;

    @Autowired
    private SncfApiService sncfApiService;

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
    public Collection<Station> getTransportsAroundMe(@QueryParam("LAT") String lat, @QueryParam("LNG") String lng) {
        List<Station> transports = Lists.newArrayList();
        if((StringUtils.isNotBlank(lat)|| !lat.equals("0")) && (StringUtils.isNotBlank(lng) || !lng.equals("0"))){
        	final Double latitude = Double.valueOf(lat);
            final Double longitude = Double.valueOf(lng);
            transports.addAll(ratpApiService.getStopsForCoordinates(latitude, longitude, DISTANCE_AROUND_MAX));
            transports.addAll(velibServiceApi.getVelibStationsForCoordinates(latitude, longitude, DISTANCE_AROUND_MAX));
            transports.addAll(autolibApiService.getAutolibs(latitude, longitude, DISTANCE_AROUND_MAX));
            transports.addAll(sncfApiService.getStopsForCoordinates(latitude, longitude, DISTANCE_AROUND_MAX));
        }
        return transports;
    }

    @GET
    @Path("/transports")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Station> getAllTransports() {
        List<Station> transports = Lists.newArrayList();
        transports.addAll(ratpApiService.getAllStops());
        transports.addAll(velibServiceApi.getAllVelibStations());
        transports.addAll(autolibApiService.getAutolibsParis());
        transports.addAll(sncfApiService.getAllStops());
        return transports;
    }

    @GET
    @Path("/velib/{stationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public VelibStation getVelibRealTimeData(@PathParam("stationId") String stationId) {
        return velibServiceApi.getStation(stationId);
    }
    @GET
    @Path("/bus/{stationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Station getBusRatpRealTimeData(@PathParam("stationId") String stationId) {
        return ratpApiService.getStation(stationId);
    }
    @GET
    @Path("/metro/{stationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Station getMetroRealTimeData(@PathParam("stationId") String stationId) {
        return ratpApiService.getStation(stationId);
    }
    @GET
    @Path("/autolib/{stationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Station getAutolibRealTimeData(@PathParam("stationId") String stationId) {
        return autolibApiService.getStation(stationId);
    }

}