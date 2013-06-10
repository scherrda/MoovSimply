package fr.duchesses.moov.apis;

import static fr.duchesses.moov.apis.DistanceHelper.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Transport;
import fr.duchesses.moov.models.TransportType;
import fr.duchesses.moov.models.velib.VelibStationModel;

public class VelibApiService implements ApiService {

    public Collection<Transport> getVelibs() {
        List<Transport> transports = Lists.newArrayList(new Transport(TransportType.VELIB, new Coordinates(10, 20)), new Transport(
                TransportType.VELIB, new Coordinates(10, 19)), new Transport(TransportType.VELIB, new Coordinates(10, 18)), new Transport(
                TransportType.VELIB, new Coordinates(11, 20)));
        return transports;
    }

    public List<Transport> getAllVelibStations() {
        List<VelibStationModel> velibs = Lists.newArrayList();
        try (InputStream is = this.getClass().getResourceAsStream("VelibStation.json")) {
            Type listType = new TypeToken<ArrayList<VelibStationModel>>() {
                // do nothing here.
            }.getType();
            velibs = new Gson().fromJson(new InputStreamReader(is), listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertFromVelibStationToTransport(velibs);
    }

    public List<Transport> getVelibStationsForCoordinates(Double latitude, Double longitude) {
        List<Transport> closestVelibStations = Lists.newArrayList();
        for (Transport transport : getAllVelibStations()) {
            if (distance(latitude, longitude, transport.getCoordinates().getLatitude(), transport.getCoordinates().getLongitude()) < 0.5) {
                closestVelibStations.add(transport);
            }
        }
        return closestVelibStations;
    }

    private List<Transport> convertFromVelibStationToTransport(List<VelibStationModel> velibStations) {
        List<Transport> transports = Lists.newArrayList();
        for (VelibStationModel velib : velibStations) {
            transports.add(new Transport(TransportType.VELIB, new Coordinates(velib.getLatitude(), velib.getLongitude())));
        }
        return transports;
    }
}
