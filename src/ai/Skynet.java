

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
            
            
            return new Move(fig.getPosition(), targets.get(rand.nextInt(targets.size())), true);
        }
        
        
        
        
        
        
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
                        
                        backup_fig = fig;
                        backup_pos = p;
                        
                        
                        if (rand.nextInt(movesEstimate) == 0)
                        {
                            
                            return new Move(fig.getPosition(), p, false);
                        }
                    }
                }
            }
        }
        
        if (backup_fig == null || backup_pos == null)
        {
            System.out.println("Skynet: No se por donde ir");
            
            return null;
        }
        
        
        System.out.println("Skynet: Salto de emergencia. Esto no deberia suceder con frencuencia");
        
        return new Move(backup_fig.getPosition(), backup_pos, false);
    }
    

    
    @Override
    public String getName()
    {
        return "Skynet";
    }
}