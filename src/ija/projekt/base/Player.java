

package ija.projekt.base;
import java.util.*;


public class Player {
    
    public enum Color {BLACK, WHITE};
    public enum Type {HUMAN, REMOTE, AI};
    private Color color;
    private Player opponent;
    private Type type;
    private ArrayList<Figure> figures;
    private GenericAI ai = null;
    
    
    public void setOpponent(Player p){
        opponent = p;
    }
    
    
    public Player opponent(){
        return opponent;
    }
    
    
    public Player(Color color){
        this.color = color;
        this.figures = new ArrayList<>();
    }
    
    
    public boolean isWhite(){
        return (color==Color.WHITE);
    }
    
    
    public boolean isBlack(){
        return (color==Color.BLACK);
    }
    
    
    public void settype(Type newtype)
    {
        type = newtype;
    }
    
    
    public Type type()
    {
        return type;
    }
    
    
    public void addFigure(Figure newfig)
    {
        figures.add(newfig);
    }
    
    
    public void remFigure(Figure delfig)
    {
        figures.remove(delfig);
    }
    
    
    public void purgeFigures()
    {
        figures.removeAll(figures);
    }
    
    
    public Integer cntFigures()
    {
        return figures.size();
    }
    
    
    public ArrayList<Figure> getFigures()
    {
        return figures;
    }
    
    
    public void setAI(GenericAI newAI)
    {
        ai = newAI;
    }
    
    
    public GenericAI getAI()
    {
        return ai;
    }
    
    
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
    
    
    public boolean loses()
    {
        return (cntFigures() == 0 || (this.isBlocked()));
    }
    
    
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
    
    
    @Override public boolean equals(Object o)
    {
	if (o instanceof Player)
	{
            Player other = (Player) o;
	    return (this.color == other.color);
	}
	return false;
    }
    
    
    @Override public int hashCode()
    {
	return (isWhite())? 1:0;
    }
}