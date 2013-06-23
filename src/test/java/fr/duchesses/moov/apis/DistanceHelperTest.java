package fr.duchesses.moov.apis;

import fr.duchesses.moov.models.Coordinates;
import org.junit.Test;

import static fr.duchesses.moov.apis.DistanceHelper.distance;
import static fr.duchesses.moov.apis.DistanceHelper.lambert2EtendutoWGS84;
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

    @Test
    public void shouldTransformLambert2CoordinatesToWGS84() {
        // Coordonnées de : AVENUE DU PRESIDENT KENNEDY - Maison de Radio France
        Coordinates wgs84 = new Coordinates(48.956973, 2.504412);
        int lambert2X = 595874;
        int lambert2Y = 2428306;

        Coordinates result = lambert2EtendutoWGS84(lambert2X, lambert2Y);
        //assertThat(result.getLatitude()).isGreaterThan(wgs84.getLatitude() - 0.001).isLessThan(wgs84.getLatitude() + 0.001);
        //assertThat(result.getLongitude()).isGreaterThan(wgs84.getLongitude() - 0.001).isLessThan(wgs84.getLongitude() + 0.001);
    }
}
