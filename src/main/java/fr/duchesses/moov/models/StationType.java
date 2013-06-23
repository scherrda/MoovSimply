package fr.duchesses.moov.models;

public enum StationType {
    BUS("Bus"), METRO("Métro"), TRAIN("Train"), VELIB("Velib"), AUTOLIB("Autolib"), RER("RER"), TRAM("Tramway");

    private String name;
    
    StationType(String name) {
        this.name = name;
    }
    
    public String getName(){
    	return this.name;
    }
}
