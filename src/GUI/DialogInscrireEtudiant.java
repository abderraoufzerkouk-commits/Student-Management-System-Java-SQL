package GUI;

import model.Etudiants;
import model.Modules;

import javax.swing.*;

import DAO.GestionEtudiants;
import DAO.GestionInscription;
import DAO.GestionModules;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class DialogInscrireEtudiant extends JDialog {
	
    private JComboBox<Etudiants> comboEtudiant;
    private JComboBox<Modules> comboModule;
    private JButton btnInscrire, btnAnnuler;
    private boolean ok = false;
    private GestionInscription gestionInscription = new GestionInscription();
    private GestionEtudiants gestionEtudiants = new GestionEtudiants();
    private GestionModules gestionModules = new GestionModules();

    public DialogInscrireEtudiant(Window parent) {
        super(parent, "Inscrire un étudiant à un module", ModalityType.APPLICATION_MODAL);
        initUI();
        chargerCombos();
        setSize(400, 200);
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Étudiant:"), gbc);
        gbc.gridx = 1; comboEtudiant = new JComboBox<>(); add(comboEtudiant, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Module:"), gbc);
        gbc.gridx = 1; comboModule = new JComboBox<>(); add(comboModule, gbc);

        JPanel panelBoutons = new JPanel(new FlowLayout());
        btnInscrire = new JButton("Inscrire");
        btnAnnuler = new JButton("Annuler");
        panelBoutons.add(btnInscrire);
        panelBoutons.add(btnAnnuler);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; add(panelBoutons, gbc);

        btnInscrire.addActionListener(e -> inscrire());
        btnAnnuler.addActionListener(e -> dispose());
    }

    private void chargerCombos() {
        try {
            List<Etudiants> etudiants = gestionEtudiants.ListerTous();
            for (Etudiants e : etudiants) comboEtudiant.addItem(e);

            List<Modules> modules = gestionModules.listerTous();
            for (Modules m : modules) comboModule.addItem(m);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement: " + ex.getMessage());
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
            ok = true;
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
        }
    }

    public boolean isOk() {
        return ok;
    }
}