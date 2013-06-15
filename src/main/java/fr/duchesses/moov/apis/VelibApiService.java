package fr.duchesses.moov.apis;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Transport;
import fr.duchesses.moov.models.TransportType;
import fr.duchesses.moov.models.velib.VelibStationModel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static fr.duchesses.moov.apis.DistanceHelper.distance;

@Component
public class VelibApiService implements ApiService {

    private static final Logger logger = Logger.getLogger(VelibApiService.class);

    private List<Transport> allVelibs = Lists.newArrayList();

    public VelibApiService() {
        // File reading
        List<VelibStationModel> velibStations = Lists.newArrayList();
        try (InputStream is = VelibApiService.class.getResourceAsStream("VelibStation.json")) {
            Type listType = new TypeToken<ArrayList<VelibStationModel>>() {
                // do nothing here.
            }.getType();
            velibStations = new Gson().fromJson(new InputStreamReader(is), listType);
        } catch (IOException e) {
            logger.error("Velib : I/O error");
        }

        // Conversion
        for (VelibStationModel velib : velibStations) {
            allVelibs.add(new Transport(TransportType.VELIB, new Coordinates(velib.getLatitude(), velib.getLongitude()), null, velib.getName()));
        }
        logger.info("Velibs loaded");
    }

    public List<Transport> getAllVelibStations() {
        return allVelibs;
    }

    public List<Transport> getVelibStationsForCoordinates(Double latitude, Double longitude, double distanceMax) {
        List<Transport> closestVelibStations = Lists.newArrayList();
        for (Transport transport : allVelibs) {
            double distanceFromPoint = distance(latitude, longitude, transport.getCoordinates().getLatitude(), transport.getCoordinates().getLongitude());
            if (distanceFromPoint < distanceMax) {
                closestVelibStations.add(transport.withDistance(distanceFromPoint));
            }
        }
        return closestVelibStations;
    }
}
