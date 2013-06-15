package fr.duchesses.moov.models;

import com.rits.cloning.Cloner;

public class Transport {
    TransportType type;
    String lineNumber;
    String lineName;
    Coordinates coordinates;
    double distance;

    public Transport(TransportType type, Coordinates coordinates, String lineNumber, String lineName) {
        this.type = type;
        this.coordinates = coordinates;
        this.lineNumber = lineNumber;
        this.lineName = lineName;
    }

    public TransportType getType() {
        return type;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public String getLineName() {
        return lineName;
    }

    public double getDistance() {
        return distance;
    }

    private void setDistance(double distance) {
        this.distance = distance;
    }

    public Transport withDistance(double distance) {
        Transport transportWithDistance = new Cloner().deepClone(this);
        transportWithDistance.setDistance(distance);
        return transportWithDistance;
    }

    @Override
    public String toString() {
        return "Transport{" +
                "type=" + type +
                ", lineNumber='" + lineNumber + '\'' +
                ", lineName='" + lineName + '\'' +
                ", coordinates=" + coordinates +
                ", distance=" + distance +
                '}';
    }
}
