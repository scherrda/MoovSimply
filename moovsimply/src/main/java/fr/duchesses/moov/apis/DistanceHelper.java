package fr.duchesses.moov.apis;

public class DistanceHelper {

    public static double distance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double radianLatitude1 = Math.PI * latitude1 / 180;
        double radianLatitude2 = Math.PI * latitude2 / 180;
        double theta = longitude1 - longitude2;
        double radianTheta = Math.PI * theta / 180;
        double dist = Math.sin(radianLatitude1) * Math.sin(radianLatitude2) + Math.cos(radianLatitude1) * Math.cos(radianLatitude2)
                * Math.cos(radianTheta);
        dist = Math.acos(dist);
        dist = dist * 180 / Math.PI;
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344; // in km
        return dist;
    }
}
