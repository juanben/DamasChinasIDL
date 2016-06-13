/**
 * IJA - projekt 2013
 * soubor: Position.java
 * Implementace policka na sachovnici
 * 
 * Autori:
 *         @author Michal Dobes (xdobes13)
 *         @author Jan Kalina   (xkalin03)
 */

package ija.projekt.base;
import ija.projekt.figures.*;
import javax.swing.*;
import ija.projekt.network.*;

/**
 * Políčko herní plochy (graficky reprezentované JButtonem)
 * @author Jan Kalina <xkalin03@stud.fit.vutbr.cz>
 * @author Michal Dobes <xdobes13@stud.fit.vutbr.cz>
 */
public final class Position extends JButton {
    
    // ikony hernich kamenu spolecne pro vsechna Position
    private static ImageIcon blackIcon;
    private static ImageIcon black2Icon;
    private static ImageIcon whiteIcon;
    private static ImageIcon white2Icon;
    
    private int x, y; // sloupec a radek pozice
    private Desk desk; // odkaz na hraci plochu
    private Figure figure; // kamen na teto pozici
    
    /**
     * Statický konstruktor - načítá ikony políček při startu aplikace
     * do statických proměnných, aby se nemusely načítat pro každé políčko
     */
    static {
        Position.blackIcon = new ImageIcon(Position.class.getResource("/ija/projekt/images/black.png"));
        Position.black2Icon = new ImageIcon(Position.class.getResource("/ija/projekt/images/black2.png"));
        Position.whiteIcon = new ImageIcon(Position.class.getResource("/ija/projekt/images/white.png"));
        Position.white2Icon = new ImageIcon(Position.class.getResource("/ija/projekt/images/white2.png"));
    }
    
    /**
     * Konstruktor políčka herní plochy
     * @param desk Herní plocha
     * @param x Horizontální souřadnice s 0 v levém rohu plochy
     * @param y Vertikální souřadnice s 0 ve spodním rohu plochy
     */
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
    
    /**
     * Jde o políčko černé barvy? (nikoliv figurky!)
     * @return true pokud je černé
     */
    public boolean isPositionBlack()
    {
        return ((x+y)%2==0);
    }
    
    /**
     * Přebarvení pozadí políčka podle aktuálního výběru
     */
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
    
    /**
     * Umístění figurky na toto políčko
     * @param figure figurka
     */
    public void placeFigure(Figure figure)
    {
        this.figure = figure;
        updateIcon();
    }
    
    /**
     * Získání figurky na tomto políčku
     * @return figurka
     */
    public Figure getFigure()
    {
	return figure;
    }
    
    /**
     * Odstranění figurky
     * Voláno pro vyhození figurky
     * @return vyhozená figurka
     */
    public Figure removeFigure()
    {
        Figure old = figure;
        figure = null;
        return old;
    }

    /**
     * Aktualizace ikony JButtonu podle figurky,
     * kterou pozice obsahuje
     */
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
    
    /**
     * Výběr této pozice
     */
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
    
    /**
     * Pokus o přeměnu kamene v dámu
     * Voláno po přemístění kamene na tuto pozici
     */
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
    
    /**
     * Obsluha události kliknutí na pozici uživatelem
     */
    public void onClick()
    {
        if (desk.isGameEnded())
        {
            JOptionPane.showMessageDialog(Game.getWindow(), "Hra již skončila a vítězi bylo pogratulováno."
                    + "\nPro novou hru zvolte Hra -> Nová Hra.",
               "Nelze hrát", JOptionPane.INFORMATION_MESSAGE);
            System.err.println("Hra již skončila!");
            return;
        }
        
        // Jsme vubec v soucasnosti?
        if (!desk.getHistory().inPresent())
        {
            JOptionPane.showMessageDialog(Game.getWindow(), "Snažíte se změnit historii. To není možné.",
               "Jste včerejší", JOptionPane.INFORMATION_MESSAGE);
            System.err.println("Uživatel nemůže hrát, pohybuje se v historii!");
            return;
        }
        
        // Jsme vubec na tahu?
        if (desk.getPlayer().type() != Player.Type.HUMAN)
        {
            JOptionPane.showMessageDialog(Game.getWindow(), "Nejste na tahu",
               "Nyní je na tahu druhý hráč!", JOptionPane.INFORMATION_MESSAGE);
            System.err.println("Uživatel nemůže hrát, na tahu je druhý hráč!");
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
                JOptionPane.showMessageDialog(Game.getWindow(), "Vyhrává černý hráč. Gratuluji.",
               "Konec hry", JOptionPane.INFORMATION_MESSAGE);
            }
            
            if (!desk.isGameEnded()
                    && getDesk().getBlackPlayer().loses() && getDesk().getHistory().getCurrent() != -1
                    && desk.getPlayer().isBlack())
            {
                System.out.println("History: " + getDesk().getHistory().getCurrent());
                getDesk().endGame();
                JOptionPane.showMessageDialog(Game.getWindow(), "Vyhrává bílý hráč. Gratuluji.",
               "Konec hry", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    /**
     * Získání označení pozice
     * @return Označení pozice
     */
    @Override public String toString()
    {
        char col = (char)((int)'a'+this.x);
        int row = this.y+1;
        return col + Integer.toString(row);
    }
    
    /**
     * Reset pozice do stavu na počátku partie
     * Voláno při otevření nové hry (Desk.newGame())
     */
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
    
    /**
     * Hrací deska
     * @return Hrací deska
     */
    public Desk getDesk()
    {
        return desk;
    }
    
    /**
     * Číslo sloupce na které pozice leží
     * @return Číslo sloupce hrací desky (0-7)
     */
    public byte getColumn(){
        return (byte)x;
    }
    
    /**
     * Číslo řádku na kterém pozice leží
     * @return Číslo řádku hrací desky (0-7)
     */
    public byte getRow(){
        return (byte)y;
    }
    
    /**
     * Získání jiné pozice hrací desky relativně vůči této pozici
     * @param dx Přírůstek horizontální souřadnice
     * @param dy Přírůstek vertikální souřadnice
     * @return Pozice
     */
    public Position nextPosition(int dx, int dy)
    {
	return desk.getPositionAt(this.x + dx, this.y + dy);
    }
    
    /**
     * Leží tato pozice na diagonále danou pozicí
     * @param p daná pozice
     * @return Zdali leží na diagonále
     */
    public boolean isDiagonalOf(Position p)
    {
        return (Math.abs(p.x - this.x) == Math.abs(p.y - this.y));
    }


    /**
     * První neprázdná pozice v cestě na diagonále z této pozice na cílovou
     * @param destination Cílová pozice
     * @return První pozice na diagonále s figurkou
     */
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
    
    /**
     * Zdali jsou pozice totožné (leží na stejných souřadnicích)
     * @param o pozice se kterou je porovnáváno
     * @return zdali mají totožné souřadnici
     */
    @Override public boolean equals(Object o)
    {
	if (o instanceof Position)
	{
	    Position p = (Position) o;
	    return (p.y == this.y && p.x == this.x);
	}
	return false;
    }
    
    /**
     * Hash pozice pro umožnění použití HashMapy
     */
    @Override public int hashCode()
    {
	return (x + y * 10);
    }
}