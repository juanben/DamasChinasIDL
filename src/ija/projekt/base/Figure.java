/**
 * IJA - projekt 2013
 * soubor: Figure.java
 * Implementace herni figurky
 * 
 * Autori:
 *         @author Michal Dobes (xdobes13)
 *         @author Jan Kalina   (xkalin03)
 */

package ija.projekt.base;

/**
 * Figurka
 * (spolecny predek kamene a damy)
 * @author Jan Kalina <xkalin03@stud.fit.vutbr.cz>
 * @author Michal Dobes <xdobes13@stud.fit.vutbr.cz>
 */
public abstract class Figure {
    
    private Position position; // pozice figurky
    private Player player; // barva figurky
    
    /**
     * Konstuktor
     * @param position Pozice na které figurka leží
     * @param player Hráč kterému figurka patří
     */
    public Figure(Position position, Player player)
    {
        this.position = position;
        this.player = player;
        player.addFigure(this);
    }
    
    /**
     * Získá hráče, kterámu figurka patří
     * @return hráč, kterému figurka patří
     */
    public Player getPlayer()
    {
        return player;
    }
    
    /**
     * Získá pozici, na které se figurka nachází
     * @return Pozice, na které se figurka nachází.
     */
    public Position getPosition()
    {
	return position;
    }
    
    /**
     * Zjistí, jestli je figurka na dané pozici.
     * @param p zkoumaná pozice
     * @return true pokud ano, false pokud ne.
     */
    public boolean isAtPosition(Position p)
    {
	return position.equals(p);
    }
    
    /**
     * Provede tah figurkou
     * @param destination cílové pole
     */
    public void move(Position destination)
    {
        position.removeFigure();
        position.updateIcon();
        destination.placeFigure(this);
        destination.updateIcon();
        this.position = destination;
        
        destination.tryChangeStoneToQueen();
    }
    
    /**
     * Muze kamen nekam skocit?
     */
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
    
    /**
     * Muze se kamen presunout na pozici destination?
     */
    public abstract boolean canMove(Position destination);
    
    /**
     * Muze kamen nekoho vyhodit?
     */
    public abstract boolean canCapture();
    
    /**
     * Muze se kamen na pozici destination dostat vyhozenim jine figurky?
     */
    public abstract boolean canCapture(Position destination);
    
    /**
     * Presun figurky s vyhozenim figurky uprostred
     * Vola se az po schvaleni kontrolou canCapture(Position)
     */
    public abstract void capture(Position dest);
    
}