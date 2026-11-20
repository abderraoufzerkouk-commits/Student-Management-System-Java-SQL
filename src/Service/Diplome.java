package Service;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import DAO.*;

public class Diplome {

	private Resultat resultatService = new Resultat();
    private GestionInscription inscriptionDAO = new GestionInscription();
    private GestionModules moduleDAO = new GestionModules();
    private GestionEtudiants etudiantDAO = new GestionEtudiants();

    public boolean estDiplome(int idEtudiant) throws SQLException {
        List<Integer> modules = inscriptionDAO.getModulesByEtudiant(idEtudiant);
        if (modules.isEmpty()) return false;
        for (int idMod : modules) {
            if (!resultatService.isModuleValide(idEtudiant, idMod)) {
                return false;
            }
        }
        return true;
    }

    public List<Integer> getDiplomes() throws SQLException {
        List<Integer> diplomes = new ArrayList<>();
        List<Etudiants> tous = etudiantDAO.ListerTous();
        for (Etudiants e : tous) {
            if (estDiplome(e.getId())) {
                diplomes.add(e.getId());
            }
        }
        return diplomes;
    }

}
