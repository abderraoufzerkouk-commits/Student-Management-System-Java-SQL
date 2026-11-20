package GUI;

import model.Enseignants;
import model.Modules;

import javax.swing.*;

import DAO.GestionEnseignants;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DialogAjouterModule extends JDialog {
    private JTextField txtNom, txtCoefficient, txtVolume;
    private JComboBox<Enseignants> comboEnseignant;
    private JButton btnOK, btnAnnuler;
    private boolean ok = false;
    private Modules module;
    private GestionEnseignants gestionEnseignants = new GestionEnseignants();

    public DialogAjouterModule(Window parent) {
        this(parent, null);
    }

    public DialogAjouterModule(Window parent, Modules m) {
        super(parent, m == null ? "Ajouter module" : "Modifier module", ModalityType.APPLICATION_MODAL);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        initUI();
        chargerEnseignants();
        if (m != null) remplirChamps(m);
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1; txtNom = new JTextField(20); add(txtNom, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Coefficient:"), gbc);
        gbc.gridx = 1; txtCoefficient = new JTextField(20); add(txtCoefficient, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Volume horaire:"), gbc);
        gbc.gridx = 1; txtVolume = new JTextField(20); add(txtVolume, gbc);
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Enseignant:"), gbc);
        gbc.gridx = 1; comboEnseignant = new JComboBox<>(); add(comboEnseignant, gbc);

        JPanel panelBoutons = new JPanel(new FlowLayout());
        btnOK = new JButton("OK");
        btnAnnuler = new JButton("Annuler");
        panelBoutons.add(btnOK);
        panelBoutons.add(btnAnnuler);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; add(panelBoutons, gbc);

        btnOK.addActionListener(e -> valider());
        btnAnnuler.addActionListener(e -> dispose());
    }

    private void chargerEnseignants() {
        try {
            List<Enseignants> enseignants = gestionEnseignants.ListerTous();
            for (Enseignants e : enseignants) {
                comboEnseignant.addItem(e);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement enseignants: " + ex.getMessage());
        }
    }

    private void remplirChamps(Modules m) {
        txtNom.setText(m.getNom());
        txtCoefficient.setText(String.valueOf(m.getCoifficient()));
        txtVolume.setText(String.valueOf(m.getVolumeHoraire()));
        if (m.getEnseignant() != null) {
            comboEnseignant.setSelectedItem(m.getEnseignant());
        }
    }

    private void valider() {
        String nom = txtNom.getText().trim();
        String coeffStr = txtCoefficient.getText().trim();
        String volStr = txtVolume.getText().trim();
        Enseignants enseignant = (Enseignants) comboEnseignant.getSelectedItem();

        if (nom.isEmpty() || coeffStr.isEmpty() || volStr.isEmpty() || enseignant == null) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires et un enseignant doit être sélectionné.");
            return;
        }

        double coefficient;
        int volume;
        try {
            coefficient = Double.parseDouble(coeffStr);
            volume = Integer.parseInt(volStr);
            if (coefficient <= 0) throw new NumberFormatException();
            if (volume <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Coefficient (positif) et volume (entier positif) invalides.");
            return;
        }

        module = new Modules(nom, coefficient, volume, enseignant);
        ok = true;
        dispose();
    }

    public boolean isOk() {
        return ok;
    }

    public Modules getModule() {
        return module;
    }
}