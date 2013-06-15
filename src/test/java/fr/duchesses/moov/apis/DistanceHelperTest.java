package fr.duchesses.moov.apis;

import org.junit.Test;

import static fr.duchesses.moov.apis.DistanceHelper.distance;
import static org.fest.assertions.Assertions.assertThat;

public class DistanceHelperTest {

    @Test
    public void shouldCalculateNoDistanceBetweenSamePoint() throws Exception {
        double lat = 48.87525,
                lng = 2.31110;
        assertThat(distance(lat, lng, lat, lng)).isEqualTo(0);
    }

    @Test
    public void shouldCalculateDistanceBetweenPlaces() throws Exception {
        double lat = 48,
                lng = 2,
                lat2 = 49,
                lng2 = 2;
        assertThat(Math.round(distance(lat, lng, lat2, lng2))).isEqualTo(111195);
    }

    @Test
    public void shouldCalculateDistanceBetweenPlaces2() throws Exception {
        double lat = 48,
                lng = 2,
                lat2 = 48,
                lng2 = 3;
        assertThat(Math.round(distance(lat, lng, lat2, lng2))).isEqualTo(74403);
    }
}
