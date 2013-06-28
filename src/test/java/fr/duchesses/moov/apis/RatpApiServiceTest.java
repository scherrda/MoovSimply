package fr.duchesses.moov.apis;

import fr.duchesses.moov.models.Station;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;

import static org.fest.assertions.Assertions.assertThat;

public class RatpApiServiceTest {

    private static RatpApiService service;

    @BeforeClass
    public static void setUp() {
        service = new RatpApiService();
    }

    @Test
    public void shouldReturnAllStops() throws Exception {
        Collection<Station> results = service.getAllStops();
        assertThat(results).hasSize(16858);
    }
}
