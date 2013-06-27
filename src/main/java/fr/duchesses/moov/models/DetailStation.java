package fr.duchesses.moov.models;


public class DetailStation {
    private final String address;
    private final int availableStands;
    private final int availableVehicles;
    private final StationStatus status;
    private long lastUpdate;

    public DetailStation(String address, int availableStands, int available_vehicles, StationStatus status, long lastUpdate) {
        this.address = address;
        this.availableStands = availableStands;
        this.availableVehicles = available_vehicles;
        this.status = status;
        this.lastUpdate = lastUpdate;
    }

    public String getAddress() {
        return address;
    }

    public int getAvailableStands() {
        return availableStands;
    }

    public int getAvailableVehicles() {
        return availableVehicles;
    }

    public StationStatus getStatus() {
        return status;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }
}
