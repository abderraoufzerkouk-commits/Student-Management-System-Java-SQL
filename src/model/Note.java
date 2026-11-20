package model;

public class Note {
    private int id;
    private double valeur;
    private String type;
    private String session;
    private Etudiants idEtudiant;
    private Modules idModule;

    public Note() {}
    
    public Note(double valeur, String type, String session, Etudiants idEtudiant, Modules idModule) {
		this.valeur = valeur;
		this.type = type;
		this.session = session;
		this.idEtudiant = idEtudiant;
		this.idModule = idModule;
	}
    
    public Note(int id, double valeur, String type, String session, Etudiants idEtudiant, Modules idModule) {
		this.id = id;
		this.valeur = valeur;
		this.type = type;
		this.session = session;
		this.idEtudiant = idEtudiant;
		this.idModule = idModule;
	}

    public int getId() {
    	return id;
    }
    public void setId(int id) {
    	this.id = id; 
    }
    public double getValeur() {
    	return valeur; 
    }
    public void setValeur(double valeur) {
    	this.valeur = valeur; 
    }
    public String getType() {
    	return type; 
    }
    public void setType(String type) {
    	this.type = type; 
    }
    public String getSession() {
    	return session; 
    }
    public void setSession(String session) {
    	this.session = session; 
    }
    public Etudiants getIdEtudiant() {
    	return idEtudiant; 
    }
    public void setIdEtudiant(Etudiants idEtudiant) {
    	this.idEtudiant = idEtudiant; 
    }
    public Modules getIdModule() {
    	return idModule; 
    }
    public void setIdModule(Modules idModule) {
    	this.idModule = idModule; 
    }
}