

package ija.projekt.figures;
import ija.projekt.base.*;


public class Queen extends Figure {
    
    
    public Queen(Position ps, Player pl)
    {
        super(ps, pl);
    }
    
    
    @Override public boolean canMove(Position destination)
    {
        if (destination.getFigure() != null) 
            return false;
      
        if (getPosition().getFirstStoneInWay(destination) != null)
            return false; 
        
        return getPosition().isDiagonalOf(destination);
    }
    
    
    @Override public boolean canCapture()
    {
        
        
        
        
        for (int dirx = -1; dirx<=1; dirx += 2)
            for (int diry = -1; diry<=1; diry += 2)
            {
                
                Position act = getPosition().nextPosition(2*dirx, 2*diry);
                while(act != null)
                {
                    if (canCapture(act))
                        return true;
                    
                    act = act.nextPosition(dirx, diry);
                }
            }
        
        return false;
    }

    
    @Override public boolean canCapture(Position destination)
    {
        
        if (destination.getFigure() != null)
            return false;
        
        
        if (!getPosition().isDiagonalOf(destination))
            return false;
        
        
        
        Position victim = getPosition().getFirstStoneInWay(destination);
        
        int opponentStones = 0;
        
        while (victim != null)
        {
            
            if (victim.getFigure().getPlayer() == getPosition().getDesk().getPlayer())
                return false;
            else
            {
                opponentStones ++;
            }
            
            victim = victim.getFigure().getPosition().getFirstStoneInWay(destination);
        }
        
        
        return opponentStones == 1; 
    }
    
    
    @Override public void capture(Position destination)
    {
        
        Position act;
        while ((act=getPosition().getFirstStoneInWay(destination)) != null)
        {
            
            if (act.getFigure().getPlayer() != this.getPlayer())
            {
                act.getFigure().getPlayer().remFigure(act.getFigure());
                act.removeFigure();
                act.updateIcon();
            }
        }
        
        
        move(destination);
    }
}
