

package ija.projekt.base;
import ija.projekt.gui.GameWindow;


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
    
    
    public static GameWindow getWindow()
    {
        return gw;
    }

}
