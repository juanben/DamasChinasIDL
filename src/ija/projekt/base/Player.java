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
     * NastavenÃ­ hrÃ¡Ä�e protivnÃ­ka
     * @param p protivnÃ­k
     */
    public void setOpponent(Player p){
        opponent = p;
    }
    
    /**
     * HrÃ¡Ä� protivnÃ­k
     * @return protivnÃ­k
     */
    public Player opponent(){
        return opponent;
    }
    
    /**
     * Konstruktor hrÃ¡Ä�e
     * @param color barva za kterou hrÃ¡Ä� hraje
     */
    public Player(Color color){
        this.color = color;
        this.figures = new ArrayList<>();
    }
    
    /**
     * Hraje za bÃ­lou barvu?
     * @return Zdali hrÃ¡Ä� hraje za bÃ­lou barvu
     */
    public boolean isWhite(){
        return (color==Color.WHITE);
    }
    
    /**
     * Hraje za Ä�ernou barvu?
     * @return Zdali hrÃ¡Ä� hraje za Ä�ernou barvu
     */
    public boolean isBlack(){
        return (color==Color.BLACK);
    }
    
    /**
     * NastavenÃ­ typu hrÃ¡Ä�e
     * @param newtype typ hrÃ¡Ä�e
     */
    public void settype(Type newtype)
    {
        type = newtype;
    }
    
    /**
     * Typ hrÃ¡Ä�e
     * @return typ hrÃ¡Ä�e
     */
    public Type type()
    {
        return type;
    }
    
    /**
     * PÅ™idÃ¡nÃ­ figurky do seznamu figurek hrÃ¡Ä�e
     * @param newfig figurka
     */
    public void addFigure(Figure newfig)
    {
        figures.add(newfig);
    }
    
    /**
     * OdebrÃ¡nÃ­ figurky ze seznamu figurek hrÃ¡Ä�e
     * @param delfig figurka k odebrÃ¡nÃ­
     */
    public void remFigure(Figure delfig)
    {
        figures.remove(delfig);
    }
    
    /**
     * VyprÃ¡zdnÄ›nÃ­ seznamu figurek hrÃ¡Ä�e
     */
    public void purgeFigures()
    {
        figures.removeAll(figures);
    }
    
    /**
     * ZÃ­skÃ¡nÃ­ poÄ�tu figurek hrÃ¡Ä�e
     * @return poÄ�et figurek hrÃ¡Ä�e
     */
    public Integer cntFigures()
    {
        return figures.size();
    }
    
    /**
     * ZÃ­skÃ¡nÃ­ seznamu figurek hrÃ¡Ä�e (napÅ™. pro foreach)
     * @return seznam figurek hrÃ¡Ä�e
     */
    public ArrayList<Figure> getFigures()
    {
        return figures;
    }
    
    /**
     * NastavenÃ­ umÄ›lÃ© inteligence hrajÃ­cÃ­ za hrÃ¡Ä�e
     * @param newAI typ umÄ›lÃ© inteligence
     */
    public void setAI(GenericAI newAI)
    {
        ai = newAI;
    }
    
    /**
     * ZÃ­skÃ¡nÃ­ typu umÄ›lÃ© inteligence hrajÃ­cÃ­ za hrÃ¡Ä�e
     * @return typ umÄ›lÃ© inteligence
     */
    public GenericAI getAI()
    {
        return ai;
    }
    
    /**
     * Zdali je hrÃ¡Ä� zablokovanÃ½ - nemÅ¯Å¾e s niÄ�Ã­m nikam tÃ¡hnout
     * @return zdali je hrÃ¡Ä� zablokovanÃ½ (prohrÃ¡l)
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
     * ZjistÃ­, jestli hrÃ¡Ä� prohrÃ¡l
     * @return true pokud hrÃ¡Ä� prohrÃ¡l, jinak false
     */
    public boolean loses()
    {
        return (cntFigures() == 0 || (this.isBlocked()));
    }
    
    /**
     * ZÃ­skÃ¡nÃ­ figurky kterÃ¡ mÅ¯Å¾e brÃ¡t
     * @return figurka kterÃ¡ mÅ¯Å¾e brÃ¡t nebo null
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
     * Jestli majÃ­ hrÃ¡Ä�i stejnou barvu
     * @param o jinÃ½ hrÃ¡Ä�
     * @return zdali majÃ­ stejnou barvu
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
     * Hash pozice pro umoÅ¾nÄ›nÃ­ pouÅ¾itÃ­ HashMapy
     */
    @Override public int hashCode()
    {
	return (isWhite())? 1:0;
    }
}