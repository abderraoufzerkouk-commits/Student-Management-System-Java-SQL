package model;

public class Modules {
	
	private int id;
	private String nom;
	private double coifficient;
	private int volumeHoraire;
	private Enseignants enseignant;
	
	public Modules () {}

	public Modules(String nom, double coifficient, int volumeHoraire, Enseignants enseignant) {
		this.nom = nom;
		this.coifficient = coifficient;
		this.volumeHoraire = volumeHoraire;
		this.enseignant = enseignant;
	}

	public Modules(int id, String nom, double coifficient, int volumeHoraire, Enseignants enseignant) {
		this.id = id;
		this.nom = nom;
		this.coifficient = coifficient;
		this.volumeHoraire = volumeHoraire;
		this.enseignant = enseignant;
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
	public double getCoifficient() {
		return coifficient;
	}
	public void setCoifficient(double coifficient) {
		this.coifficient = coifficient;
	}
	public int getVolumeHoraire() {
		return volumeHoraire;
	}
	public void setVolumeHoraire(int volumeHoraire) {
		this.volumeHoraire = volumeHoraire;
	}
	public Enseignants getEnseignant() {
	    return enseignant;
	}
	public void setEnseignant(Enseignants enseignant) {
		this.enseignant = enseignant;
	}

	@Override
	public String toString() {
		return "Modules [nom=" + nom + ", coifficient=" + coifficient + "]";
	}

}
