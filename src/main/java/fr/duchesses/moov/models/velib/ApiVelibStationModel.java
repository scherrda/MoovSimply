package fr.duchesses.moov.models.velib;

import fr.duchesses.moov.models.StationStatus;
import lombok.Data;

import com.google.gson.annotations.SerializedName;

@Data
public class ApiVelibStationModel {

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

     private StationStatus status;
    
     @SerializedName("bike_stands")
     private int bikeStands;
    
     @SerializedName("available_bike_stands")
     private int availableBikeStands;
    
     @SerializedName("available_bikes")
     private int availableBikes;
    
     @SerializedName("last_update")
     private long lastUpdate;

    public ApiVelibStationModel() {
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

    public StationStatus getStatus() {
        return status;
    }

    public int getBikeStands() {
        return bikeStands;
    }

    public int getAvailableBikeStands() {
        return availableBikeStands;
    }

    public int getAvailableBikes() {
        return availableBikes;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }
}
