package fr.duchesses.moov.apis;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.NotFoundException;
import fr.duchesses.moov.models.*;
import fr.duchesses.moov.models.velib.ApiVelibStationModel;
import fr.duchesses.moov.models.velib.VelibStation;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fr.duchesses.moov.apis.DistanceHelper.distance;


public class VelibApiService implements ApiService {

    private static final String API_KEY = "c9cf755bab39d7f2bcf8adc5dc83927ee5ddc9e6";
    private static final String BASE_URL = "https://api.jcdecaux.com/vls/v1/stations/";

    private static final Logger logger = Logger.getLogger(VelibApiService.class);

    private Map<Long, ApiVelibStationModel> velibStations = Maps.newHashMap();

    public VelibApiService() {
        try (InputStream is = VelibApiService.class.getResourceAsStream("VelibStation.json")) {
            Type listType = new TypeToken<ArrayList<ApiVelibStationModel>>() {
                // do nothing here.
            }.getType();

            List<ApiVelibStationModel> velibList = new Gson().fromJson(new InputStreamReader(is), listType);
            for (ApiVelibStationModel velibModel : velibList) {
                velibStations.put(velibModel.getNumber(), velibModel);
            }

        } catch (IOException e) {
            logger.error("Velibs : I/O error");
        }

        logger.info("Velibs loaded : " + velibStations.size() + " stations");
    }

    private VelibStation convertToTransport(ApiVelibStationModel velib) {
        return new VelibStation(ServiceType.VELIB, StationType.VELIB, new Coordinates(
                velib.getLatitude(), velib.getLongitude()),
                String.valueOf(velib.getNumber()),
                null,//lineNumber not used
                velib.getName().split(" - ")[1],
                velib.getStatus(),
                velib.getBikeStands(),
                velib.getAvailableBikeStands(),
                velib.getAvailableBikes(),
                velib.getLastUpdate());
    }

    public List<Station> getAllVelibStations() {
        List<Station> allVelibs = Lists.newArrayList();
        for (ApiVelibStationModel velib : velibStations.values()) {
            allVelibs.add(convertToTransport(velib));
        }
        return allVelibs;
    }

	public List<Station> getVelibStationsArround(Double latitude,
                                                 Double longitude, double distanceMax) {
		List<Station> closestVelibStations = Lists.newArrayList();
		for (ApiVelibStationModel velib : velibStations.values()) {
			double distanceFromPoint = distance(latitude, longitude,
					velib.getLatitude(), velib.getLongitude());
			if (distanceFromPoint <= distanceMax) {
				closestVelibStations.add(convertToTransport(velib)
						.withDistance(distanceFromPoint));
			}
		}
		return closestVelibStations;
	}

	private boolean isStationActif(long stationNumber) {
		try (InputStream is = buildUrl(String.valueOf(stationNumber)).openStream()) {
			ApiVelibStationModel station = new Gson().fromJson(
					new InputStreamReader(is), ApiVelibStationModel.class);
            logger.info("station velibs isOpen");
			return (station.getStatus().equals(StationStatus.OPEN) && station
					.getAvailableBikes() > 0);
		} catch (IOException e) {
			logger.error("Velib : I/O error in isStationActif method");
		}
		return false;
	}


    private ApiVelibStationModel queryForRealTimeData(String stationNumber) {
        ApiVelibStationModel station = null;
        try (InputStream is = buildUrl(stationNumber).openStream()) {
            station = new Gson().fromJson(new InputStreamReader(is), ApiVelibStationModel.class);
        } catch (IOException e) {
            logger.error("Velib : I/O error real time Data unavailable for velib" + stationNumber);
        }
        return station ;
    }

    public DetailStation getStation(String stationNumber) {
        ApiVelibStationModel velibStation = velibStations.get(Long.valueOf(stationNumber));
        if(velibStation == null){
            throw new NotFoundException("no velib station found : " + stationNumber);
        }
        VelibStation station = convertToTransport(velibStation);

        ApiVelibStationModel stationModel = queryForRealTimeData(station.getStationId());
        String address = stationModel.getAddress();
        int nbStands = stationModel.getAvailableBikeStands();
        int nbVehicles = stationModel.getAvailableBikes();
        return new DetailStation(address, nbStands, nbVehicles, stationModel.getStatus(), stationModel.getLastUpdate());

    }

    private URL buildUrl(String stationNumber) {
        String url = new StringBuilder(BASE_URL)
                .append(stationNumber).append("?contract=Paris&apiKey=")
                .append(API_KEY).toString();
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            logger.error("bad url");
        }
        return null;
    }

}
