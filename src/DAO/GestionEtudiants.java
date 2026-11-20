package DAO;

import model.Etudiants;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Connection.DatabaseConnection;

public class GestionEtudiants {
	
	private Connection conn() throws SQLException {
	    return DatabaseConnection.getConnection();
	}
	
	public void addEtudiant(Etudiants e) throws SQLException{
		String sql = "INSERT INTO Etudiant (nom, prenom, date_naissance, email) VALUES (?,?,?,?)";
		try(PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
			ps.setString(1, e.getNom());
            ps.setString(2, e.getPrenom());
            ps.setDate  (3, Date.valueOf(e.getDateNaissance()));
            ps.setString(4, e.getEmail());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) e.setId(rs.getInt(1));
            }
		}
	}
	
	public void modifyEtudiant(Etudiants e) throws SQLException{
		String sql = "UPDATE Etudiant SET nom=?, prenom=?, date_naissance=?, email=? WHERE id_etudiant=?";
		try(PreparedStatement ps = conn().prepareStatement(sql)){
			ps.setString(1, e.getNom());
			ps.setString(2, e.getPrenom());
			ps.setDate(3, Date.valueOf(e.getDateNaissance()));
			ps.setString(4, e.getEmail());
			ps.setInt(5, e.getId());
			ps.executeUpdate();
		}
	}
	
	public void deleteEtudiant(int id) throws SQLException{
		String sql = "DELETE FROM Etudiant WHERE id_etudiant=?";
		try(PreparedStatement ps = conn().prepareStatement(sql)){
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}
	
	public Etudiants findById (int id) throws SQLException{
		String sql = "SELECT * FROM Etudiant WHERE id_etudiant=?";
		try(PreparedStatement ps = conn().prepareStatement(sql)){
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				Etudiants e = new Etudiants();
				e.setId(rs.getInt("id_etudiant"));
				e.setNom(rs.getString("nom"));
				e.setPrenom(rs.getString("prenom"));
				e.setDateNaissance(rs.getDate("date_naissance").toLocalDate());
				e.setEmail(rs.getString("email"));
				return e;
			}
			return null;
		}
	}
	
	public List<Etudiants> ListerTous() throws SQLException{
		List<Etudiants> list = new ArrayList<>();
		String sql = "SELECT * FROM Etudiant ORDER BY nom, prenom";
		try(Statement s = conn().createStatement();
				ResultSet rs = s.executeQuery(sql)){
			while (rs.next()) {
				Etudiants e = new Etudiants();
				e.setId(rs.getInt("id_etudiant"));
				e.setNom(rs.getString("nom"));
				e.setPrenom(rs.getString("prenom"));
				e.setDateNaissance(rs.getDate("date_naissance").toLocalDate());
				e.setEmail(rs.getString("email"));
				list.add(e);
			}
		}
		return list;
	}
}
