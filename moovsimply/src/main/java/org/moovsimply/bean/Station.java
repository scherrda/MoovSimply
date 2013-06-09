package org.moovsimply.bean;

public class Station {

	private Location location;
	private String nom;
	private TypeStationEnum typeStationEnum;
	
	

	
	public Station(String nom) {
		super();
		this.nom = nom;
	}

	public Station() {
		super();
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public TypeStationEnum getTypeStationEnum() {
		return typeStationEnum;
	}

	public void setTypeStationEnum(TypeStationEnum typeStationEnum) {
		this.typeStationEnum = typeStationEnum;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
