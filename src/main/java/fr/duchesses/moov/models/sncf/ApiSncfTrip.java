package fr.duchesses.moov.models.sncf;

import lombok.Data;

@Data
public class ApiSncfTrip {

    private static final int INDEX_ROUTE_ID = 0;
    private static final int INDEX_TRIP_ID = 2;

    private String routeId;
    private String tripId;

    public ApiSncfTrip(String[] rawTrip) {
        this.routeId = rawTrip[INDEX_ROUTE_ID];
        this.tripId = rawTrip[INDEX_TRIP_ID];
    }
}
