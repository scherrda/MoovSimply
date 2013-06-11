package fr.duchesses.moov.models;

public class Transport {
    TransportType type;
    int lineNumber;
    String lineName;
    Coordinates coordinates;

    public Transport(TransportType type, Coordinates coordinates, int lineNumber, String lineName) {
        this.type = type;
        this.coordinates = coordinates;
        this.lineNumber=lineNumber;
        this.lineName = lineName;
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

	public String getLineName() {
		return lineName;
	}
    
}
