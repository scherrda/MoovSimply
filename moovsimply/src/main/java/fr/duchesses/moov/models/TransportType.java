package fr.duchesses.moov.models;

public enum TransportType {
    BUS("Bus"), METRO("Métro"), TRAIN("Train"), VELIB("Velib"), AUTOLIB("Autolib");

    private String name;
    TransportType(String name) {
        this.name = name;
    }
}
