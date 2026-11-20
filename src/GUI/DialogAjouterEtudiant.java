package GUI;

import model.Etudiants;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class DialogAjouterEtudiant extends JDialog {
    private JTextField txtNom, txtPrenom, txtEmail, txtDate;
    private JButton btnOK, btnAnnuler;
    private boolean ok = false;
    private Etudiants etudiant;

    public DialogAjouterEtudiant(Window parent) {
        this(parent, null);
    }

    public DialogAjouterEtudiant(Window parent, Etudiants e) {
        super(parent, e == null ? "Ajouter étudiant" : "Modifier étudiant", ModalityType.APPLICATION_MODAL);
        initUI();
        pack(); 
        setLocationRelativeTo(parent);
        if (e != null) remplirChamps(e);
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1; txtNom = new JTextField(30); add(txtNom, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Prénom:"), gbc);
        gbc.gridx = 1; txtPrenom = new JTextField(30); add(txtPrenom, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Date naissance (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; txtDate = new JTextField(30); add(txtDate, gbc);
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; txtEmail = new JTextField(30); add(txtEmail, gbc);

        JPanel panelBoutons = new JPanel(new FlowLayout());
        btnOK = new JButton("OK");
        btnAnnuler = new JButton("Annuler");
        panelBoutons.add(btnOK);
        panelBoutons.add(btnAnnuler);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; add(panelBoutons, gbc);

        btnOK.addActionListener(e -> valider());
        btnAnnuler.addActionListener(e -> dispose());
    }

    private void remplirChamps(Etudiants e) {
        txtNom.setText(e.getNom());
        txtPrenom.setText(e.getPrenom());
        txtDate.setText(e.getDateNaissance().toString());
        txtEmail.setText(e.getEmail());
    }

    private void valider() {
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String email = txtEmail.getText().trim();
        String dateStr = txtDate.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || dateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires.");
            return;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Date invalide (format YYYY-MM-DD)");
            return;
        }

        etudiant = new Etudiants(nom, prenom, date, email);
        ok = true;
        dispose();
    }

    public boolean isOk() {
        return ok;
    }

    public Etudiants getEtudiant() {
        return etudiant;
    }
}