package DAO;

import model.Note;
import model.Etudiants;
import model.Modules;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Connection.DatabaseConnection;

public class GestionNote {
	
	private Connection conn() throws SQLException {
	    return DatabaseConnection.getConnection();
	}
	
	public void addNote(Note n) throws SQLException{
		String sql = "INSERT INTO Note (id_etudiant, id_module, valeur, type, session) VALUES (?,?,?,?,?)";
		try(PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
			 ps.setInt(1, n.getIdEtudiant().getId());
		     ps.setInt(2, n.getIdModule().getId());   
		     ps.setDouble(3, n.getValeur());        
		     ps.setString(4, n.getType());              
		     ps.setString(5, n.getSession());
             ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) n.setId(rs.getInt(1));
		}
	}
	
	public void modifyNote(Note n) throws SQLException{
		String sql = "UPDATE Note SET valeur=? WHERE id_note=?";
		try(PreparedStatement ps = conn().prepareStatement(sql)){
			ps.setDouble(1, n.getValeur());
            ps.setInt(2, n.getId());
            ps.executeUpdate();
		}
	}
	
	public void deleteNote (int idNote) throws SQLException{
		String sql = "DELETE FROM Note WHERE id_note=?";
		try(PreparedStatement ps = conn().prepareStatement(sql)){
			ps.setInt(1, idNote);
			ps.executeUpdate();
		}
	}
	
	public Note getNote(int idEtudiant, int idModule, String type, String session) throws SQLException{
		String sql = "SELECT * FROM Note WHERE id_etudiant=? AND id_module=? AND type=? AND session=?";
		try(PreparedStatement ps = conn().prepareStatement(sql)){
			ps.setInt(1, idEtudiant);
		    ps.setInt(2, idModule);
		    ps.setString(3, type);
		    ps.setString(4, session);
		    ResultSet rs = ps.executeQuery();
		    if (rs.next()) {
		       Note n = new Note();
		       n.setId(rs.getInt("id_note"));
		       n.setValeur(rs.getDouble("valeur"));
		       n.setType(rs.getString("type"));
		       n.setSession(rs.getString("session"));
		       Etudiants e = new Etudiants();
               e.setId(rs.getInt("id_etudiant"));
               n.setIdEtudiant(e);
		       Modules m = new Modules();
               m.setId(rs.getInt("id_module"));
               n.setIdModule(m);
		    return n;
		   }
		    return null;
		}
	}
	
	public List<Note> getNotesByEtudiant(int idEtudiant, String session) throws SQLException {
        List<Note> list = new ArrayList<>();
        String sql = "SELECT * FROM Note WHERE id_etudiant=? AND session=? ORDER BY id_module, type";
        try (PreparedStatement stmt = conn().prepareStatement(sql)) {
            stmt.setInt(1, idEtudiant);
            stmt.setString(2, session);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Note n = new Note();
                n.setId(rs.getInt("id_note"));
                n.setValeur(rs.getDouble("valeur"));
                n.setType(rs.getString("type"));
                n.setSession(rs.getString("session"));
                Etudiants e = new Etudiants();
                e.setId(rs.getInt("id_etudiant"));
                n.setIdEtudiant(e);
                Modules m = new Modules();
                m.setId(rs.getInt("id_module"));
                n.setIdModule(m);
                list.add(n);
            }
        }
        return list;
    }
}
