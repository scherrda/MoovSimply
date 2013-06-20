package fr.duchesses.moov.models.velib;

import lombok.Data;

import com.google.gson.annotations.SerializedName;

@Data
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

     private VelibStatus status;
    
     @SerializedName("bike_stands")
     private int bikeStands;
    
     @SerializedName("available_bike_stands")
     private int availableBikeStands;
    
     @SerializedName("available_bikes")
     private int availableBikes;
    
     @SerializedName("last_update")
     private long lastUpdate;

    public VelibStationModel() {
        // Default constructor
    }

}
