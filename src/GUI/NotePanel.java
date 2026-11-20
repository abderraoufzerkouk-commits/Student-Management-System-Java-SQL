package GUI;

import model.Note;
import model.Etudiants;
import model.Modules;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import DAO.GestionEtudiants;
import DAO.GestionModules;
import DAO.GestionNote;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class NotePanel extends JPanel {
    private GestionNote gestionNote = new GestionNote();
    private GestionEtudiants gestionEtudiants = new GestionEtudiants();
    private GestionModules gestionModules = new GestionModules();
    private JTable table;
    private NoteTableModel tableModel;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnActualiser;
    private JComboBox<Etudiants> comboEtudiant;
    private JComboBox<String> comboSession;

    public NotePanel() {
        setLayout(new BorderLayout());
        initComponents();
        loadData();
    }
    
    public void refresh() {
        loadData();
    }

    private void initComponents() {
    	
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Étudiant:"));
        comboEtudiant = new JComboBox<>();
        topPanel.add(comboEtudiant);
        topPanel.add(new JLabel("Session:"));
        comboSession = new JComboBox<>(new String[]{"Normale", "Rattrapage"});
        topPanel.add(comboSession);
        JButton btnFiltrer = new JButton("Afficher");
        topPanel.add(btnFiltrer);
        add(topPanel, BorderLayout.NORTH);

        
        tableModel = new NoteTableModel();
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

        
        btnFiltrer.addActionListener(e -> loadData());
        btnAjouter.addActionListener(e -> ajouter());
        btnModifier.addActionListener(e -> modifier());
        btnSupprimer.addActionListener(e -> supprimer());
        btnActualiser.addActionListener(e -> {
            chargerComboEtudiants();
            loadData();
        });

        chargerComboEtudiants();
    }

    private void chargerComboEtudiants() {
        try {
            List<Etudiants> etudiants = gestionEtudiants.ListerTous();
            comboEtudiant.removeAllItems();
            for (Etudiants e : etudiants) {
                comboEtudiant.addItem(e);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement étudiants: " + ex.getMessage());
        }
    }

    private void loadData() {
        Etudiants e = (Etudiants) comboEtudiant.getSelectedItem();
        if (e == null) return;
        String session = (String) comboSession.getSelectedItem();
        try {
            List<Note> notes = gestionNote.getNotesByEtudiant(e.getId(), session);
            tableModel.setData(notes);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement notes: " + ex.getMessage());
        }
    }

    private void ajouter() {
        Etudiants e = (Etudiants) comboEtudiant.getSelectedItem();
        if (e == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un étudiant.");
            return;
        }
        DialogAjouterNote dialog = new DialogAjouterNote(SwingUtilities.getWindowAncestor(this), e);
        dialog.setVisible(true);
        if (dialog.isOk()) {
            loadData();
        }
    }

    private void modifier() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une note.");
            return;
        }
        Note n = tableModel.getAt(row);
        DialogAjouterNote dialog = new DialogAjouterNote(SwingUtilities.getWindowAncestor(this), n);
        dialog.setVisible(true);
        if (dialog.isOk()) {
            loadData();
        }
    }

    private void supprimer() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Note n = tableModel.getAt(row);
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cette note ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                gestionNote.deleteNote(n.getId());
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur suppression: " + ex.getMessage());
            }
        }
    }

    class NoteTableModel extends AbstractTableModel {
        private List<Note> notes;
        private String[] colonnes = {"ID", "Étudiant", "Module", "Type", "Session", "Valeur"};

        public void setData(List<Note> list) {
            this.notes = list;
            fireTableDataChanged();
        }

        public Note getAt(int row) {
            return notes.get(row);
        }

        @Override
        public int getRowCount() {
            return notes == null ? 0 : notes.size();
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
            Note n = notes.get(row);
            switch (col) {
                case 0: return n.getId();
                case 1: return n.getIdEtudiant().getNom() + " " + n.getIdEtudiant().getPrenom();
                case 2: return n.getIdModule().getNom();
                case 3: return n.getType();
                case 4: return n.getSession();
                case 5: return n.getValeur();
                default: return null;
            }
        }
    }
}