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

    public String getEmplacement() {
        return emplacement;
    }

    public int getBornes_de_charge_pour_vehicule_tiers() {
        return bornes_de_charge_pour_vehicule_tiers;
    }

    public String getCode_postal() {
        return code_postal;
    }

    public int getBornes_de_charge_autolib() {
        return bornes_de_charge_autolib;
    }

    public String getVille() {
        return ville;
    }

    public int getNombre_total_de_bornes_de_charge() {
        return nombre_total_de_bornes_de_charge;
    }

    public String getIdentifiant_autolib() {
        return identifiant_autolib;
    }

    public String getRue() {
        return rue;
    }

    public String getIdentifiant_dsp() {
        return identifiant_dsp;
    }

    public double[] getField13() {
        return field13;
    }

    public String getEtat_actuel() {
        return etat_actuel;
    }

    public String getType_de_station() {
        return type_de_station;
    }
}
