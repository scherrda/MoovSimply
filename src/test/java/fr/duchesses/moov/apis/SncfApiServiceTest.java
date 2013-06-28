package fr.duchesses.moov.apis;

import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Station;
import fr.duchesses.moov.models.StationType;
import fr.duchesses.moov.models.sncf.SncfStation;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;

import static org.fest.assertions.Assertions.assertThat;

public class SncfApiServiceTest {

    private static SncfApiService service;

    @BeforeClass
    public static void setUp() {
        FileReader fileReader = new FileReader();
        service = new SncfApiService(fileReader);
    }

    @Test
    public void shouldGetAllStops() {
        Collection<Station> results = service.getAllStops();
        assertThat(results).hasSize(479);
        assertThat(results).onProperty("type").containsOnly(StationType.TRAIN, StationType.RER, StationType.TRAM, StationType.BUS);
        assertThat(results).onProperty("lineNumber").containsOnly("A", "B", "C", "D", "E", "H", "J", "K", "L", "N", "P", "R", "U", "T4", "TER");
    }

    @Test
    public void shouldGetStopsAround() {
        Coordinates oneStopCoords = new Coordinates(48.70121464, 2.09795955);

        Collection<Station> results = service.getStopsAround(oneStopCoords.getLatitude(), oneStopCoords.getLongitude(), 0);
        assertThat(results).hasSize(1);
        Station oneStation = (Station) results.toArray()[0];
        assertThat(oneStation.getType()).isEqualTo(StationType.RER);
        assertThat(oneStation.getLineNumber()).isEqualTo("B");
        assertThat(oneStation.getName()).isEqualTo("COURCELLE SUR YVETTE");
        assertThat(oneStation.getDistance()).isEqualTo(0);
    }

    @Test
    public void shouldReturnNullIfNoStationDetails() {
        SncfStation station = service.getStation("StopPoint:DUA8711385");
        assertThat(station).isNull();
    }

    @Test
    public void shouldGetStationDetails() {
        SncfStation station = service.getStation("DUA8711385");
        assertThat(station).isNotNull();
        assertThat(station.getTimes()).isNotEmpty();

        DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm");
        LocalTime now = new LocalTime();
        assertThat(TIME_FORMATTER.parseLocalTime(station.getTimes().get(0)).isAfter(now));
    }
}
