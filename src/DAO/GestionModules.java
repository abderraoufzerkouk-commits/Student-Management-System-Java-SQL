package DAO;

import model.Enseignants;
import model.Modules;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Connection.DatabaseConnection;

public class GestionModules {	
	
	private Connection conn() throws SQLException {
	    return DatabaseConnection.getConnection();
	}
	
	public void addModule(Modules m) throws SQLException{
		String sql = "INSERT INTO Module(nom, coefficient, volume_horaire, id_enseignant) VALUES (?,?,?,?)";
		try(PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
			ps.setString(1, m.getNom());
			ps.setDouble(2, m.getCoifficient());
			ps.setInt(3, m.getVolumeHoraire());
			ps.setInt(4, m.getEnseignant().getId());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) m.setId(rs.getInt(1));
		}
	}
	
	public void modifyModule (Modules m) throws SQLException{
		String sql = "UPDATE Module SET nom=?, coefficient=?, volume_horaire=?, id_enseignant=? WHERE id_module=?";
		try(PreparedStatement ps = conn().prepareStatement(sql)){
			ps.setString(1, m.getNom());
			ps.setDouble(2, m.getCoifficient());
			ps.setInt(3, m.getVolumeHoraire());
			ps.setInt(4, m.getEnseignant().getId());
			ps.setInt(5, m.getId());
			ps.executeUpdate();
		}
	}
	
	public void deleteModule (int id) throws SQLException{
		String sql = "DELETE FROM Module WHERE id_module=?";
		try(PreparedStatement ps = conn().prepareStatement(sql)){
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}
	
	public Modules FindById(int id) throws SQLException{
		String sql = "SELECT m.*, e.nom as enseignant_nom FROM Module m JOIN Enseignant e ON m.id_enseignant = e.id_enseignant WHERE m.id_module=?";
		try(PreparedStatement ps = conn().prepareStatement(sql)){
			ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Modules m = new Modules();
                m.setId(id);
                m.setNom(rs.getString("nom"));
                m.setCoifficient(rs.getDouble("coefficient"));
                m.setVolumeHoraire(rs.getInt("volume_horaire"));
                Enseignants ens = new Enseignants();
                ens.setId(rs.getInt("id_enseignant"));
                ens.setNom(rs.getString("enseignant_nom"));
                m.setEnseignant(ens);
                return m;
            }
            return null;
		}
	}
	
	public List<Modules> listerTous() throws SQLException{
		 List<Modules> list = new ArrayList<>();
	        String sql = "SELECT m.*, e.nom as enseignant_nom FROM Module m JOIN Enseignant e ON m.id_enseignant = e.id_enseignant ORDER BY m.nom";
	        try (Statement s = conn().createStatement();
	             ResultSet rs = s.executeQuery(sql)) {
	            while (rs.next()) {
	                Modules m = new Modules();
	                m.setId(rs.getInt("id_module"));
	                m.setNom(rs.getString("nom"));
	                m.setCoifficient(rs.getDouble("coefficient"));
	                m.setVolumeHoraire(rs.getInt("volume_horaire"));
	                Enseignants ens = new Enseignants();
	                ens.setId(rs.getInt("id_enseignant"));
	                ens.setNom(rs.getString("enseignant_nom"));
	                m.setEnseignant(ens);
	                list.add(m);
	            }
	        }
	        return list;
	    }
}