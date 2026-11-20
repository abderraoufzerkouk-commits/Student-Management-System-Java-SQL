package Service;

import model.*;

import java.sql.*;
import java.util.*;

import DAO.*;

public class Resultat {
	
	 private GestionNote noteDAO = new GestionNote();
	    private GestionModules moduleDAO = new GestionModules();
	    private GestionInscription inscriptionDAO = new GestionInscription();
	    private GestionEtudiants etudiantDAO = new GestionEtudiants(); // à adapter

	    public Double getMoyenneModule(int idEtudiant, int idModule, String session) throws SQLException {
	        Note cc = noteDAO.getNote(idEtudiant, idModule, "CC", session);
	        Note examen = noteDAO.getNote(idEtudiant, idModule, "Examen", session);
	        if (cc == null || examen == null) return null;
	        return cc.getValeur() * 0.4 + examen.getValeur() * 0.6;
	    }

	    public Double getMoyenneModuleApresRattrapage(int idEtudiant, int idModule) throws SQLException {
	        Note cc = noteDAO.getNote(idEtudiant, idModule, "CC", "Normale");
	        Note examenNormal = noteDAO.getNote(idEtudiant, idModule, "Examen", "Normale");
	        Note examenRattrapage = noteDAO.getNote(idEtudiant, idModule, "Examen", "Rattrapage");
	        if (cc == null) return null;
	        double meilleurExamen;
	        if (examenNormal == null && examenRattrapage == null) return null;
	        else if (examenNormal == null) meilleurExamen = examenRattrapage.getValeur();
	        else if (examenRattrapage == null) meilleurExamen = examenNormal.getValeur();
	        else meilleurExamen = Math.max(examenNormal.getValeur(), examenRattrapage.getValeur());
	        return cc.getValeur() * 0.4 + meilleurExamen * 0.6;
	    }

	    public Double getMoyenneGenerale(int idEtudiant, String session) throws SQLException {
	        List<Integer> modulesIds = inscriptionDAO.getModulesByEtudiant(idEtudiant);
	        double sommePonderee = 0;
	        double sommeCoefs = 0;
	        for (int idMod : modulesIds) {
	            Modules m = moduleDAO.FindById(idMod);
	            if (m == null) continue;
	            Double moyMod = getMoyenneModule(idEtudiant, idMod, session);
	            if (moyMod != null) {
	                sommePonderee += moyMod * m.getCoifficient();
	                sommeCoefs += m.getCoifficient();
	            }
	        }
	        if (sommeCoefs == 0) return null;
	        return sommePonderee / sommeCoefs;
	    }

	    public Double getMoyenneGeneraleApresRattrapage(int idEtudiant) throws SQLException {
	        List<Integer> modulesIds = inscriptionDAO.getModulesByEtudiant(idEtudiant);
	        double sommePonderee = 0;
	        double sommeCoefs = 0;
	        for (int idMod : modulesIds) {
	            Modules m = moduleDAO.FindById(idMod);
	            if (m == null) continue;
	            Double moyMod = getMoyenneModuleApresRattrapage(idEtudiant, idMod);
	            if (moyMod != null) {
	                sommePonderee += moyMod * m.getCoifficient();
	                sommeCoefs += m.getCoifficient();
	            }
	        }
	        if (sommeCoefs == 0) return null;
	        return sommePonderee / sommeCoefs;
	    }

	    public boolean isModuleValide(int idEtudiant, int idModule) throws SQLException {
	        Double moyApres = getMoyenneModuleApresRattrapage(idEtudiant, idModule);
	        return moyApres != null && moyApres >= 10;
	    }

	    public List<Integer> getEtudiantsEnRattrapage() throws SQLException {
	        List<Etudiants> tous = etudiantDAO.ListerTous();
	        List<Integer> result = new ArrayList<>();
	        for (Etudiants e : tous) {
	            List<Integer> modules = inscriptionDAO.getModulesByEtudiant(e.getId());
	            for (int idMod : modules) {
	                Double moyNormale = getMoyenneModule(e.getId(), idMod, "Normale");
	                if (moyNormale != null && moyNormale < 10) {
	                    result.add(e.getId());
	                    break;
	                }
	            }
	        }
	        return result;
	    }

	    public List<Map<String, Object>> getNotesAvantRattrapage(int idEtudiant) throws SQLException {
	        List<Map<String, Object>> result = new ArrayList<>();
	        List<Integer> modules = inscriptionDAO.getModulesByEtudiant(idEtudiant);
	        for (int idMod : modules) {
	            Modules m = moduleDAO.FindById(idMod);
	            if (m == null) continue;
	            Map<String, Object> ligne = new HashMap<>();
	            ligne.put("module", m.getNom());
	            Note cc = noteDAO.getNote(idEtudiant, idMod, "CC", "Normale");
	            Note examen = noteDAO.getNote(idEtudiant, idMod, "Examen", "Normale");
	            ligne.put("cc", cc == null ? null : cc.getValeur());
	            ligne.put("examen", examen == null ? null : examen.getValeur());
	            ligne.put("moyenne", getMoyenneModule(idEtudiant, idMod, "Normale"));
	            result.add(ligne);
	        }
	        return result;
	    }
}
