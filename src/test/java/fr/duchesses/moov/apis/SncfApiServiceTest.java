package fr.duchesses.moov.apis;

import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Station;
import fr.duchesses.moov.models.StationType;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;

import static fr.duchesses.moov.apis.DistanceHelper.lambert2EtendutoWGS84;
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
        assertThat(results).hasSize(506);
        assertThat(results).onProperty("type").containsOnly(StationType.TRAIN, StationType.RER, StationType.TRAM, StationType.BUS);
        assertThat(results).onProperty("lineNumber").containsOnly("A", "B", "C", "D", "E", "H", "J", "K", "L", "N", "P", "R", "U", "T4", "TER");
    }

    @Test
    public void shouldGetStopsForCoordinates() throws Exception {
        Coordinates cdg1Coords = lambert2EtendutoWGS84(616412, 2445760);
        System.out.println(cdg1Coords.getLatitude() + " " + cdg1Coords.getLongitude());

        Collection<Station> results = service.getStopsArround(cdg1Coords.getLatitude(), cdg1Coords.getLongitude(), 0);
        assertThat(results).hasSize(1);
        Station oneStation = (Station) results.toArray()[0];
        assertThat(oneStation.getType()).isEqualTo(StationType.RER);
        assertThat(oneStation.getLineNumber()).isEqualTo("B");
        assertThat(oneStation.getName()).isEqualTo("Aéroport Charles de Gaulle 1");
        assertThat(oneStation.getDistance()).isEqualTo(0);
    }
}
