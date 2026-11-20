package GUI;

import model.Etudiants;
import model.Modules;
import Service.Resultat;
import Service.Diplome;

import javax.swing.*;

import DAO.GestionEtudiants;
import DAO.GestionModules;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ResultatPanel extends JPanel {
    private GestionEtudiants gestionEtudiants = new GestionEtudiants();
    private GestionModules gestionModules = new GestionModules();
    private Resultat resultatService = new Resultat();
    private Diplome diplomeService = new Diplome();

    private JComboBox<Etudiants> comboEtudiants;
    private JTextArea textArea;
    private JButton btnMoyenneAvant, btnMoyenneApres, btnRattrapage, btnDiplomes, btnDetailsNotes;

    public ResultatPanel() {
        setLayout(new BorderLayout());
        initUI();
        chargerEtudiants();
    }
    
    public void refresh() {
        chargerEtudiants();
        textArea.setText("");
    }

    private void initUI() {
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Étudiant :"));
        comboEtudiants = new JComboBox<>();
        topPanel.add(comboEtudiants);

        btnMoyenneAvant = new JButton("Moyenne avant rattrapage");
        btnMoyenneApres = new JButton("Moyenne après rattrapage");
        btnRattrapage = new JButton("Étudiants en rattrapage");
        btnDiplomes = new JButton("Diplômés");
        btnDetailsNotes = new JButton("Détail des notes (avant rattrapage)");

        topPanel.add(btnMoyenneAvant);
        topPanel.add(btnMoyenneApres);
        topPanel.add(btnRattrapage);
        topPanel.add(btnDiplomes);
        topPanel.add(btnDetailsNotes);

        add(topPanel, BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Action listeners
        btnMoyenneAvant.addActionListener(e -> afficherMoyenneAvant());
        btnMoyenneApres.addActionListener(e -> afficherMoyenneApres());
        btnRattrapage.addActionListener(e -> afficherRattrapage());
        btnDiplomes.addActionListener(e -> afficherDiplomes());
        btnDetailsNotes.addActionListener(e -> afficherDetailsNotes());
    }

    private void chargerEtudiants() {
        try {
            List<Etudiants> etudiants = gestionEtudiants.ListerTous();
            comboEtudiants.removeAllItems();
            for (Etudiants e : etudiants) {
                comboEtudiants.addItem(e);
            }
            if (etudiants.size() > 0) {
                comboEtudiants.setSelectedIndex(0);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement étudiants : " + ex.getMessage());
        }
    }

    private Etudiants getEtudiantSelectionne() {
        return (Etudiants) comboEtudiants.getSelectedItem();
    }

    private void afficherMoyenneAvant() {
        Etudiants e = getEtudiantSelectionne();
        if (e == null) return;
        try {
            Double moyenne = resultatService.getMoyenneGenerale(e.getId(), "Normale");
            if (moyenne == null) {
                textArea.setText("Moyenne générale avant rattrapage pour " + e.getNom() + " " + e.getPrenom() + " :\nNon calculable (notes manquantes ou aucun module).");
            } else {
                textArea.setText(String.format("Moyenne générale avant rattrapage pour %s %s :\n%.2f", e.getNom(), e.getPrenom(), moyenne));
            }
        } catch (SQLException ex) {
            textArea.setText("Erreur : " + ex.getMessage());
        }
    }

    private void afficherMoyenneApres() {
        Etudiants e = getEtudiantSelectionne();
        if (e == null) return;
        try {
            Double moyenne = resultatService.getMoyenneGeneraleApresRattrapage(e.getId());
            if (moyenne == null) {
                textArea.setText("Moyenne générale après rattrapage pour " + e.getNom() + " " + e.getPrenom() + " :\nNon calculable (notes manquantes ou aucun module).");
            } else {
                textArea.setText(String.format("Moyenne générale après rattrapage pour %s %s :\n%.2f", e.getNom(), e.getPrenom(), moyenne));
            }
        } catch (SQLException ex) {
            textArea.setText("Erreur : " + ex.getMessage());
        }
    }

    private void afficherRattrapage() {
        try {
            List<Integer> ids = resultatService.getEtudiantsEnRattrapage();
            if (ids.isEmpty()) {
                textArea.setText("Aucun étudiant en rattrapage.");
                return;
            }
            StringBuilder sb = new StringBuilder("Étudiants en rattrapage (au moins une matière < 10) :\n");
            for (int id : ids) {
                Etudiants e = gestionEtudiants.findById(id);
                if (e != null) {
                    sb.append("- ").append(e.getNom()).append(" ").append(e.getPrenom()).append("\n");
                }
            }
            textArea.setText(sb.toString());
        } catch (SQLException ex) {
            textArea.setText("Erreur : " + ex.getMessage());
        }
    }

    private void afficherDiplomes() {
        try {
            List<Integer> ids = diplomeService.getDiplomes();
            if (ids.isEmpty()) {
                textArea.setText("Aucun étudiant diplômé pour l'instant.");
                return;
            }
            StringBuilder sb = new StringBuilder("Étudiants diplômés (tous modules validés) :\n");
            for (int id : ids) {
                Etudiants e = gestionEtudiants.findById(id);
                if (e != null) {
                    sb.append("- ").append(e.getNom()).append(" ").append(e.getPrenom()).append("\n");
                }
            }
            textArea.setText(sb.toString());
        } catch (SQLException ex) {
            textArea.setText("Erreur : " + ex.getMessage());
        }
    }

    private void afficherDetailsNotes() {
        Etudiants e = getEtudiantSelectionne();
        if (e == null) return;
        try {
            List<Map<String, Object>> details = resultatService.getNotesAvantRattrapage(e.getId());
            if (details.isEmpty()) {
                textArea.setText("Aucune note trouvée pour " + e.getNom() + " " + e.getPrenom());
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Détail des notes (avant rattrapage) pour ").append(e.getNom()).append(" ").append(e.getPrenom()).append(" :\n\n");
            for (Map<String, Object> ligne : details) {
                String module = (String) ligne.get("module");
                Object cc = ligne.get("cc");
                Object examen = ligne.get("examen");
                Object moyenne = ligne.get("moyenne");
                sb.append("Module : ").append(module).append("\n");
                sb.append("  CC      : ").append(cc != null ? String.format("%.2f", (Double) cc) : "non saisie").append("\n");
                sb.append("  Examen  : ").append(examen != null ? String.format("%.2f", (Double) examen) : "non saisie").append("\n");
                sb.append("  Moyenne : ").append(moyenne != null ? String.format("%.2f", (Double) moyenne) : "non calculable").append("\n\n");
            }
            textArea.setText(sb.toString());
        } catch (SQLException ex) {
            textArea.setText("Erreur : " + ex.getMessage());
        }
    }
}