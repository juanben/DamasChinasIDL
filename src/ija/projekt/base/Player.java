/**
 * IJA - projekt 2013
 * soubor: Player.java
 * Trida reprezentujici hrace.
 * 
 * Autori:
 *         @author Michal Dobes (xdobes13)
 *         @author Jan Kalina   (xkalin03)
 */

package ija.projekt.base;
import java.util.*;

/**
 * Hrac
 * @author Jan Kalina <xkalin03@stud.fit.vutbr.cz>
 * @author Michal Dobes <xdobes13@stud.fit.vutbr.cz>
 */
public class Player {
    
    public enum Color {BLACK, WHITE};
    public enum Type {HUMAN, REMOTE, AI};
    private Color color;
    private Player opponent;
    private Type type;
    private ArrayList<Figure> figures;
    private GenericAI ai = null;
    
    /**
     * Nastavení hráče protivníka
     * @param p protivník
     */
    public void setOpponent(Player p){
        opponent = p;
    }
    
    /**
     * Hráč protivník
     * @return protivník
     */
    public Player opponent(){
        return opponent;
    }
    
    /**
     * Konstruktor hráče
     * @param color barva za kterou hráč hraje
     */
    public Player(Color color){
        this.color = color;
        this.figures = new ArrayList<>();
    }
    
    /**
     * Hraje za bílou barvu?
     * @return Zdali hráč hraje za bílou barvu
     */
    public boolean isWhite(){
        return (color==Color.WHITE);
    }
    
    /**
     * Hraje za černou barvu?
     * @return Zdali hráč hraje za černou barvu
     */
    public boolean isBlack(){
        return (color==Color.BLACK);
    }
    
    /**
     * Nastavení typu hráče
     * @param newtype typ hráče
     */
    public void settype(Type newtype)
    {
        type = newtype;
    }
    
    /**
     * Typ hráče
     * @return typ hráče
     */
    public Type type()
    {
        return type;
    }
    
    /**
     * Přidání figurky do seznamu figurek hráče
     * @param newfig figurka
     */
    public void addFigure(Figure newfig)
    {
        figures.add(newfig);
    }
    
    /**
     * Odebrání figurky ze seznamu figurek hráče
     * @param delfig figurka k odebrání
     */
    public void remFigure(Figure delfig)
    {
        figures.remove(delfig);
    }
    
    /**
     * Vyprázdnění seznamu figurek hráče
     */
    public void purgeFigures()
    {
        figures.removeAll(figures);
    }
    
    /**
     * Získání počtu figurek hráče
     * @return počet figurek hráče
     */
    public Integer cntFigures()
    {
        return figures.size();
    }
    
    /**
     * Získání seznamu figurek hráče (např. pro foreach)
     * @return seznam figurek hráče
     */
    public ArrayList<Figure> getFigures()
    {
        return figures;
    }
    
    /**
     * Nastavení umělé inteligence hrající za hráče
     * @param newAI typ umělé inteligence
     */
    public void setAI(GenericAI newAI)
    {
        ai = newAI;
    }
    
    /**
     * Získání typu umělé inteligence hrající za hráče
     * @return typ umělé inteligence
     */
    public GenericAI getAI()
    {
        return ai;
    }
    
    /**
     * Zdali je hráč zablokovaný - nemůže s ničím nikam táhnout
     * @return zdali je hráč zablokovaný (prohrál)
     */
    public boolean isBlocked()
    {
        for (Figure fig : figures)
        {
            if (!fig.canCapture())
            {
                for (int i = 0; i < 8; i++)
                {
                    for (int j = 0; j < 8; j++)
                    {
                        Position p = fig.getPosition().getDesk().getPositionAt(i, j);
                        if (fig.canMove(p))
                        {
                            return false;
                        }
                    }
                }
            }
            else
            {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Zjistí, jestli hráč prohrál
     * @return true pokud hráč prohrál, jinak false
     */
    public boolean loses()
    {
        return (cntFigures() == 0 || (this.isBlocked()));
    }
    
    /**
     * Získání figurky která může brát
     * @return figurka která může brát nebo null
     */
    public Figure getCaptureFigure()
    {
        for (Figure fig : figures)
        {
            if (fig.canCapture())
            {
                return fig;
            }
        }
        return null;
    }
    
    /**
     * Jestli mají hráči stejnou barvu
     * @param o jiný hráč
     * @return zdali mají stejnou barvu
     */
    @Override public boolean equals(Object o)
    {
	if (o instanceof Player)
	{
            Player other = (Player) o;
	    return (this.color == other.color);
	}
	return false;
    }
    
    /**
     * Hash pozice pro umožnění použití HashMapy
     */
    @Override public int hashCode()
    {
	return (isWhite())? 1:0;
    }
}