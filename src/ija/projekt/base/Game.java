/**
 * IJA - projekt 2013
 * soubor: Game.java
 * Hlavni trida hry.
 * 
 * Autori:
 *         @author Michal Dobes (xdobes13)
 *         @author Jan Kalina   (xkalin03)
 */

package ija.projekt.base;
import ija.projekt.gui.GameWindow;

/**
 * Spousteci trida hry
 * @author Jan Kalina <xkalin03@stud.fit.vutbr.cz>
 * @author Michal Dobes <xdobes13@stud.fit.vutbr.cz>
 */
public class Game extends java.applet.Applet {
    
    private static GameWindow gw;
    
    public void init() {
        gw = new GameWindow();
        gw.setVisible(true);
    }
    
    public static void main(String[] args) {
        gw = new GameWindow();
        gw.setVisible(true);
    }
    
    /**
     * Získá hlavní okno GUI
     * @return okno GUI
     */
    public static GameWindow getWindow()
    {
        return gw;
    }

}
