package fr.duchesses.moov.models.sncf;

import lombok.Data;

@Data
public class ApiSncfTrip {

    private static final int INDEX_ROUTE_ID = 0;
    private static final int INDEX_TRIP_ID = 2;
    private static final int INDEX_TRIP_HEADSIGN = 3;

    private String routeId;
    private String tripId;
    private String headSign;

    public ApiSncfTrip(String[] rawTrip) {
        this.routeId = rawTrip[INDEX_ROUTE_ID];
        this.tripId = rawTrip[INDEX_TRIP_ID];
        this.headSign = rawTrip[INDEX_TRIP_HEADSIGN];
    }
}
