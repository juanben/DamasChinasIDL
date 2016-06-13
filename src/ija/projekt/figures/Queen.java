

package ija.projekt.figures;
import ija.projekt.base.*;


public class Queen extends Figure {
    
    
    public Queen(Position ps, Player pl)
    {
        super(ps, pl);
    }
    
    
    @Override public boolean canMove(Position destination)
    {
        if (destination.getFigure() != null) // Na cilovem policku uz nekdo je
            return false;
      
        if (getPosition().getFirstStoneInWay(destination) != null)
            return false; // Preskakovali bychom kameny
        
        return getPosition().isDiagonalOf(destination);
    }
    
    
    @Override public boolean canCapture()
    {
        // Musim volat canCapture(Position p) na vsechna pole na diagovalach
        // krome poli tesne vedle.
        
        // Pro vsechny 4 diagonalni smery
        for (int dirx = -1; dirx<=1; dirx += 2)
            for (int diry = -1; diry<=1; diry += 2)
            {
                // Tesne okoli testovat nemusime
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
        // Je cilove policko volne?
        if (destination.getFigure() != null)
            return false;
        
        // Je to po diagonale??
        if (!getPosition().isDiagonalOf(destination))
            return false;
        
        
        // A je mezi mnou a cilovym polickem protihracuv kamen?
        Position victim = getPosition().getFirstStoneInWay(destination);
        
        int opponentStones = 0;
        
        while (victim != null)
        {
            // Je na draze skoku figurka skakajiciho hrace? Pak nemuze skakat...
            if (victim.getFigure().getPlayer() == getPosition().getDesk().getPlayer())
                return false;
            else
            {
                opponentStones ++;
            }
            
            victim = victim.getFigure().getPosition().getFirstStoneInWay(destination);
        }
        
        //return opponentStones != 0; // vic kamenu
        return opponentStones == 1; // 1 kamen
    }
    
    
    @Override public void capture(Position destination)
    {
        // Vyhozeni vsech protihracovych kamenu v ceste.
        Position act;
        while ((act=getPosition().getFirstStoneInWay(destination)) != null)
        {
            // Vyhodit pouze protihracovy kameny
            if (act.getFigure().getPlayer() != this.getPlayer())
            {
                act.getFigure().getPlayer().remFigure(act.getFigure());
                act.removeFigure();
                act.updateIcon();
            }
        }
        
        // Presun
        move(destination);
    }
}
