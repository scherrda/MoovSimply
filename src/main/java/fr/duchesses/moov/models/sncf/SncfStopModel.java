package fr.duchesses.moov.models.sncf;

import lombok.Data;

@Data
public class SncfStopModel {

    private static final int INDEX_ID = 0;
    private static final int INDEX_NAME = 1;
    private static final int INDEX_DESCRIPTION = 2;
    private static final int INDEX_LATITUDE = 3;
    private static final int INDEX_LONGITUDE = 4;
    private static final int INDEX_ZONE_ID = 5;
    private static final int INDEX_URL = 6;
    private static final int INDEX_LOCATION_TYPE = 7;
    private static final int INDEX_PARENT_STATION = 8;

    private String stopId;
    private String name;
    private double latitude;
    private double longitude;

    public SncfStopModel(String[] rawStop) {
        stopId = rawStop[INDEX_ID];
        name = rawStop[INDEX_NAME];
        latitude = Double.parseDouble(rawStop[INDEX_LATITUDE]);
        longitude = Double.parseDouble(rawStop[INDEX_LONGITUDE]);
    }

    public String getName() {
        return name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }
}
