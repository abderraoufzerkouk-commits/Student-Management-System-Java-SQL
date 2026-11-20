package GUI;

import model.Etudiants;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import DAO.GestionEtudiants;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EtudiantPanel extends JPanel {
    private GestionEtudiants gestionEtudiants = new GestionEtudiants();
    private JTable table;
    private EtudiantTableModel tableModel;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnActualiser;

    public EtudiantPanel() {
        setLayout(new BorderLayout());
        initComponents();
        loadData();
    }
    
    public void refresh() {
        loadData();
    }

    private void initComponents() {
        tableModel = new EtudiantTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBoutons = new JPanel(new FlowLayout());
        btnAjouter = new JButton("Ajouter");
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");
        btnActualiser = new JButton("Actualiser");
        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnActualiser);
        add(panelBoutons, BorderLayout.SOUTH);

        btnAjouter.addActionListener(e -> ajouter());
        btnModifier.addActionListener(e -> modifier());
        btnSupprimer.addActionListener(e -> supprimer());
        btnActualiser.addActionListener(e -> loadData());
    }

    private void loadData() {
        try {
            List<Etudiants> etudiants = gestionEtudiants.ListerTous();
            tableModel.setData(etudiants);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ajouter() {
        DialogAjouterEtudiant dialog = new DialogAjouterEtudiant(SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        if (dialog.isOk()) {
            Etudiants e = dialog.getEtudiant();
            try {
                gestionEtudiants.addEtudiant(e);
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur ajout: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modifier() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un étudiant.");
            return;
        }
        Etudiants e = tableModel.getAt(row);
        DialogAjouterEtudiant dialog = new DialogAjouterEtudiant(SwingUtilities.getWindowAncestor(this), e);
        dialog.setVisible(true);
        if (dialog.isOk()) {
            Etudiants modif = dialog.getEtudiant();
            modif.setId(e.getId());
            try {
                gestionEtudiants.modifyEtudiant(modif);
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur modification: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void supprimer() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Etudiants e = tableModel.getAt(row);
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer " + e.getNom() + " " + e.getPrenom() + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                gestionEtudiants.deleteEtudiant(e.getId());
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur suppression: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    
    class EtudiantTableModel extends AbstractTableModel {
        private List<Etudiants> etudiants;
        private String[] colonnes = {"ID", "Nom", "Prénom", "Date naissance", "Email"};

        public void setData(List<Etudiants> list) {
            this.etudiants = list;
            fireTableDataChanged();
        }

        public Etudiants getAt(int row) {
            return etudiants.get(row);
        }

        @Override
        public int getRowCount() {
            return etudiants == null ? 0 : etudiants.size();
        }

        @Override
        public int getColumnCount() {
            return colonnes.length;
        }

        @Override
        public String getColumnName(int col) {
            return colonnes[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            Etudiants e = etudiants.get(row);
            switch (col) {
                case 0: return e.getId();
                case 1: return e.getNom();
                case 2: return e.getPrenom();
                case 3: return e.getDateNaissance();
                case 4: return e.getEmail();
                default: return null;
            }
        }
    }
}