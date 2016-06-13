

package ija.projekt.figures;
import ija.projekt.base.*;


public class Stone extends Figure {
    
    
    public Stone(Position position, Player player)
    {
        super(position, player);
    }
    
    
    @Override public boolean canMove(Position destination)
    {
        if (destination.getFigure() != null) // Na cilovem policku uz nekdo je
            return false;
        
        int direction = ( getPosition().getDesk().getPlayer().isWhite() ? 1 : -1 );
        
        if(getPosition().nextPosition( 1, direction) == destination) return true; // O 1 vpravo
        if(getPosition().nextPosition(-1, direction) == destination) return true; // O 1 vlevo
        return false;
    }
    
    
    @Override public boolean canCapture()
    {
        int direction = ( getPosition().getDesk().getPlayer().isWhite() ? 1 : -1 );
        
        // Pole pozic na ktere se odtud muzeme presunout
        Position[] destinations = new Position[2];
        destinations[0] = getPosition().nextPosition( 2, 2*direction);
        destinations[1] = getPosition().nextPosition(-2, 2*direction);
        
        for(int i=0; i<destinations.length; i++){
            if(canCapture(destinations[i])){
                return true;
            }
        }
        return false;
    }

    
    @Override public boolean canCapture(Position destination)
    {
        if (destination == null) return false; // cilove pole neexistuje
        if (destination.getFigure() != null) return false; // cilove pole obsazene
        
        int direction = ( getPosition().getDesk().getPlayer().isWhite() ? 1 : -1 );
        
        for (int side = -1; side < 2; side += 2) // Jednou vpravo, jednou vlevo
        {
            if (getPosition().nextPosition(2*side, 2*direction) == destination)
            {
                // preskakovana, tedy vyhazovana, figurka
                Figure jumpedFig = getPosition().nextPosition(1*side, direction).getFigure();
                
                if ( jumpedFig == null )
                    return false; // Neskaceme pres zadnou figurku
                else if ( jumpedFig.getPlayer() == this.getPlayer() )
                    return false; // Skaceme pres vlastni figurku
                else
                    return true; // Skaceme pres cizi figurku - muzeme ji vyhodit
            }
        }
        return false;
    }
    
    
    @Override public void capture(Position destination)
    {
        // Vyhozeni
        Position stoneInWayPos = getPosition().getFirstStoneInWay(destination);
        if(stoneInWayPos == null)
            return; // TODO raise exception???
        
        stoneInWayPos.getFigure().getPlayer().remFigure(stoneInWayPos.getFigure());
        stoneInWayPos.removeFigure();
        stoneInWayPos.updateIcon();
        
        // Presun
        move(destination);
    }
}
