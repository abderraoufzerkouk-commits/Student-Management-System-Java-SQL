package model;

import java.time.LocalDate;

public class Inscription {
	private Etudiants etudiant;
	private Modules module;
	private LocalDate dateInscription;
	
	public Inscription () {}

	public Inscription(Etudiants etudiant, Modules module, LocalDate dateInscription) {
		super();
		this.etudiant = etudiant;
		this.module = module;
		this.dateInscription = dateInscription;
	}
	
	public Etudiants getEtudiant() {
		return etudiant;
	}
	public void setEtudiant(Etudiants etudiant) {
		this.etudiant = etudiant;
	}
	public Modules getModule() {
		return module;
	}
	public void setModule(Modules module) {
		this.module = module;
	}
	public LocalDate getDateInscription() {
		return dateInscription;
	}
	public void setDateInscription(LocalDate dateInscription) {
		this.dateInscription = dateInscription;
	}

	@Override
	public String toString() {
		return "Inscription [etudiant=" + etudiant + ", module=" + module + ", dateInscription=" + dateInscription
				+ "]";
	}
	
}
