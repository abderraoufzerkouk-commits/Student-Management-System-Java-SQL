package GUI;

import model.Modules;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import DAO.GestionModules;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ModulePanel extends JPanel {
    private GestionModules gestionModules = new GestionModules();
    private JTable table;
    private ModuleTableModel tableModel;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnActualiser;

    public ModulePanel() {
        setLayout(new BorderLayout());
        initComponents();
        loadData();
    }
    
    public void refresh() {
        loadData();
    }

    private void initComponents() {
        tableModel = new ModuleTableModel();
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
            List<Modules> modules = gestionModules.listerTous();
            tableModel.setData(modules);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ajouter() {
        DialogAjouterModule dialog = new DialogAjouterModule(SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        if (dialog.isOk()) {
            Modules m = dialog.getModule();
            try {
                gestionModules.addModule(m);
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur ajout: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modifier() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un module.");
            return;
        }
        Modules m = tableModel.getAt(row);
        DialogAjouterModule dialog = new DialogAjouterModule(SwingUtilities.getWindowAncestor(this), m);
        dialog.setVisible(true);
        if (dialog.isOk()) {
            Modules modif = dialog.getModule();
            modif.setId(m.getId());
            try {
                gestionModules.modifyModule(modif);
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur modification: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void supprimer() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Modules m = tableModel.getAt(row);
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer le module " + m.getNom() + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                gestionModules.deleteModule(m.getId());
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur suppression: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class ModuleTableModel extends AbstractTableModel {
        private List<Modules> modules;
        private String[] colonnes = {"ID", "Nom", "Coefficient", "Volume horaire", "Enseignant"};

        public void setData(List<Modules> list) {
            this.modules = list;
            fireTableDataChanged();
        }

        public Modules getAt(int row) {
            return modules.get(row);
        }

        @Override
        public int getRowCount() {
            return modules == null ? 0 : modules.size();
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
            Modules m = modules.get(row);
            switch (col) {
                case 0: return m.getId();
                case 1: return m.getNom();
                case 2: return m.getCoifficient();
                case 3: return m.getVolumeHoraire();
                case 4: return m.getEnseignant() == null ? "" : m.getEnseignant().getNom();
                default: return null;
            }
        }
    }
}