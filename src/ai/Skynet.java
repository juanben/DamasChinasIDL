

package ai;
import java.util.ArrayList;
import java.util.Random;

import ija.projekt.base.*;


public class Skynet extends GenericAI
{
    Random rand;
    
     
    public Skynet(Player myplayer, Desk mydesk)
    {
        super(myplayer, mydesk);
        rand = new Random();
    }
    
    
    @Override
    public Move getMove()
    {
        ArrayList<Figure> canCapture = new ArrayList<>();
        for (Figure fig : getPlayer().getFigures())
        {
            if (fig.canCapture())
            {
                canCapture.add(fig);
            }
        }
        
        if (!canCapture.isEmpty())
        {
            // Nahodne vybrat figurku, vzit s ni neco
            Figure fig = canCapture.get(rand.nextInt(canCapture.size()));
            ArrayList<Position> targets = new ArrayList<>();
            
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    Position p = fig.getPosition().getDesk().getPositionAt(i, j);
                    if (fig.canCapture(p))
                    {
                        targets.add(p);
                    }
                }
            }
            
            // Vratit skok
            return new Move(fig.getPosition(), targets.get(rand.nextInt(targets.size())), true);
        }
        // Pokud zadna figurka nemuze brat, vytvorime seznam 
        // figurek, ktere se mohou pohnout
        
        // Figurkou pohneme jen s urcitou pravdepodobnosti.
        // Muze se tedy stat, ze nepohneme zadnou figurkou. 
        // Proto musime mit zalozni tah (posledni zkoumany)
        Figure backup_fig = null;
        Position backup_pos = null;
        
        int figuresCnt = getPlayer().cntFigures();
        int movesEstimate = figuresCnt/3+1;
        
        for (Figure fig : getPlayer().getFigures())
        {
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    Position p = fig.getPosition().getDesk().getPositionAt(i, j);
                    if (fig.canMove(p))
                    {
                        // pridat jako backup:
                        backup_fig = fig;
                        backup_pos = p;
                        
                        // Skocit s nejakou rozumnou pravdepodobnosti
                        if (rand.nextInt(movesEstimate) == 0)
                        {
                            // Vratit skok
                            return new Move(fig.getPosition(), p, false);
                        }
                    }
                }
            }
        }
        
        if (backup_fig == null || backup_pos == null)
        {
            System.out.println("Skynet: Sakra, nemám kam sko�?it. Asi jsem poražen. Hmm...");
            // ukonceni hry s vrelymi gratulacemi uzivateli
            return null;
        }
        
        // Nouzovy skok:
        System.out.println("Skynet: Nouzový skok. Toto by se nemělo stávat příliš �?asto.");
        
        return new Move(backup_fig.getPosition(), backup_pos, false);
    }
    

    
    @Override
    public String getName()
    {
        return "Skynet";
    }
}