package fr.duchesses.moov.apis;

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
}
