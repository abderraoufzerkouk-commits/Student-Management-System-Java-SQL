package GUI;

import model.Enseignants;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import DAO.GestionEnseignants;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class EnseignantPanel extends JPanel {
    private GestionEnseignants gestionEnseignants = new GestionEnseignants();
    private JTable table;
    private EnseignantTableModel tableModel;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnActualiser;

    public EnseignantPanel() {
        setLayout(new BorderLayout());
        initComponents();
        loadData();
    }
    
    public void refresh() {
        loadData();
    }

    private void initComponents() {
        tableModel = new EnseignantTableModel();
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
            List<Enseignants> enseignants = gestionEnseignants.ListerTous();
            tableModel.setData(enseignants);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ajouter() {
        DialogAjouterEnseignant dialog = new DialogAjouterEnseignant(SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        if (dialog.isOk()) {
            Enseignants e = dialog.getEnseignant();
            try {
                gestionEnseignants.addEnseignant(e);
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur ajout: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modifier() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un enseignant.");
            return;
        }
        Enseignants e = tableModel.getAt(row);
        DialogAjouterEnseignant dialog = new DialogAjouterEnseignant(SwingUtilities.getWindowAncestor(this), e);
        dialog.setVisible(true);
        if (dialog.isOk()) {
            Enseignants modif = dialog.getEnseignant();
            modif.setId(e.getId());
            try {
                gestionEnseignants.modifyEnseignant(modif);
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur modification: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void supprimer() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Enseignants e = tableModel.getAt(row);
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer " + e.getNom() + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                gestionEnseignants.deleteEnseignant(e.getId());
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur suppression: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class EnseignantTableModel extends AbstractTableModel {
        private List<Enseignants> enseignants;
        private String[] colonnes = {"ID", "Nom", "Spécialité"};

        public void setData(List<Enseignants> list) {
            this.enseignants = list;
            fireTableDataChanged();
        }

        public Enseignants getAt(int row) {
            return enseignants.get(row);
        }

        @Override
        public int getRowCount() {
            return enseignants == null ? 0 : enseignants.size();
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
            Enseignants e = enseignants.get(row);
            switch (col) {
                case 0: return e.getId();
                case 1: return e.getNom();
                case 2: return e.getSpecialite();
                default: return null;
            }
        }
    }
}