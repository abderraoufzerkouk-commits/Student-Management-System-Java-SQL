package GUI;

import model.Enseignants;

import javax.swing.*;
import java.awt.*;

public class DialogAjouterEnseignant extends JDialog {
    private JTextField txtNom, txtSpecialite;
    private JButton btnOK, btnAnnuler;
    private boolean ok = false;
    private Enseignants enseignant;

    public DialogAjouterEnseignant(Window parent) {
        this(parent, null);
    }

    public DialogAjouterEnseignant(Window parent, Enseignants e) {
        super(parent, e == null ? "Ajouter enseignant" : "Modifier enseignant", ModalityType.APPLICATION_MODAL);
        setSize(350, 180);
        setLocationRelativeTo(parent);
        initUI();
        if (e != null) remplirChamps(e);
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1; txtNom = new JTextField(15); add(txtNom, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Spécialité:"), gbc);
        gbc.gridx = 1; txtSpecialite = new JTextField(15); add(txtSpecialite, gbc);

        JPanel panelBoutons = new JPanel(new FlowLayout());
        btnOK = new JButton("OK");
        btnAnnuler = new JButton("Annuler");
        panelBoutons.add(btnOK);
        panelBoutons.add(btnAnnuler);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; add(panelBoutons, gbc);

        btnOK.addActionListener(e -> valider());
        btnAnnuler.addActionListener(e -> dispose());
    }

    private void remplirChamps(Enseignants e) {
        txtNom.setText(e.getNom());
        txtSpecialite.setText(e.getSpecialite());
    }

    private void valider() {
        String nom = txtNom.getText().trim();
        String specialite = txtSpecialite.getText().trim();

        if (nom.isEmpty() || specialite.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires.");
            return;
        }

        enseignant = new Enseignants(nom, specialite);
        ok = true;
        dispose();
    }

    public boolean isOk() {
        return ok;
    }

    public Enseignants getEnseignant() {
        return enseignant;
    }
}