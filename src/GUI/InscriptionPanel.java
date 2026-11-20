package GUI;

import model.Etudiants;
import model.Modules;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import DAO.GestionEtudiants;
import DAO.GestionInscription;
import DAO.GestionModules;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InscriptionPanel extends JPanel {
    private GestionInscription gestionInscription = new GestionInscription();
    private GestionEtudiants gestionEtudiants = new GestionEtudiants();
    private GestionModules gestionModules = new GestionModules();
    private JTable table;
    private InscriptionTableModel tableModel;
    private JButton btnInscrire, btnDesinscrire, btnActualiser;
    private JComboBox<Etudiants> comboEtudiant;
    private JComboBox<Modules> comboModule;

    public InscriptionPanel() {
        setLayout(new BorderLayout());
        initComponents();
        loadData();
        chargerCombos();
    }
    
    public void refresh() {
        loadData();
    }

    private void initComponents() {
    	
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Étudiant:"));
        comboEtudiant = new JComboBox<>();
        topPanel.add(comboEtudiant);
        topPanel.add(new JLabel("Module:"));
        comboModule = new JComboBox<>();
        topPanel.add(comboModule);
        btnInscrire = new JButton("Inscrire");
        btnDesinscrire = new JButton("Désinscrire");
        btnActualiser = new JButton("Actualiser");
        topPanel.add(btnInscrire);
        topPanel.add(btnDesinscrire);
        topPanel.add(btnActualiser);
        add(topPanel, BorderLayout.NORTH);

        
        tableModel = new InscriptionTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        
        comboEtudiant.addActionListener(e -> loadData());
        btnInscrire.addActionListener(e -> inscrire());
        btnDesinscrire.addActionListener(e -> desinscrire());
        btnActualiser.addActionListener(e -> {
            chargerCombos();
            loadData();
        });
    }

    private void chargerCombos() {
        try {
            List<Etudiants> etudiants = gestionEtudiants.ListerTous();
            comboEtudiant.removeAllItems();
            for (Etudiants e : etudiants) comboEtudiant.addItem(e);

            List<Modules> modules = gestionModules.listerTous();
            comboModule.removeAllItems();
            for (Modules m : modules) comboModule.addItem(m);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement listes: " + ex.getMessage());
        }
    }

    private void loadData() {
        Etudiants e = (Etudiants) comboEtudiant.getSelectedItem();
        if (e == null) return;
        try {
            List<Integer> idModules = gestionInscription.getModulesByEtudiant(e.getId());
            List<Modules> modulesInscrits = new ArrayList<>();
            for (int id : idModules) {
                Modules m = gestionModules.FindById(id);
                if (m != null) modulesInscrits.add(m);
            }
            tableModel.setData(modulesInscrits, e);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement inscriptions: " + ex.getMessage());
        }
    }

    private void inscrire() {
        Etudiants e = (Etudiants) comboEtudiant.getSelectedItem();
        Modules m = (Modules) comboModule.getSelectedItem();
        if (e == null || m == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un étudiant et un module.");
            return;
        }
        try {
            if (gestionInscription.estInscrit(e.getId(), m.getId())) {
                JOptionPane.showMessageDialog(this, "Cet étudiant est déjà inscrit à ce module.");
                return;
            }
            gestionInscription.inscrire(e.getId(), m.getId(), LocalDate.now());
            JOptionPane.showMessageDialog(this, "Inscription réussie.");
            loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur inscription: " + ex.getMessage());
        }
    }

    private void desinscrire() {
        Etudiants e = (Etudiants) comboEtudiant.getSelectedItem();
        Modules m = (Modules) comboModule.getSelectedItem();
        if (e == null || m == null) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Désinscrire " + e.getNom() + " de " + m.getNom() + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                gestionInscription.desinscrire(e.getId(), m.getId());
                JOptionPane.showMessageDialog(this, "Désinscription réussie.");
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur désinscription: " + ex.getMessage());
            }
        }
    }

    class InscriptionTableModel extends AbstractTableModel {
        private List<Modules> modules;
        private Etudiants etudiant;
        private String[] colonnes = {"ID Module", "Nom du module", "Coefficient", "Volume horaire"};

        public void setData(List<Modules> list, Etudiants e) {
            this.modules = list;
            this.etudiant = e;
            fireTableDataChanged();
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
                default: return null;
            }
        }
    }
}