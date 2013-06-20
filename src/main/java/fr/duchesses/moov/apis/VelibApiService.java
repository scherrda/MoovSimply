package fr.duchesses.moov.apis;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Transport;
import fr.duchesses.moov.models.TransportType;
import fr.duchesses.moov.models.velib.VelibStationModel;
import fr.duchesses.moov.models.velib.VelibStatus;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static fr.duchesses.moov.apis.DistanceHelper.distance;

@Component
public class VelibApiService implements ApiService {

	private static final String API_KEY = "c9cf755bab39d7f2bcf8adc5dc83927ee5ddc9e6";

	private static final Logger logger = Logger
			.getLogger(VelibApiService.class);

	private List<VelibStationModel> velibStations = Lists.newArrayList();

	public VelibApiService() {
		try (InputStream is = VelibApiService.class
				.getResourceAsStream("VelibStation.json")) {
			Type listType = new TypeToken<ArrayList<VelibStationModel>>() {
				// do nothing here.
			}.getType();
			velibStations = new Gson().fromJson(new InputStreamReader(is),
					listType);
		} catch (IOException e) {
			logger.error("Velib : I/O error");
		}

		logger.info("Velibs loaded");
	}

	private Transport convertToTransport(VelibStationModel velib) {
		return new Transport(TransportType.VELIB, new Coordinates(
				velib.getLatitude(), velib.getLongitude()), null, velib
				.getName().split(" - ")[1]);
	}

	public List<Transport> getAllVelibStations() {
		List<Transport> allVelibs = Lists.newArrayList();
		for (VelibStationModel velib : velibStations) {
			allVelibs.add(convertToTransport(velib));
		}
		return allVelibs;
	}

	public List<Transport> getVelibStationsForCoordinates(Double latitude,
			Double longitude, double distanceMax) {
		List<Transport> closestVelibStations = Lists.newArrayList();
		for (VelibStationModel velib : velibStations) {
			double distanceFromPoint = distance(latitude, longitude,
					velib.getLatitude(), velib.getLongitude());
			if ((distanceFromPoint < distanceMax)
					&& isStationActif(velib.getNumber())) {
				closestVelibStations.add(convertToTransport(velib)
						.withDistance(distanceFromPoint));
			}
		}
		return closestVelibStations;
	}

	private boolean isStationActif(long stationNumber) {

		StringBuilder url = new StringBuilder(
				"https://api.jcdecaux.com/vls/v1/stations/")
				.append(stationNumber).append("?contract=Paris&apiKey=")
				.append(API_KEY);
		try (InputStream is = new URL(url.toString()).openStream()) {
			VelibStationModel station = new Gson().fromJson(
					new InputStreamReader(is), VelibStationModel.class);
			return (station.getStatus().equals(VelibStatus.OPEN) && station
					.getAvailableBikes() > 0);
		} catch (IOException e) {
			logger.error("Velib : I/O error in isStationActif method");
		}
		return false;
	}
}
