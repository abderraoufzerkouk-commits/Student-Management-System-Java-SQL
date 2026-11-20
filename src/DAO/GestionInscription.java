package DAO;

import model.Modules;
import model.Etudiants;
import model.Inscription;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Connection.DatabaseConnection;

import java.time.LocalDate;

public class GestionInscription {

	private Connection conn() throws SQLException {
	    return DatabaseConnection.getConnection();
	}
	
	public void inscrire(int idEtudiant, int idModule, LocalDate date) throws SQLException{
		String sql = "INSERT INTO Inscription(id_etudiant, id_module, date_inscription) VALUES (?,?,?)";
		try(PreparedStatement ps = conn().prepareStatement(sql)){
			ps.setInt(1, idEtudiant);
            ps.setInt(2, idModule);
            ps.setDate(3, Date.valueOf(date));
            ps.executeUpdate();
		}
	}
	
	public void desinscrire(int idEtudiant, int idModule) throws SQLException {
        String sql = "DELETE FROM Inscription WHERE id_etudiant=? AND id_module=?";
        try(PreparedStatement ps = conn().prepareStatement(sql)){
        	ps.setInt(1, idEtudiant);
            ps.setInt(2, idModule);
            ps.executeUpdate();
            }
	}
	
	 public boolean estInscrit(int idEtudiant, int idModule) throws SQLException {
	        String sql = "SELECT 1 FROM Inscription WHERE id_etudiant=? AND id_module=?";
	        try (PreparedStatement stmt = conn().prepareStatement(sql)) {
	            stmt.setInt(1, idEtudiant);
	            stmt.setInt(2, idModule);
	            ResultSet rs = stmt.executeQuery();
	            return rs.next();
	        }
	 }
	 
	 public List<Integer> getModulesByEtudiant(int idEtudiant) throws SQLException {
	        List<Integer> modules = new ArrayList<>();
	        String sql = "SELECT id_module FROM Inscription WHERE id_etudiant=?";
	        try (PreparedStatement stmt = conn().prepareStatement(sql)) {
	            stmt.setInt(1, idEtudiant);
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) modules.add(rs.getInt("id_module"));
	        }
	        return modules;
	 }
	 
	 public List<Integer> getEtudiantsByModule(int idModule) throws SQLException {
	        List<Integer> etudiants = new ArrayList<>();
	        String sql = "SELECT id_etudiant FROM Inscription WHERE id_module=?";
	        try (PreparedStatement stmt = conn().prepareStatement(sql)) {
	            stmt.setInt(1, idModule);
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) etudiants.add(rs.getInt("id_etudiant"));
	        }
	        return etudiants;
	 }
}
