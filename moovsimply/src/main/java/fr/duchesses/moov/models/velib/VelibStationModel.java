package fr.duchesses.moov.models.velib;

import com.google.gson.annotations.SerializedName;

public class VelibStationModel {

    @SerializedName("number")
    private long number;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    // private VelibStatus status;
    //
    // @SerializedName("bike_stands")
    // private int bikeStands;
    //
    // @SerializedName("available_bike_stands")
    // private int availableBikeStands;
    //
    // @SerializedName("available_bikes")
    // private int availableBikes;
    //
    // @SerializedName("last_update")
    // private Date lastUpdate;

    public VelibStationModel() {
        // Default constructor
    }

    public long getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    // public VelibStatus getStatus() {
    // return status;
    // }
    //
    // public int getBikeStands() {
    // return bikeStands;
    // }
    //
    // public int getAvailableBikeStands() {
    // return availableBikeStands;
    // }
    //
    // public int getAvailableBikes() {
    // return availableBikes;
    // }
    //
    // public Date getLastUpdate() {
    // return lastUpdate;
    // }

    public void setNumber(long number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // public void setStatus(VelibStatus status) {
    // this.status = status;
    // }
    //
    // public void setBikeStands(int bikeStands) {
    // this.bikeStands = bikeStands;
    // }
    //
    // public void setAvailableBikeStands(int availableBikeStands) {
    // this.availableBikeStands = availableBikeStands;
    // }
    //
    // public void setAvailableBikes(int availableBikes) {
    // this.availableBikes = availableBikes;
    // }
    //
    // public void setLastUpdate(Date lastUpdate) {
    // this.lastUpdate = lastUpdate;
    // }

    @Override
    public String toString() {
        return "VelibStationModel [number=" + number + ", name=" + name + ", address=" + address + ", latitude=" + latitude
                + ", longitude=" + longitude + "]";
    }

}
