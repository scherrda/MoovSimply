package fr.duchesses.moov.apis;

import static org.fest.assertions.Assertions.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class VelibApiServiceTest {

    private VelibApiService service;

    @Before
    public void setUp() {
        service = new VelibApiService();
    }

    @Test
    public void shouldGetAllVelibStationsLocatedInParis() {
        assertThat(service.getAllVelibStations()).hasSize(1225);
    }
    
}
