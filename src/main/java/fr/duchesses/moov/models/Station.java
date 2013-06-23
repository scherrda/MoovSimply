package fr.duchesses.moov.models;

import com.rits.cloning.Cloner;
import lombok.Data;

@Data
public class Station {
    private StationType type;
    private Coordinates coordinates;
    private String lineNumber;
    private String name;
    private String address;
    private double distance;


    public Station(StationType type, Coordinates coordinates, String lineNumber, String name) {
        this.type = type;
        this.coordinates = coordinates;
        this.lineNumber = lineNumber;
        this.name = name;
    }

    public Station withDistance(double distance) {
        Station transportWithDistance = new Cloner().deepClone(this);
        transportWithDistance.setDistance(distance);
        return transportWithDistance;
    }
}
