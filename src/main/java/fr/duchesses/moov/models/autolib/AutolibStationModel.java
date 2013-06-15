package fr.duchesses.moov.models.autolib;

import lombok.Data;

@Data
public class AutolibStationModel {

    private String emplacement;
    private int bornes_de_charge_pour_vehicule_tiers;
    private String code_postal;
    private int bornes_de_charge_autolib;
    private String ville;
    private int nombre_total_de_bornes_de_charge;
    private String identifiant_autolib;
    private String rue;
    private String identifiant_dsp;
    private double[] field13;
    private String etat_actuel;
    private String type_de_station;

    public double getLatitude() {
        return field13[0];
    }

    public double getLongitude() {
        return field13[1];
    }

}
