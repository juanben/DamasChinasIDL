

package ija.projekt.base;
import ija.projekt.figures.*;
import javax.swing.*;
import ija.projekt.network.*;


public final class Position extends JButton {
    
    // ikony hernich kamenu spolecne pro vsechna Position
    private static ImageIcon blackIcon;
    private static ImageIcon black2Icon;
    private static ImageIcon whiteIcon;
    private static ImageIcon white2Icon;
    
    private int x, y; // sloupec a radek pozice
    private Desk desk; // odkaz na hraci plochu
    private Figure figure; // kamen na teto pozici
    
    
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
        this.figure = null; // pole je na zacatku prazdne
        
        // pri kliknuti na tlacitko pozice zavolat metodu Position.onClick()
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
                    ( // pokud s touto figurkou muze brat nebo tahnout
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
            // Figurka tam bud neni, nebo nepatri hraci na tahu
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
            JOptionPane.showMessageDialog(Game.getWindow(), "Hra jiÅ¾ skonÄ�ila a vÃ­tÄ›zi bylo pogratulovÃ¡no."
                    + "\nPro novou hru zvolte Hra -> NovÃ¡ Hra.",
               "Nelze hrÃ¡t", JOptionPane.INFORMATION_MESSAGE);
            System.err.println("Hra jiÅ¾ skonÄ�ila!");
            return;
        }
        
        // Jsme vubec v soucasnosti?
        if (!desk.getHistory().inPresent())
        {
            JOptionPane.showMessageDialog(Game.getWindow(), "SnaÅ¾Ã­te se zmÄ›nit historii. To nenÃ­ moÅ¾nÃ©.",
               "Jste vÄ�erejÅ¡Ã­", JOptionPane.INFORMATION_MESSAGE);
            System.err.println("UÅ¾ivatel nemÅ¯Å¾e hrÃ¡t, pohybuje se v historii!");
            return;
        }
        
        // Jsme vubec na tahu?
        if (desk.getPlayer().type() != Player.Type.HUMAN)
        {
            JOptionPane.showMessageDialog(Game.getWindow(), "Nejste na tahu",
               "NynÃ­ je na tahu druhÃ½ hrÃ¡Ä�!", JOptionPane.INFORMATION_MESSAGE);
            System.err.println("UÅ¾ivatel nemÅ¯Å¾e hrÃ¡t, na tahu je druhÃ½ hrÃ¡Ä�!");
            return;
        }
        
        
        Figure capturingFigure = desk.getPlayer().getCaptureFigure();
        
        // neni-li vybran zdroj a je tu figurka, bude toto zdrojem
        if( desk.selected() == null && this.getFigure() != null ){
            
            // jestlize nektera figurka muze brat, ale ne tato
            if (capturingFigure != null && this.getFigure().canCapture() == false) {
                
                // bude pro tentokrat zobrazena napoveda
                getDesk().setTempHelp(true); 
                
                // zdrojovou figurkou se stane figurka kterou lze brat
                capturingFigure.getPosition().select();
                System.out.println("Vybranou nemuze tahnout, vybira se ta kterou muze brat - "+capturingFigure.getPosition());
                
            }else{
                // jinak se zdrojem stane vybrane pole (patri-li figurka na nem uzivateli)
                this.select();
            }
            
        // zdroj je jiz vybran
        }else if(desk.selected()!=null && desk.selected().getFigure()!=null){
            Figure selFigure = desk.selected().getFigure();
            
            // pokud nemuze skocit z prvniho pole na druhe, vyber se zrusi
            if( (!selFigure.canMove(this) && !selFigure.canCapture(this)) && !desk.isRealHelpEnabled() ){
                if (desk.isTempHelpEnabled())
                {
                    desk.setTempHelp(false);
                    desk.clearSelected();
                    return;
                }
                
                desk.setTempHelp(true);
                desk.updateAllBackgrounds();
                //desk.clearSelected();
                return;
            }
            
            // jestlize musi brat
            if (capturingFigure != null){
                
                // bral by pri prechodu sem
                if (selFigure.canCapture(this)){ 
                    
                    // bere dle vyberu
                    desk.selected().getFigure().capture(this);
                    desk.getHistory().addCapture(desk.selected(), this);
                    
                    if(desk.getNetLink()!=null){
                        try{
                            desk.getNetLink().sendMove(desk.selected(),this);
                        }
                        catch(NetException e){
                            System.err.println("Nezdarilo se sendMove(): "+e.toString());
                        }
                    }
                    
                    getDesk().setTempHelp(false);
                    desk.nextPlayer();
                }
                
                // jestlize musi ale nebere
                else if (this != desk.selected() && !(desk.isHelpEnabled())){
                    // vybrat figurku, ktera muze brat
                    if(!selFigure.canCapture()){
                        desk.select(capturingFigure.getPosition());
                    }
                    // jednorazove zobrazit napovedu - kam s ni muze brat
                    getDesk().setTempHelp(true);
                    getDesk().updateAllBackgrounds();
                    return;
                }
            }
            
            // jestlize nemusi a nemuze brat
            else{
                // Muze se figurka premistit na vybrane pole?
                if (desk.selected().getFigure().canMove(this))
                {
                    // Muze se premistit -> premistime
                    desk.selected().getFigure().move(this);
                    desk.getHistory().addMove(desk.selected(), this);
                    
                    if(desk.getNetLink()!=null){
                        try{
                            desk.getNetLink().sendMove(desk.selected(),this);
                        }
                        catch(NetException e){
                            System.err.println("Nezdarilo se sendMove(): "+e.toString());
                        }
                    }
                    getDesk().setTempHelp(false);
                    desk.nextPlayer();
                }
                else if (!getDesk().isHelpEnabled() && this != desk.selected())
                {
                    // Zobrazit pro tentokrat napovedu
                    getDesk().setTempHelp(true);
                    getDesk().updateAllBackgrounds();
                    return;
                }
            }
            desk.clearSelected();
            getDesk().setTempHelp(false);
            
            
            // Kontrola vyhry / prohry 
            // Ale ne pro porazku AI, ti maji vlastni personalizovanou hlasku :-)
            
            // Pokud hrac na tahu nema jak tahnout, prohrava.            
            if ( !desk.isGameEnded() // Pokud je skoncena hra, hralo se s AI
                    && getDesk().getWhitePlayer().loses() && desk.getPlayer().isWhite())
            {
                getDesk().endGame();
                JOptionPane.showMessageDialog(Game.getWindow(), "VyhrÃ¡vÃ¡ Ä�ernÃ½ hrÃ¡Ä�. Gratuluji.",
               "Konec hry", JOptionPane.INFORMATION_MESSAGE);
            }
            
            if (!desk.isGameEnded()
                    && getDesk().getBlackPlayer().loses() && getDesk().getHistory().getCurrent() != -1
                    && desk.getPlayer().isBlack())
            {
                System.out.println("History: " + getDesk().getHistory().getCurrent());
                getDesk().endGame();
                JOptionPane.showMessageDialog(Game.getWindow(), "VyhrÃ¡vÃ¡ bÃ­lÃ½ hrÃ¡Ä�. Gratuluji.",
               "Konec hry", JOptionPane.INFORMATION_MESSAGE);
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
        
        // Inkrementace v obou osach
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