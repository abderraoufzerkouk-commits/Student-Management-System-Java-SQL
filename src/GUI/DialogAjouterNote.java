package GUI;

import model.Note;
import model.Etudiants;
import model.Modules;

import javax.swing.*;

import DAO.GestionEtudiants;
import DAO.GestionModules;
import DAO.GestionNote;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DialogAjouterNote extends JDialog {
    private JComboBox<Modules> comboModule;
    private JComboBox<String> comboType, comboSession;
    private JTextField txtValeur;
    private JButton btnOK, btnAnnuler;
    private boolean ok = false;
    private Note note;
    private Etudiants etudiant;   
    private GestionNote gestionNote = new GestionNote();
    private GestionModules gestionModules = new GestionModules();

    
    public DialogAjouterNote(Window parent, Etudiants e) {
        super(parent, "Ajouter une note", ModalityType.APPLICATION_MODAL);
        this.etudiant = e;
        initUI();
        chargerModules();
        setSize(400, 280);
        setLocationRelativeTo(parent);
    }

    
    public DialogAjouterNote(Window parent, Note n) {
        super(parent, "Modifier une note", ModalityType.APPLICATION_MODAL);
        this.note = n;
        this.etudiant = n.getIdEtudiant();
        initUI();
        chargerModules();
        remplirChamps(n);
        setSize(400, 280);
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Étudiant:"), gbc);
        gbc.gridx = 1; add(new JLabel(etudiant.getNom() + " " + etudiant.getPrenom()), gbc);

        
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Module:"), gbc);
        gbc.gridx = 1; comboModule = new JComboBox<>(); add(comboModule, gbc);

        
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Type:"), gbc);
        gbc.gridx = 1; comboType = new JComboBox<>(new String[]{"CC", "Examen", "Rattrapage"}); add(comboType, gbc);

        
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Session:"), gbc);
        gbc.gridx = 1; comboSession = new JComboBox<>(new String[]{"Normale", "Rattrapage"}); add(comboSession, gbc);

        
        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Note (0-20):"), gbc);
        gbc.gridx = 1; txtValeur = new JTextField(); add(txtValeur, gbc);

        
        JPanel panelBoutons = new JPanel(new FlowLayout());
        btnOK = new JButton("OK");
        btnAnnuler = new JButton("Annuler");
        panelBoutons.add(btnOK);
        panelBoutons.add(btnAnnuler);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; add(panelBoutons, gbc);

        btnOK.addActionListener(e -> valider());
        btnAnnuler.addActionListener(e -> dispose());
    }

    private void chargerModules() {
        try {
            List<Modules> modules = gestionModules.listerTous();
            for (Modules m : modules) {
                comboModule.addItem(m);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement modules: " + ex.getMessage());
        }
    }

    private void remplirChamps(Note n) {
        comboModule.setSelectedItem(n.getIdModule());
        comboType.setSelectedItem(n.getType());
        comboSession.setSelectedItem(n.getSession());
        txtValeur.setText(String.valueOf(n.getValeur()));
    }

    private void valider() {
        Modules module = (Modules) comboModule.getSelectedItem();
        String type = (String) comboType.getSelectedItem();
        String session = (String) comboSession.getSelectedItem();
        String valeurStr = txtValeur.getText().trim();

        if (module == null || type == null || session == null || valeurStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires.");
            return;
        }

        double valeur;
        try {
            valeur = Double.parseDouble(valeurStr);
            if (valeur < 0 || valeur > 20) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La note doit être un nombre entre 0 et 20.");
            return;
        }

        if (note == null) {
            
            note = new Note(valeur, type, session, etudiant, module);
            try {
                gestionNote.addNote(note);
                JOptionPane.showMessageDialog(this, "Note ajoutée avec succès.");
                ok = true;
                dispose();
            } catch (SQLException ex) {
                if (ex.getMessage().contains("UNIQUE")) {
                    JOptionPane.showMessageDialog(this, "Une note de ce type pour cette session existe déjà pour ce module.");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
                }
            }
        } else {
            
            note.setValeur(valeur);
            
            try {
                gestionNote.modifyNote(note);
                JOptionPane.showMessageDialog(this, "Note modifiée avec succès.");
                ok = true;
                dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
            }
        }
    }

    public boolean isOk() {
        return ok;
    }

    public Note getNote() {
        return note;
    }
}