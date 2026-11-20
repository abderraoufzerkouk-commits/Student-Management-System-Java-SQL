package DAO;

import model.Enseignants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Connection.DatabaseConnection;

public class GestionEnseignants {
	private Connection conn() throws SQLException {
	    return DatabaseConnection.getConnection();
	}
	
	public void addEnseignant(Enseignants e) throws SQLException{
		String sql = "INSERT INTO Enseignant (nom, specialite) VALUES (?, ?)";
		try(PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
			ps.setString(1, e.getNom());
			ps.setString(2, e.getSpecialite());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) e.setId(rs.getInt(1));
		}
	}
	
	public void deleteEnseignant (int id) throws SQLException{
		String sql = "DELETE FROM Enseignant WHERE id_enseignant=?";
		try(PreparedStatement ps = conn().prepareStatement(sql)){
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}
	
	public void modifyEnseignant (Enseignants e) throws SQLException{
		String sql = "UPDATE Enseignant SET nom=?, specialite=? WHERE id_enseignant=?";
		try(PreparedStatement ps = conn().prepareStatement(sql)){
			ps.setString(1, e.getNom());
			ps.setString(2, e.getSpecialite());
			ps.setInt(3, e.getId());
			ps.executeUpdate();
		}
	}
	
	public Enseignants FindById(int id) throws SQLException{
		String sql = "SELECT * FROM Enseignant WHERE id_enseignant=?";
		try(PreparedStatement ps = conn().prepareStatement(sql)){
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				Enseignants e = new Enseignants();
				e.setId(id);
				e.setNom(rs.getString("nom"));
				e.setSpecialite(rs.getString("specialite"));
				return e;
			} 
			return null;
		}
	}
	
	public List<Enseignants> ListerTous() throws SQLException{
		List<Enseignants> list = new ArrayList<>();
		String sql = "SELECT * FROM Enseignant ORDER BY nom";
		try(Statement s = conn().createStatement();
				ResultSet rs = s.executeQuery(sql)){
			while(rs.next()) {
				Enseignants e = new Enseignants();
				e.setId(rs.getInt("id_enseignant"));
				e.setNom(rs.getString("nom"));
				e.setSpecialite(rs.getString("specialite"));
				list.add(e);
			}
		}
		return list;
	}
}
