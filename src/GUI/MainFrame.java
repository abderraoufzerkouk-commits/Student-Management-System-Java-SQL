package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel buttonPanel;

    public MainFrame() {
        setTitle("GESTION UNIVERSITAIRE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        
    	
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(new EtudiantPanel(), "Etudiants");
        cardPanel.add(new EnseignantPanel(), "Enseignants");
        cardPanel.add(new ModulePanel(), "Modules");
        cardPanel.add(new InscriptionPanel(), "Inscriptions");
        cardPanel.add(new NotePanel(), "Notes");
        cardPanel.add(new ResultatPanel(), "Resultats");

        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        String[] titres = {"Etudiants", "Enseignants", "Modules", "Inscriptions", "Notes", "Resultats"};
        for (String titre : titres) {
            JButton btn = new JButton(titre);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(150, 40));
            btn.setPreferredSize(new Dimension(150, 40));
            btn.addActionListener(e -> cardLayout.show(cardPanel, titre));
            buttonPanel.add(btn);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); 
        }

        
        JButton btnRefreshAll = new JButton("Actualiser tout");
        btnRefreshAll.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRefreshAll.setMaximumSize(new Dimension(150, 40));
        btnRefreshAll.setPreferredSize(new Dimension(150, 40));
        btnRefreshAll.addActionListener(e -> refreshAll());
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
        buttonPanel.add(btnRefreshAll);
        
        
        buttonPanel.add(Box.createVerticalGlue());

        
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);
    }

    private void refreshAll() {
        Component[] components = cardPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof EtudiantPanel) ((EtudiantPanel) comp).refresh();
            else if (comp instanceof EnseignantPanel) ((EnseignantPanel) comp).refresh();
            else if (comp instanceof ModulePanel) ((ModulePanel) comp).refresh();
            else if (comp instanceof InscriptionPanel) ((InscriptionPanel) comp).refresh();
            else if (comp instanceof NotePanel) ((NotePanel) comp).refresh();
            else if (comp instanceof ResultatPanel) ((ResultatPanel) comp).refresh();
        }
        JOptionPane.showMessageDialog(this, "Tous les panneaux ont été actualisés.");
    }

    public static void main(String[] args) {
        // Modification du font
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("Label.font", new Font("Tahoma", Font.PLAIN, 16));
        UIManager.put("Table.font", new Font("Tahoma", Font.PLAIN, 14));
        UIManager.put("TableHeader.font", new Font("Arial", Font.BOLD, 15));
        UIManager.put("TextField.font", new Font("Arial", Font.PLAIN, 16));
        
        
     // Modification des couleurs
        UIManager.put("Panel.background", new Color(245, 245, 245));
        UIManager.put("Panel.foreground", new Color(50, 50, 50));

        UIManager.put("Button.background", new Color(52, 152, 219));  
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.border", BorderFactory.createLineBorder(new Color(41, 128, 185), 1));
        UIManager.put("Button.select", new Color(41, 128, 185));

        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.foreground", new Color(50, 50, 50));
        UIManager.put("Table.selectionBackground", new Color(212, 230, 245)); 
        UIManager.put("Table.selectionForeground", Color.BLACK);
        UIManager.put("Table.gridColor", new Color(220, 220, 220));

        UIManager.put("TableHeader.background", new Color(52, 73, 94));
        UIManager.put("TableHeader.foreground", Color.WHITE);

        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", new Color(50, 50, 50));
        UIManager.put("TextField.border", BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        UIManager.put("TextField.caretForeground", Color.BLACK);

        UIManager.put("Label.foreground", new Color(50, 50, 50));

        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.foreground", new Color(50, 50, 50));
        UIManager.put("ComboBox.selectionBackground", new Color(52, 152, 219));
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);

        UIManager.put("ScrollPane.background", new Color(245, 245, 245));
        UIManager.put("ScrollBar.thumb", new Color(180, 180, 180));
        UIManager.put("ScrollBar.track", new Color(230, 230, 230));

        UIManager.put("TitledBorder.titleColor", new Color(52, 73, 94));
        UIManager.put("TitledBorder.border", BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
