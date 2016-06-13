

package ija.projekt.base;
import ija.projekt.network.NetException;
import javax.swing.JOptionPane;


public abstract class GenericAI
{    
    private Player player;
    private Desk desk;
    
    
    public GenericAI(Player myplayer, Desk mydesk)
    {
        this.player = myplayer;
        myplayer.settype(Player.Type.AI);
        this.desk = mydesk;
    }
    
    
    public Player getPlayer()
    {
        return player;
    }
    
    
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
    
    
    public abstract Move getMove();
    
    
    public abstract String getName();
}
