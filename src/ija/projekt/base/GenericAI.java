/**
 * IJA - projekt 2013
 * soubor: GenericAI.java
 * 
 * 
 * Autori:
 *        @author Michal Dobes (xdobes13)
 *        @author Jan Kalina   (xkalin03)
 */

package ija.projekt.base;
import ija.projekt.network.NetException;
import javax.swing.JOptionPane;

/**
 * Obecna umela inteligence
 * @author Jan Kalina <xkalin03@stud.fit.vutbr.cz>
 * @author Michal Dobes <xdobes13@stud.fit.vutbr.cz>
 */
public abstract class GenericAI
{    
    private Player player;
    private Desk desk;
    
    /**
     * Konstruktor
     * @param myplayer hráč, za kterého má být dosazeni AI
     * @param mydesk deska, na které bude AI hrát
     */
    public GenericAI(Player myplayer, Desk mydesk)
    {
        this.player = myplayer;
        myplayer.settype(Player.Type.AI);
        this.desk = mydesk;
    }
    
    /**
     * Získá hráče
     * @return hráč, za kterého AI hraje
     */
    public Player getPlayer()
    {
        return player;
    }
    
    /**
     * Provede tah
     */
    public void makeMove()
    {
        // Ziskat tah, provest ho a pridat do historie
        Move m = getMove();
        if (m == null)
        {
            desk.endGame();
            JOptionPane.showMessageDialog(Game.getWindow(), getName() + " byl poražen. Blahopřeji!",
               "Výhra!", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Konec hry.");
            // Odnastavit AI:
            desk.getWhitePlayer().settype(Player.Type.HUMAN);
            desk.getBlackPlayer().settype(Player.Type.HUMAN);
            Game.getWindow().update(); // aby se zmena projevila v UI
            return;
        }
        
        if (m.isCapture() == true)
        {
            m.getSource().getFigure().capture(m.getDestination());
            desk.getHistory().addCapture(m.getSource(), m.getDestination());
        }
        else
        {
            m.getSource().getFigure().move(m.getDestination());
            desk.getHistory().addMove(m.getSource(), m.getDestination());
        }
        
        if(desk.getNetLink()!=null){
            try{
                desk.getNetLink().sendMove(m.getSource(), m.getDestination());
            }
            catch(NetException e){
                System.err.println("Nezdarilo se sendMove() AI: "+e.toString());
            }
        }
    }
    
    /**
     * Vypočítá další tah
     * @return tah, který se má provést
     */
    public abstract Move getMove();
    
    /**
     * Vrátí jméno AI
     * @return jméno AI
     */
    public abstract String getName();
}
