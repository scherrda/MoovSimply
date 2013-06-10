package fr.duchesses.moov.models;

public class Transport {
    TransportType type;
    int lineNumber;
    Coordinates coordinates;

    public Transport(TransportType type, Coordinates coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public TransportType getType() {
        return type;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}
