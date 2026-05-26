package fr.cytech.ignissim;

import javax.swing.SwingUtilities;
import fr.cytech.ignissim.view.FenetrePrincipale;

public class Main {
    public static void main(String[] args) {
        // Swing n'est pas "Thread-Safe", on utilise SwingUtilities pour éviter les bugs graphiques
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // On crée une grille de 30 lignes sur 40 colonnes pour tester
                FenetrePrincipale stationTactique = new FenetrePrincipale(30, 40);
                
                // Rend la fenêtre visible à l'écran
                stationTactique.setVisible(true);
            }
        });
    }
}