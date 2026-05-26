package fr.cytech.ignissim.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class FenetrePrincipale extends JFrame {

    // Composants de l'interface (VUE)
    private PanneauGrille panneauGrille; // Zone d'affichage de la carte 2D
    private JButton btnPlay, btnPause, btnReset, btnSuivant;
    private JButton btnSauvegarder, btnCharger;
    private JSlider sliderVentVitesse, sliderPente;
    private JComboBox<String> comboVentDirection;
    
    // Zone d'affichage des statistiques en direct
    private JLabel lblCellulesSaines, lblCellulesEnFeu, lblCellulesCendres;

    public FenetrePrincipale(int lignes, int colonnes) {
        // Configuration de la fenêtre principale (Thème sombre tactique)
        setTitle("IgnisSim - Station Tactique d'Aide à la Décision");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(15, 23, 42)); // Couleur Slate 900
        setLayout(new BorderLayout(10, 10));

        // 1. BARRE DE TITRE ET STATISTIQUES (HAUT)
        JPanel panneauHaut = new JPanel(new BorderLayout());
        panneauHaut.setBackground(new Color(30, 41, 59)); // Slate 800
        panneauHaut.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitre = new JLabel("IGNISSIM v1.0");
        lblTitre.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitre.setForeground(new Color(234, 88, 12)); // Orange incendie
        panneauHaut.add(lblTitre, BorderLayout.WEST);

        // Panel des statistiques en direct (Demandé dans le PDF)
        JPanel panneauStats = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        panneauStats.setOpaque(false);
        
        lblCellulesSaines = new JLabel("Saines : 100%");
        lblCellulesSaines.setForeground(new Color(34, 197, 94)); // Vert
        lblCellulesEnFeu = new JLabel("En Feu : 0");
        lblCellulesEnFeu.setForeground(new Color(239, 68, 68)); // Rouge
        lblCellulesCendres = new JLabel("Cendres : 0");
        lblCellulesCendres.setForeground(new Color(100, 116, 139)); // Gris

        panneauStats.add(lblCellulesSaines);
        panneauStats.add(lblCellulesEnFeu);
        panneauStats.add(lblCellulesCendres);
        panneauHaut.add(panneauStats, BorderLayout.EAST);

        add(panneauHaut, BorderLayout.NORTH);

        // 2. CENTRE : LA CARTE DES INCENDIES (Matrice 2D)
        panneauGrille = new PanneauGrille(lignes, colonnes);
        panneauGrille.setBorder(BorderFactory.createLineBorder(new Color(234, 88, 12), 2));
        add(panneauGrille, BorderLayout.CENTER);

        // 3. DROITE : PANNEAU DE CONTRÔLE TACTIQUE (Loi de Fitts & Paramètres)
        JPanel panneauControle = new JPanel();
        panneauControle.setLayout(new BoxLayout(panneauControle, BoxLayout.Y_AXIS));
        panneauControle.setBackground(new Color(30, 41, 59));
        panneauControle.setPreferredSize(new Dimension(320, 720));
        panneauControle.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // --- SECTION ACTIONS (Boutons larges pour utilisation avec gants) ---
        JLabel lblActions = new JLabel("CONTRÔLES SIMULATION");
        lblActions.setForeground(Color.WHITE);
        lblActions.setFont(new Font("SansSerif", Font.BOLD, 14));
        panneauControle.add(lblActions);
        panneauControle.add(Box.createVerticalStrut(10));

        btnPlay = creerBoutonLarge("LANCER", new Color(34, 197, 94));
        btnPause = creerBoutonLarge("PAUSE", new Color(234, 88, 12));
        btnSuivant = creerBoutonLarge("PAS SUIVANT", new Color(59, 130, 246));
        btnReset = creerBoutonLarge("RÉINITIALISER", new Color(239, 68, 68));

        panneauControle.add(btnPlay);
        panneauControle.add(Box.createVerticalStrut(8));
        panneauControle.add(btnPause);
        panneauControle.add(Box.createVerticalStrut(8));
        panneauControle.add(btnSuivant);
        panneauControle.add(Box.createVerticalStrut(8));
        panneauControle.add(btnReset);

        panneauControle.add(Box.createVerticalStrut(25));
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(300, 2));
        panneauControle.add(sep);
        panneauControle.add(Box.createVerticalStrut(20));

        // --- SECTION PARAMÈTRES ENVIRONNEMENTAUX ---
        JLabel lblParams = new JLabel("PARAMÈTRES PHYSIQUES");
        lblParams.setForeground(Color.WHITE);
        lblParams.setFont(new Font("SansSerif", Font.BOLD, 14));
        panneauControle.add(lblParams);
        panneauControle.add(Box.createVerticalStrut(15));

        // Curseur de force du Vent
        panneauControle.add(new JLabel("Force du Vent (km/h) :", JLabel.LEFT));
        sliderVentVitesse = new JSlider(0, 100, 20);
        configurerSlider(sliderVentVitesse);
        panneauControle.add(sliderVentVitesse);
        panneauControle.add(Box.createVerticalStrut(10));

        // Direction du Vent
        panneauControle.add(new JLabel("Direction du Vent :"));
        String[] directions = {"NORD", "SUD", "EST", "OUEST"};
        comboVentDirection = new JComboBox<>(directions);
        comboVentDirection.setMaximumSize(new Dimension(280, 35));
        panneauControle.add(comboVentDirection);
        panneauControle.add(Box.createVerticalStrut(15));

        // Curseur de Pente
        panneauControle.add(new JLabel("Inclinaison de la Pente (%) :"));
        sliderPente = new JSlider(-50, 50, 0);
        configurerSlider(sliderPente);
        panneauControle.add(sliderPente);

        panneauControle.add(Box.createVerticalGlue()); // Pousse le reste vers le bas

        // --- SECTION SAUVEGARDE (Sérialisation binaire) ---
        btnSauvegarder = new JButton("Exporter (.bin)");
        btnCharger = new JButton("Importer (.bin)");
        JPanel panneauFichier = new JPanel(new GridLayout(1, 2, 10, 0));
        panneauFichier.setOpaque(false);
        panneauFichier.setMaximumSize(new Dimension(280, 40));
        panneauFichier.add(btnSauvegarder);
        panneauFichier.add(btnCharger);
        panneauControle.add(panneauFichier);

        add(panneauControle, BorderLayout.EAST);
    }

    // Méthode utilitaire pour appliquer le style "Loi de Fitts" (Gros boutons faciles à cliquer)
    private JButton creerBoutonLarge(String texte, Color couleurFond) {
        JButton bouton = new JButton(texte);
        bouton.setMaximumSize(new Dimension(280, 45)); // Boutons larges et hauts
        bouton.setFont(new Font("SansSerif", Font.BOLD, 14));
        bouton.setBackground(couleurFond);
        bouton.setForeground(Color.WHITE);
        bouton.setFocusPainted(false);
        bouton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return bouton;
    }

    private void configurerSlider(JSlider slider) {
        slider.setOpaque(false);
        slider.setMajorTickSpacing(25);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setForeground(Color.LIGHT_GRAY);
        slider.setMaximumSize(new Dimension(280, 50));
    }

    // --- EN COOPÉRATION AVEC LE CONTRÔLEUR (MVC) ---
    // Ces méthodes permettent au Contrôleur d'enregistrer les actions sans que la Vue ne fasse de calculs
    
    public void enregistrerEcouteurPlay(ActionListener ecouteur) {
        btnPlay.addActionListener(ecouteur);
    }

    public void enregistrerEcouteurPause(ActionListener ecouteur) {
        btnPause.addActionListener(ecouteur);
    }

    public void enregistrerEcouteurSuivant(ActionListener ecouteur) {
        btnSuivant.addActionListener(ecouteur);
    }

    public void enregistrerEcouteurReset(ActionListener ecouteur) {
        btnReset.addActionListener(ecouteur);
    }

    public void enregistrerEcouteurSauvegarde(ActionListener ecouteur) {
        btnSauvegarder.addActionListener(ecouteur);
    }

    public void enregistrerEcouteurChargement(ActionListener ecouteur) {
        btnCharger.addActionListener(ecouteur);
    }

    // Permet de mettre à jour les statistiques en direct depuis le contrôleur
    public void mettreAJourStats(double sainPct, int enFeu, int cendres) {
        lblCellulesSaines.setText(String.format("Saines : %.1f%%", sainPct));
        lblCellulesEnFeu.setText("En Feu : " + enFeu);
        lblCellulesCendres.setText("Cendres : " + cendres);
    }

    public PanneauGrille getPanneauGrille() {
        return panneauGrille;
    }
}