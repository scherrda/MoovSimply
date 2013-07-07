package fr.duchesses.moov.models.sncf;

import lombok.Data;

@Data
public class ApiSncfRoute {

    private static final int INDEX_ROUTE_ID = 0;
    private static final int INDEX_ROUTE_LONG_NAME = 3;

    private String routeId;
    private String routeLongName;

    public ApiSncfRoute(String[] rawRoute) {
        this.routeId = rawRoute[INDEX_ROUTE_ID];
        this.routeLongName = rawRoute[INDEX_ROUTE_LONG_NAME];
    }
}
