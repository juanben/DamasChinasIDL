

package base;
import javax.swing.*;

import figuras.*;
import red.*;


public final class Position extends JButton {
    
    
    private static ImageIcon blackIcon;
    private static ImageIcon black2Icon;
    private static ImageIcon whiteIcon;
    private static ImageIcon white2Icon;
    
    private int x, y; 
    private Desk desk; 
    private Figure figure; 
    
    
    static {
        Position.blackIcon = new ImageIcon(Position.class.getResource("/ija/projekt/images/black.png"));
        Position.black2Icon = new ImageIcon(Position.class.getResource("/ija/projekt/images/black2.png"));
        Position.whiteIcon = new ImageIcon(Position.class.getResource("/ija/projekt/images/white.png"));
        Position.white2Icon = new ImageIcon(Position.class.getResource("/ija/projekt/images/white2.png"));
    }
    
    
    public Position(Desk desk, int x, int y)
    {
        this.desk = desk;
        this.x = x;
        this.y = y;
        this.figure = null; 
        
        
        addActionListener(new java.awt.event.ActionListener(){
          @Override public void actionPerformed(java.awt.event.ActionEvent evt){
            ((Position)evt.getSource()).onClick();
          }
        });
        
        setFocusPainted(false);
        setBorder(null);
    }
    
    
    public boolean isPositionBlack()
    {
        return ((x+y)%2==0);
    }
    
    
    public void updateBackground()
    {
        if(desk.selected()==this){
            setBackground(java.awt.Color.yellow);
        }else if(desk.isHelpEnabled() &&
                 desk.selected()!=null &&
                 desk.selected().getFigure()!=null &&
                 desk.selected().getFigure().canCapture(this)
                ){
            setBackground(java.awt.Color.red);
        }else if(desk.isHelpEnabled() &&
                 desk.selected()!=null &&
                 desk.selected().getFigure()!=null &&
                 desk.selected().getFigure().canMove(this) &&
                 desk.selected().getFigure().getPlayer().getCaptureFigure()==null
          ){
            setBackground(java.awt.Color.green);
        }else if(desk.isHelpEnabled() &&
                    desk.selected()==null &&
                    this.getFigure()!=null &&
                    this.getFigure().getPlayer() == desk.getPlayer() &&
                    this.getFigure().getPlayer().type() == Player.Type.HUMAN &&
                    desk.getHistory().isPlayingHistory() == false &&
                    desk.getHistory().inPresent() &&
                    ( 
                        this.getFigure().canCapture() ||
                        (
                            this.getFigure().canMove() &&
                            desk.getPlayer().getCaptureFigure()==null
                        )
                    )
          ){
            setBackground(new java.awt.Color(0x55,0x55,0xFF));
        }else{
            if(isPositionBlack()){
                setBackground(java.awt.Color.gray);
            }else{
                setBackground(java.awt.Color.lightGray);
            }
        }
    }
    
    
    public void placeFigure(Figure figure)
    {
        this.figure = figure;
        updateIcon();
    }
    
    
    public Figure getFigure()
    {
	return figure;
    }
    
    
    public Figure removeFigure()
    {
        Figure old = figure;
        figure = null;
        return old;
    }

    
    public void updateIcon()
    {
        if(figure == null){
            setIcon(null);
        }else if(figure instanceof Queen){
            if(figure.getPlayer().isBlack()){
                setIcon(black2Icon);
            }else{
                setIcon(white2Icon);
            }
        }else if(figure instanceof Stone){
            if(figure.getPlayer().isBlack()){
                setIcon(blackIcon);
            }else{
                setIcon(whiteIcon);
            }
        }
    }
    
    
    public void select()
    {
        if(figure == null || figure.getPlayer() != desk.getPlayer()){
            
            desk.select(null);
        }else{
            desk.select(this);
        }
        desk.updateAllBackgrounds();
    }
    
    
    public void tryChangeStoneToQueen()
    {
        if(
            (figure.getPlayer().isWhite() && y==7) ||
            (figure.getPlayer().isBlack() && y==0)
        ){
            figure.getPlayer().remFigure(figure);
            figure = new Queen(figure.getPosition(),figure.getPlayer());
            this.updateIcon();
        }
    }
    
    
    public void onClick()
    {
        if (desk.isGameEnded())
        {
            JOptionPane.showMessageDialog(Game.getWindow(), "El juego finalzo y se felicita a los jugadores"
                    + "\nPara un nuevo juego seleccion nuevo juego",
               "No se puede reproducir", JOptionPane.INFORMATION_MESSAGE);
            System.err.println("Juego termino");
            return;
        }
        
        
        if (!desk.getHistory().inPresent())
        {
            JOptionPane.showMessageDialog(Game.getWindow(), "Estas tratando de cambiar la historia, no es posible",
               " ", JOptionPane.INFORMATION_MESSAGE);
            System.err.println("Los usuario no pueden jugar se mueve la historia");
            return;
        }
        
        
        if (desk.getPlayer().type() != Player.Type.HUMAN)
        {
            JOptionPane.showMessageDialog(Game.getWindow(), "Usted no esta en el movimiento",
               "Ahora es el turno del segundo jugador", JOptionPane.INFORMATION_MESSAGE);
            System.err.println("Los usuario no pueden jugar, girar al segundo jugador");
            return;
        }
        
        
        Figure capturingFigure = desk.getPlayer().getCaptureFigure();
        
        
        if( desk.selected() == null && this.getFigure() != null ){
            
            
            if (capturingFigure != null && this.getFigure().canCapture() == false) {
                
                
                getDesk().setTempHelp(true); 
                
                
                capturingFigure.getPosition().select();
                System.out.println("El seleccionado no puede tirar, escoge el que pueda hacerlo"+capturingFigure.getPosition());
                
            }else{
                
                this.select();
            }
            
        
        }else if(desk.selected()!=null && desk.selected().getFigure()!=null){
            Figure selFigure = desk.selected().getFigure();
            
            
            if( (!selFigure.canMove(this) && !selFigure.canCapture(this)) && !desk.isRealHelpEnabled() ){
                if (desk.isTempHelpEnabled())
                {
                    desk.setTempHelp(false);
                    desk.clearSelected();
                    return;
                }
                
                desk.setTempHelp(true);
                desk.updateAllBackgrounds();
                
                return;
            }
            
            
            if (capturingFigure != null){
                
                
                if (selFigure.canCapture(this)){ 
                    
                    
                    desk.selected().getFigure().capture(this);
                    desk.getHistory().addCapture(desk.selected(), this);
                    
                    if(desk.getNetLink()!=null){
                        try{
                            desk.getNetLink().sendMove(desk.selected(),this);
                        }
                        catch(NetException e){
                            System.err.println("Fracaso sendMove(): "+e.toString());
                        }
                    }
                    
                    getDesk().setTempHelp(false);
                    desk.nextPlayer();
                }
                
                
                else if (this != desk.selected() && !(desk.isHelpEnabled())){
                    
                    if(!selFigure.canCapture()){
                        desk.select(capturingFigure.getPosition());
                    }
                    
                    getDesk().setTempHelp(true);
                    getDesk().updateAllBackgrounds();
                    return;
                }
            }
            
            
            else{
                
                if (desk.selected().getFigure().canMove(this))
                {
                    
                    desk.selected().getFigure().move(this);
                    desk.getHistory().addMove(desk.selected(), this);
                    
                    if(desk.getNetLink()!=null){
                        try{
                            desk.getNetLink().sendMove(desk.selected(),this);
                        }
                        catch(NetException e){
                            System.err.println("Fracaso sendMove(): "+e.toString());
                        }
                    }
                    getDesk().setTempHelp(false);
                    desk.nextPlayer();
                }
                else if (!getDesk().isHelpEnabled() && this != desk.selected())
                {
                    
                    getDesk().setTempHelp(true);
                    getDesk().updateAllBackgrounds();
                    return;
                }
            }
            desk.clearSelected();
            getDesk().setTempHelp(false);
            
            
            
            
            
            
            if ( !desk.isGameEnded() 
                    && getDesk().getWhitePlayer().loses() && desk.getPlayer().isWhite())
            {
                getDesk().endGame();
                JOptionPane.showMessageDialog(Game.getWindow(), "El jugador negro gana felicitaciones",
               "Final del juego", JOptionPane.INFORMATION_MESSAGE);
            }
            
            if (!desk.isGameEnded()
                    && getDesk().getBlackPlayer().loses() && getDesk().getHistory().getCurrent() != -1
                    && desk.getPlayer().isBlack())
            {
                System.out.println("History: " + getDesk().getHistory().getCurrent());
                getDesk().endGame();
                JOptionPane.showMessageDialog(Game.getWindow(), "El jugador blanco gana felicitaciones",
               "Final del juego", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    
    @Override public String toString()
    {
        char col = (char)((int)'a'+this.x);
        int row = this.y+1;
        return col + Integer.toString(row);
    }
    
    
    public void resetPosition()
    {
        if(isPositionBlack()){
            if(y<3){
                figure = new Stone(this, desk.getWhitePlayer());
            }else if(y>4){
                figure = new Stone(this, desk.getBlackPlayer());
            }else{
                figure = null;
            }
        }else{
            figure = null;
        }
        updateIcon();
        updateBackground();
    }
    
    
    public Desk getDesk()
    {
        return desk;
    }
    
    
    public byte getColumn(){
        return (byte)x;
    }
    
    
    public byte getRow(){
        return (byte)y;
    }
    
    
    public Position nextPosition(int dx, int dy)
    {
	return desk.getPositionAt(this.x + dx, this.y + dy);
    }
    
    
    public boolean isDiagonalOf(Position p)
    {
        return (Math.abs(p.x - this.x) == Math.abs(p.y - this.y));
    }


    
    public Position getFirstStoneInWay(Position destination)
    {
        if (!this.isDiagonalOf(destination))
            return null;
        
        
        int xinc = ( this.x < destination.x ? 1 : -1 );
        int yinc = ( this.y < destination.y ? 1 : -1 );
        
        Position curr = this;
        while( (curr = curr.nextPosition(xinc, yinc)) != destination ){
            Figure figureOfCurrent = curr.getFigure();
            if(figureOfCurrent != null) return figureOfCurrent.getPosition();
        }
        return null;
    }
    
    
    @Override public boolean equals(Object o)
    {
	if (o instanceof Position)
	{
	    Position p = (Position) o;
	    return (p.y == this.y && p.x == this.x);
	}
	return false;
    }
    
    
    @Override public int hashCode()
    {
	return (x + y * 10);
    }
}