package fr.duchesses.moov.models.ratp;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import lombok.Data;

@Data
public class RatpStopModel {

    private static final int STOP_INDEX_ID = 0;
    private static final int STOP_INDEX_LATITUDE = 2;
    private static final int STOP_INDEX_LONGITUDE = 1;
    private static final int STOP_INDEX_NAME = 3;
    private static final int STOP_INDEX_DISTRICT = 4;
    private static final int STOP_INDEX_TYPE = 5;
    private static final int STOP_INDEX_FORMATTED_NAME = 6;

    private int id;
    private Double latitude;
    private Double longitude;
    private String name;
    private String district;
    private String type;
    private String formattedName;

    public RatpStopModel(String[] rawStop) {
        id = Ints.tryParse(rawStop[STOP_INDEX_ID]);
        latitude = Doubles.tryParse(rawStop[STOP_INDEX_LATITUDE]);
        longitude = Doubles.tryParse(rawStop[STOP_INDEX_LONGITUDE]);
        name = rawStop[STOP_INDEX_NAME];
        district = rawStop[STOP_INDEX_DISTRICT];
        type = rawStop[STOP_INDEX_TYPE];
        formattedName = rawStop[STOP_INDEX_FORMATTED_NAME];
    }

    public static int getStopIndexId() {
        return STOP_INDEX_ID;
    }

    public static int getStopIndexLatitude() {
        return STOP_INDEX_LATITUDE;
    }

    public static int getStopIndexLongitude() {
        return STOP_INDEX_LONGITUDE;
    }

    public static int getStopIndexName() {
        return STOP_INDEX_NAME;
    }

    public static int getStopIndexDistrict() {
        return STOP_INDEX_DISTRICT;
    }

    public static int getStopIndexType() {
        return STOP_INDEX_TYPE;
    }

}
