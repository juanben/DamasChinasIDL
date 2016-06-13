

package ija.projekt.figures;
import ija.projekt.base.*;


public class Stone extends Figure {
    
    
    public Stone(Position position, Player player)
    {
        super(position, player);
    }
    
    
    @Override public boolean canMove(Position destination)
    {
        if (destination.getFigure() != null) 
            return false;
        
        int direction = ( getPosition().getDesk().getPlayer().isWhite() ? 1 : -1 );
        
        if(getPosition().nextPosition( 1, direction) == destination) return true; 
        if(getPosition().nextPosition(-1, direction) == destination) return true; 
        return false;
    }
    
    
    @Override public boolean canCapture()
    {
        int direction = ( getPosition().getDesk().getPlayer().isWhite() ? 1 : -1 );
        
        
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
        if (destination == null) return false; 
        if (destination.getFigure() != null) return false; 
        
        int direction = ( getPosition().getDesk().getPlayer().isWhite() ? 1 : -1 );
        
        for (int side = -1; side < 2; side += 2) 
        {
            if (getPosition().nextPosition(2*side, 2*direction) == destination)
            {
                
                Figure jumpedFig = getPosition().nextPosition(1*side, direction).getFigure();
                
                if ( jumpedFig == null )
                    return false; 
                else if ( jumpedFig.getPlayer() == this.getPlayer() )
                    return false; 
                else
                    return true; 
            }
        }
        return false;
    }
    
    
    @Override public void capture(Position destination)
    {
        
        Position stoneInWayPos = getPosition().getFirstStoneInWay(destination);
        if(stoneInWayPos == null)
            return; 
        
        stoneInWayPos.getFigure().getPlayer().remFigure(stoneInWayPos.getFigure());
        stoneInWayPos.removeFigure();
        stoneInWayPos.updateIcon();
        
        
        move(destination);
    }
}
