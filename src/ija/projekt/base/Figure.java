

package ija.projekt.base;


public abstract class Figure {
    
    private Position position; // pozice figurky
    private Player player; // barva figurky
    
    
    public Figure(Position position, Player player)
    {
        this.position = position;
        this.player = player;
        player.addFigure(this);
    }
    
    
    public Player getPlayer()
    {
        return player;
    }
    
    
    public Position getPosition()
    {
	return position;
    }
    
    
    public boolean isAtPosition(Position p)
    {
	return position.equals(p);
    }
    
    
    public void move(Position destination)
    {
        position.removeFigure();
        position.updateIcon();
        destination.placeFigure(this);
        destination.updateIcon();
        this.position = destination;
        
        destination.tryChangeStoneToQueen();
    }
    
    
    public boolean canMove()
    {
        for( int x=0 ; x<8 ; x++ ){
            for( int y=0 ; y<8 ; y++ ){
                if(canMove(position.getDesk().getPositionAt(x,y))){
                    return true;
                }
            }
        }
        return false;
    }
    
    
    public abstract boolean canMove(Position destination);
    
    
    public abstract boolean canCapture();
    
    
    public abstract boolean canCapture(Position destination);
    
    
    public abstract void capture(Position dest);
    
}