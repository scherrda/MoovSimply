package fr.duchesses.moov.models.sncf;

import lombok.Data;
import org.apache.log4j.Logger;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Data
public class ApiSncfStopTime implements Comparable<ApiSncfStopTime> {

    private static final Logger logger = Logger.getLogger(ApiSncfStopTime.class);

    private static final int INDEX_TRIP_ID = 0;
    private static final int INDEX_ARRIVAL_TIME = 1;
    private static final int INDEX_DEPARTURE_TIME = 2;
    private static final int INDEX_STOP_ID = 3;
    private static final int INDEX_STOP_SEQUENCE = 4;
    private static final int INDEX_STOP_HEADSIGN = 5;
    private static final int INDEX_PICKUP_TYPE = 6;
    private static final int INDEX_DROP_OFF_TYPE = 7;
    private static final int INDEX_SHAPE_DIST_TRAVELED = 8;

    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm:ss");

    private LocalTime arrivalTime;
    private String stopId;

    public ApiSncfStopTime(String[] rawStopTime) {
        try {
            arrivalTime = fmt.parseLocalTime(rawStopTime[INDEX_ARRIVAL_TIME]);
        } catch (IllegalFieldValueException e) {
            int hour = Integer.parseInt(rawStopTime[INDEX_ARRIVAL_TIME].split(":")[0]);
            int minute = Integer.parseInt(rawStopTime[INDEX_ARRIVAL_TIME].split(":")[1]);
            arrivalTime = new LocalTime(0).withHourOfDay(hour % 24).withMinuteOfHour(minute);
        } catch (Exception e) {
            //logger.warn("Cannot parse arrival time", e);
        }
        this.stopId = rawStopTime[INDEX_STOP_ID].split(":")[1];
    }
    
    public ApiSncfStopTime(String stopTime, String stopId) {
    		String hourMinut = stopTime.split(" ")[1];
            int hour = Integer.parseInt(hourMinut.split(":")[0]);
            int minute = Integer.parseInt(hourMinut.split(":")[1]);
            arrivalTime = new LocalTime(0).withHourOfDay(hour % 24).withMinuteOfHour(minute);
        this.stopId = stopId;
    }

	@Override
	public int compareTo(ApiSncfStopTime o) {
		return arrivalTime.compareTo(o.getArrivalTime());
	}
}
