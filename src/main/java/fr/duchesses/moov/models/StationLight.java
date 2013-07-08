package fr.duchesses.moov.models;

public class StationLight {
    ServiceType serT;
    StationType type;
    String id;
    String num;
    String name;
    private double lat;
    private double lng;


    public StationLight(ServiceType serT, StationType stT, String id, String num, String name, Coordinates coordinates) {
        this.serT = serT;
        this.type = stT;
        this.id = id;
        this.num = num;
        this.name = name;
        this.lat = coordinates.getLatitude();
        this.lng = coordinates.getLongitude();
    }

    public ServiceType getSerT() {
        return serT;
    }

    public StationType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getNum() {
        return num;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
