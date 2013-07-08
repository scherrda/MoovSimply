package fr.duchesses.moov.rest;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import fr.duchesses.moov.apis.AutolibApiService;
import fr.duchesses.moov.apis.RatpApiService;
import fr.duchesses.moov.apis.SncfApiService;
import fr.duchesses.moov.apis.VelibApiService;
import fr.duchesses.moov.models.DetailStation;
import fr.duchesses.moov.models.Station;
import fr.duchesses.moov.models.StationLight;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;

@Path("/moovin")

public class StationsResource {
    private static final Logger logger = Logger.getLogger(StationsResource.class);


    private static final double DISTANCE_AROUND_MAX = 1500d;


    private VelibApiService velibServiceApi;
    private RatpApiService ratpApiService;
    private AutolibApiService autolibApiService;
    private SncfApiService sncfApiService;

    @Inject
    public StationsResource(VelibApiService velibServiceApi, RatpApiService ratpApiService, AutolibApiService autolibApiService, SncfApiService sncfApiService) {
        this.velibServiceApi = velibServiceApi;
        this.ratpApiService = ratpApiService;
        this.autolibApiService = autolibApiService;
        this.sncfApiService = sncfApiService;
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
    public Collection<Station> getTransportsAroundMe(@QueryParam("LAT") String lat, @QueryParam("LNG") String lng) {
        List<Station> transports = Lists.newArrayList();
        if((StringUtils.isNotBlank(lat)|| !lat.equals("0")) && (StringUtils.isNotBlank(lng) || !lng.equals("0"))){
        	final Double latitude = Double.valueOf(lat);
            final Double longitude = Double.valueOf(lng);
            transports.addAll(ratpApiService.getStopsAround(latitude, longitude, DISTANCE_AROUND_MAX));
            transports.addAll(velibServiceApi.getVelibStationsArround(latitude, longitude, DISTANCE_AROUND_MAX));
            transports.addAll(autolibApiService.getAutolibs(latitude, longitude, DISTANCE_AROUND_MAX));
            transports.addAll(sncfApiService.getStopsAround(latitude, longitude, DISTANCE_AROUND_MAX));
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
    @Path("/all/light")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<StationLight> getAllSLightStations() {
        return toLighten(getAllTransports());
    }

    private Collection<StationLight> toLighten(Collection<Station> transports) {
        List<StationLight> lightenStations = Lists.newArrayList();
        for(Station station : transports){
            logger.debug("station " + station);
            lightenStations.add(new StationLight(station.getServiceType(), station.getType(), station.getStationId(), station.getLineNumber(), station.getName(), station.getCoordinates()));
        }
        return lightenStations;
    }


    @GET
    @Path("/velib/{stationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public DetailStation getVelibRealTimeData(@PathParam("stationId") String stationId) {
        return velibServiceApi.getStation(stationId);
    }
    @GET
    @Path("/ratp/{stationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Station getBusRatpRealTimeData(@PathParam("stationId") String stationId) {
        return ratpApiService.getStation(stationId);
    }
    @GET
    @Path("/sncf/{stationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Station getMetroRealTimeData(@PathParam("stationId") String stationId) {
        return sncfApiService.getStation(stationId);
    }
    @GET
    @Path("/autolib/{stationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public DetailStation getAutolibRealTimeData(@PathParam("stationId") String stationId) {
        return autolibApiService.getDetailStation(stationId);
    }

}