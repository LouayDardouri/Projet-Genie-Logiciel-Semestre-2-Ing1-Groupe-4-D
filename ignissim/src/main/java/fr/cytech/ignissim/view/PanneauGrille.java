package fr.cytech.ignissim.view;

import javax.swing.*;
import java.awt.*;

public class PanneauGrille extends JPanel {
    private int lignes;
    private int colonnes;

    public PanneauGrille(int lignes, int colonnes) {
        this.lignes = lignes;
        this.colonnes = colonnes;
        // Fond sombre pour rester dans le thème tactique
        setBackground(new Color(15, 23, 42)); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Message temporaire en attendant de brancher le Modèle
        g2.setColor(new Color(100, 116, 139));
        g2.setFont(new Font("SansSerif", Font.ITALIC, 16));
        g2.drawString("Zone d'affichage de la carte 2D (En attente du moteur de simulation)", 50, 50);
    }
}