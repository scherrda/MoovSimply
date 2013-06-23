package fr.duchesses.moov.models.velib;

import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Station;
import fr.duchesses.moov.models.StationType;
import lombok.Data;

@Data
public class VelibStation extends Station {

    private VelibStatus status;
    private int bikeStands;
    private int availableBikeStands;
    private int availableBikes;
    private long lastUpdate;

    public VelibStation(StationType type, Coordinates coordinates, String lineNumber, String name, VelibStatus status, int bikeStands, int availableBikeStands, int availableBikes, long lastUpdate) {
        super(type, coordinates, lineNumber, name);
        this.status = status;
        this.bikeStands = bikeStands;
        this.availableBikeStands = availableBikeStands;
        this.availableBikes = availableBikes;
        this.lastUpdate = lastUpdate;
    }
}
