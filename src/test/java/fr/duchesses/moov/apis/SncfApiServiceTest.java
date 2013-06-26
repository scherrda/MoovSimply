package fr.duchesses.moov.apis;

import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Station;
import fr.duchesses.moov.models.StationType;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;

import static org.fest.assertions.Assertions.assertThat;

public class SncfApiServiceTest {

    private static SncfApiService service;

    @BeforeClass
    public static void setUp() {
        service = new SncfApiService();
    }

    @Test
    public void shouldGetAllStops() throws Exception {
        Collection<Station> results = service.getAllStops();
        assertThat(results).hasSize(483);
        assertThat(results).onProperty("type").containsOnly(StationType.TRAIN, StationType.RER, StationType.TRAM, StationType.BUS);
        assertThat(results).onProperty("lineNumber").containsOnly("A", "B", "C", "D", "E", "H", "J", "K", "L", "N", "P", "R", "U", "T4", "TER");
    }

    @Test
    public void shouldGetStopsAround() throws Exception {
        Coordinates cdg1Coords = new Coordinates(48.70071899, 2.09912281);

        Collection<Station> results = service.getStopsArround(cdg1Coords.getLatitude(), cdg1Coords.getLongitude(), 0);
        assertThat(results).hasSize(1);
        Station oneStation = (Station) results.toArray()[0];
        assertThat(oneStation.getType()).isEqualTo(StationType.RER);
        assertThat(oneStation.getLineNumber()).isEqualTo("B");
        assertThat(oneStation.getName()).isEqualTo("COURCELLE SUR YVETTE");
        assertThat(oneStation.getDistance()).isEqualTo(0);
    }
}
