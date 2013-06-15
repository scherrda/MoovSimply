package fr.duchesses.moov.models;

import com.rits.cloning.Cloner;
import lombok.Data;

@Data
public class Transport {
    private TransportType type;
    private Coordinates coordinates;
    private String lineNumber;
    private String name;
    private double distance;

    public Transport(TransportType type, Coordinates coordinates, String lineNumber, String name) {
        this.type = type;
        this.coordinates = coordinates;
        this.lineNumber = lineNumber;
        this.name = name;
    }

    public Transport withDistance(double distance) {
        Transport transportWithDistance = new Cloner().deepClone(this);
        transportWithDistance.setDistance(distance);
        return transportWithDistance;
    }
}
