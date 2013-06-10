package fr.duchesses.moov.models;

public enum TransportType {
    BUS("Bus"), METRO("Métro"), TRAIN("Train"), VELIB("Velib"), AUTOLIB("Autolib"), RER("RER"), TRAM("Tramway");

    private String name;
    TransportType(String name) {
        this.name = name;
    }
}
