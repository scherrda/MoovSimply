package fr.duchesses.moov.apis;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.spi.Message;

import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Direction;
import fr.duchesses.moov.models.MetroDirection;
import fr.duchesses.moov.models.ServiceType;
import fr.duchesses.moov.models.Station;
import fr.duchesses.moov.models.StationType;
import fr.duchesses.moov.models.ratp.RatpLineModel;
import fr.duchesses.moov.models.ratp.RatpStopModel;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import javax.inject.Inject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static fr.duchesses.moov.apis.DistanceHelper.distance;


public class RatpApiService implements ApiService {

    private static final Logger logger = Logger.getLogger(RatpApiService.class);
  
	private static final String URL_METRO_REALTME = "http://www.ratp.fr/horaires/fr/ratp/metro/prochains_passages/PP/";

    private Map<String, Station> allStations = Maps.newHashMap();

    @Inject
    public RatpApiService() {
        // File reading
        List<String[]> rawStops = FileReader.getLines("ratp/ratp_arret_graphique.csv", Charsets.UTF_8, '#', 0);
        List<String[]> rawStopLines = FileReader.getLines("ratp/ratp_arret_ligne.csv", Charsets.UTF_8, '#', 0);

        // Conversion
        List<RatpStopModel> allStops = Lists.newArrayList();
        HashMultimap<Integer, RatpLineModel> allStopLines = HashMultimap.create();

        for (String[] rawStop : rawStops) {
            allStops.add(new RatpStopModel(rawStop));
        }
        for (String[] rawLine : rawStopLines) {
            RatpLineModel stopLine = new RatpLineModel(rawLine);
            allStopLines.put(stopLine.getStopId(), stopLine);
        }

        // Data aggregration
        for (RatpStopModel stop : allStops) {
            Collection<RatpLineModel> stopLines = allStopLines.get(stop.getId());
            for (RatpLineModel lineStop : stopLines) {
                String stationId = lineStop.getNumber() + "-" + stop.getId();
                allStations.put(stationId, toStation(stationId, stop.getType().toUpperCase(), stop.getLatitude(), stop.getLongitude(), lineStop.getNumber(), stop.getName(), stop.getFormattedName()));
            }
        }
        logger.info("RATP loaded stations : " + allStations.size());
    }

    public Collection<Station> getAllStops() {
        return allStations.values();
    }


    public Collection<Station> getStopsAround(double latitude, double longitude, double distanceMax) {
        List<Station> stationsAround = Lists.newArrayList();
        for (Station station : allStations.values()) {
            double distance = distance(latitude, longitude, station.getLatitude(), station.getLongitude());
            if (distance <= distanceMax) {
                station.setDistance(distance);
                stationsAround.add(station);
            }
        }
        logger.debug("RATP near stations size: " + stationsAround.size());
        return stationsAround;
    }

    private Station toStation(String id, String type, double lat, double lng, String number, String name, String formattedName) {
        Station station = new Station(ServiceType.RATP, StationType.valueOf(type), id, new Coordinates(lat, lng), number, name, 0);
        station.setFormattedName(formattedName);
        return station;
    }

    public Station getStation(String stationId) {
        Station station = allStations.get(stationId);
        List<Direction> directions = Lists.newArrayList();
        String formattedName = station.getFormattedName();
    	String lineNumber = station.getLineNumber();
    	
		if(station.getType()==StationType.METRO && StringUtils.isNotBlank(formattedName)){
			Direction directionForAller = getStationWithRealTimeInfo(formattedName, lineNumber, MetroDirection.A);
			if(directionForAller!=null){
				directions.add(directionForAller);
			}
			Direction directionForRetour = getStationWithRealTimeInfo(formattedName, lineNumber, MetroDirection.R);
			if(directionForRetour!=null){
				directions.add(directionForRetour);
			}
        }
		station.setDirections(directions);
		return station;
    }
    
    /**
     * 
     * @param stationName: for exemple solferino or palais+royal+musee+du+louvre
     * @param lineNumber: from 1 to 14
     * @param direction: A or R
     * @return Direction and time for the next subway
     */
    public Direction getStationWithRealTimeInfo(String stationName, String lineNumber, MetroDirection sens){
    	Document document = null;    	
    	try {
			document = Jsoup.connect(URL_METRO_REALTME+stationName+"/"+lineNumber+"/"+sens).get();
		} catch (IOException e) {
			logger.debug("Exeception for "+stationName +"with line number "+lineNumber+":" + e.getMessage());
			return null;
		}
    	
    	if(StringUtils.isNotBlank(document.getElementsByClass("errors").text())){
    		logger.debug("There are errors getting real time information for station "+stationName + " lineNumber "+lineNumber+". Must be terminus.");
    		return null;
    	}
    	    
    	Direction direction = new Direction();
    	direction.setDirection(document.getElementsByClass("direction").text());
    	direction.setTime(document.select("table #prochains_passages table tbody tr td").get(1).text());
		logger.debug("realtime result for station "+stationName + " lineNumber "+ lineNumber+":"+direction);
		return direction;
	 }
    
}
