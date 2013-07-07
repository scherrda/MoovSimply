package fr.duchesses.moov.models.sncf;

import fr.duchesses.moov.models.Direction;
import fr.duchesses.moov.models.Station;
import lombok.Data;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Collections;

@Data
public class SncfStation extends Station {

    private static final int NB_NEXT_TIMES = 5;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm");

    public SncfStation(Station station) {
        super(station.getServiceType(), station.getType(), station.getStationId(), station.getCoordinates(), station.getLineNumber(), station.getName(), station.getCodeUIC());
    }

    public void addTime(String routeLongName, LocalTime time) {
        directions.add(new Direction(routeLongName, TIME_FORMATTER.print(time)));
    }

    public void sortAndTruncateNextStopTimes() {
        String now = TIME_FORMATTER.print(new LocalTime());
        Direction nowDirection = new Direction("Fake", now);
        directions.add(nowDirection);
        Collections.sort(directions);

        int nextTimeIndex = directions.indexOf(nowDirection) + 1;
        int lastTimeIndex = directions.size();
        directions = directions.subList(nextTimeIndex, Math.min(nextTimeIndex + NB_NEXT_TIMES, lastTimeIndex));
    }
}
