package model;

import java.time.LocalDate;

public class Etudiants {
	
	private int id;
	private String nom;
	private String prenom;
	private LocalDate dateNaissance;
	private String email;
	
	public Etudiants() {}

	public Etudiants(String nom, String prenom, LocalDate dateNaissance, String email) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.dateNaissance = dateNaissance;
		this.email = email;
	}

	public Etudiants(int id, String nom, String prenom, LocalDate dateNaissance, String email) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.dateNaissance = dateNaissance;
		this.email = email;
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
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public LocalDate getDateNaissance() {
		return dateNaissance;
	}
	public void setDateNaissance(LocalDate dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "Etudiants [id=" + id + ", nom=" + nom + ", prenom=" + prenom + "]";
	}
	
}
