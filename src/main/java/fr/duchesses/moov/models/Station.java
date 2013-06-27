package fr.duchesses.moov.models;

import com.rits.cloning.Cloner;
import lombok.Data;

@Data
public class Station {
    private ServiceType serviceType;
    private StationType type;
    private String stationId;
    private Coordinates coordinates;
    private String lineNumber;
    private String name;
    private String address;
    private double distance;

    public Station(ServiceType serviceType, StationType type, String stationId, Coordinates coordinates, String lineNumber, String name) {
        this.serviceType = serviceType;
        this.type = type;
        this.stationId = stationId;
        this.coordinates = coordinates;
        this.lineNumber = lineNumber;
        this.name = name;
    }

    public Station withDistance(double distance) {
        Station transportWithDistance = new Cloner().deepClone(this);
        transportWithDistance.setDistance(distance);
        return transportWithDistance;
    }

    public double getLatitude() {
        return coordinates.getLatitude();
    }

    public double getLongitude() {
        return coordinates.getLongitude();
    }
}
