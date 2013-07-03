package fr.duchesses.moov.models.velib;

import fr.duchesses.moov.models.*;
import lombok.Data;

@Data
public class VelibStation extends Station {

    private StationStatus status;
    private int bikeStands;
    private int availableBikeStands;
    private int availableBikes;
    private long lastUpdate;

    public VelibStation(ServiceType serviceType, StationType type, Coordinates coordinates, String id, String lineNumber, String name, StationStatus status, int bikeStands, int availableBikeStands, int availableBikes, long lastUpdate) {
        super(serviceType, type, id, coordinates, lineNumber, name,0);//TODO lineMumber not in base class Station
        this.status = status;
        this.bikeStands = bikeStands;
        this.availableBikeStands = availableBikeStands;
        this.availableBikes = availableBikes;
        this.lastUpdate = lastUpdate;
    }
}
