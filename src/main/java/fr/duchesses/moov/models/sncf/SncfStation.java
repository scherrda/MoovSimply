package fr.duchesses.moov.models.sncf;

import com.google.common.collect.Lists;
import fr.duchesses.moov.models.Station;
import lombok.Data;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Collections;
import java.util.List;

@Data
public class SncfStation extends Station {

    private static final int NB_NEXT_TIMES = 5;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm");

    private List<String> times = Lists.newArrayList();

    public SncfStation(Station station) {
        super(station.getServiceType(), station.getType(), station.getStationId(), station.getCoordinates(), station.getLineNumber(), station.getName());
    }

    public void addTime(LocalTime time) {
        times.add(TIME_FORMATTER.print(time));
    }

    public void sortAndTruncateNextStopTimes() {
        String now = TIME_FORMATTER.print(new LocalTime());
        times.add(now);
        Collections.sort(times);

        int nextTimeIndex = times.indexOf(now) + 1;
        int lastTimeIndex = times.size();
        times = times.subList(nextTimeIndex, Math.min(nextTimeIndex + NB_NEXT_TIMES, lastTimeIndex));
    }
}
