package model;

public class Enseignants {
	
	private int id;
	private String nom;
	private String specialite;
	
	public Enseignants() {}

	public Enseignants(String nom, String specialite) {
		this.nom = nom;
		this.specialite = specialite;
	}
	

	public Enseignants(int id, String nom, String specialite) {
		this.id = id;
		this.nom = nom;
		this.specialite = specialite;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getSpecialite() {
		return specialite;
	}
	public void setSpecialite(String specialite) {
		this.specialite = specialite;
	}
	@Override
	public String toString() {
		return "Enseignants [nom=" + nom + ", specialite=" + specialite + "]";
	}

}
