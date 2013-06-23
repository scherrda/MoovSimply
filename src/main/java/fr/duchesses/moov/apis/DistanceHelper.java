package fr.duchesses.moov.apis;

import fr.duchesses.moov.models.Coordinates;

public class DistanceHelper {

    private static double R = 6371; // Radius of the earth in km

    public static double distance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double dLat = Math.toRadians(latitude2 - latitude1);
        double dLon = Math.toRadians(longitude2 - longitude1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double angle = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * angle; // Distance in km
        return d * 1000; // Distance in m
    }

    public static Coordinates lambert2EtendutoWGS84(double lambertX, double lambertY) {
        double Cm = 11745793.393435;
        double n = 0.728968627421412;
        double XSm = 600000;
        double YSm = 8199695.76800186;
        double a = 6378249.2000;
        double f1 = 6356515.0000;
        double X = lambertX - XSm;
        double Y = lambertY - YSm;

        // X conversion
        double longitude = Math.atan(-(X) / (Y));
        longitude = longitude / n;
        longitude = longitude * 180 / Math.PI;
        double constante = 2 + (20 / 60) + (14.025 / 3600);
        longitude = longitude + constante;

        // Y conversion
        double latitude = Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2));
        double f = (a - f1) / a;
        double e2 = 2 * f - Math.pow(f, 2);
        double e = Math.sqrt(e2);
        double Latiso = Math.log(Cm / latitude) / n;
        latitude = Math.tanh(Latiso);
        for (int i = 0; i < 6; i++) {
            latitude = Math.tanh(Latiso + e * atanh(e * latitude));
        }
        latitude = Math.asin(latitude);
        latitude = latitude / Math.PI;
        latitude = latitude * 180;

        return new Coordinates(latitude, longitude);
    }

    private static double atanh(double x) {
        return Math.log((1 + x) / (1 - x)) / 2;
    }
}
