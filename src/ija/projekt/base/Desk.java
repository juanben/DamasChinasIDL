/**
 * IJA - projekt 2013
 * soubor: Desk.java
 * Implementace hraci desky
 * 
 * Autori:
 *         @author Michal Dobes (xdobes13)
 *         @author Jan Kalina   (xkalin03)
 */

package ija.projekt.base;
import javax.swing.*;
import ija.projekt.network.*;

/**
 * Hraci plocha
 * (obsahuje 64 pozic)
 * @author Jan Kalina <xkalin03@stud.fit.vutbr.cz>
 * @author Michal Dobes <xdobes13@stud.fit.vutbr.cz>
 */
public class Desk extends JPanel {
    
    private Position positions[][]; // [ x=sloupec: 0-7 ] [ y=radek: 0-7 ]
    private Position selected = null; // aktualne vybrana (zluta) pozice
    private Player playerWhite, playerBlack; // hraci
    private Player player; // hrac ktery je prave na rade
    private History history; // seznam provedenych tahu
    
    private boolean helpEnabled;
    private boolean helpOnce = false;
    
    private NetLink netlink = null;
    
    private boolean gameEnded = false;
    
    /**
     * Konstruktor
     */
    public Desk()
    {
        // Inicializace hracu
        playerWhite = new Player(Player.Color.WHITE);
        playerBlack = new Player(Player.Color.BLACK);
        playerWhite.setOpponent(playerBlack);
        playerBlack.setOpponent(playerWhite);
        
        playerWhite.settype(Player.Type.HUMAN);
        playerBlack.settype(Player.Type.HUMAN);
        
        // Inicializace jednotlivych pozic herni plochy tak,
        // aby se pole vykreslila ve spravnem poradi
        positions = new Position[8][8];
        for( int y=7 ; y>=0 ; y-- ){
            for( int x=0 ; x<=7 ; x++ ){
                positions[x][y] = new Position(this,x,y);
                //positions[x][y].setText(x+""+y+"-"+positions[x][y].toString()); // DEBUG
                add(positions[x][y]);
            }
        }
        
        helpEnabled = false;
    }
    
    /**
     * Přiřadí desce historii
     * @param history objekt historie
     */
    public void setHistory(History history)
    {
        assert this.history == null : "Metodu setHistory(History) nelze volat opakovane!";
        this.history = (History)history;
    }
    
    /**
     * Získá hisorii
     * @return objekt historie
     */
    public History getHistory()
    {
        return history;
    }
    
    /**
     * Ověří, jestli na desce hraje umělá inteligence
     * @return bool jestli je na desce AI
     */
    public boolean isAIEnabled()
    {
        return ( getWhitePlayer().type() == Player.Type.AI 
                || getBlackPlayer().type() == Player.Type.AI);
    }
    
    /**
     * Zjistí, jestli je vyžadována nápověda
     * @return bool true pokud je vyžadována nápověda
     */
    public boolean isHelpEnabled(){
        return helpEnabled || helpOnce;
    }
    
    /**
     * Nastaví dočasnou (tj. automatickou) nápovědu
     * @param val true -> zapnout, false -> vypnout
     */
    public void setTempHelp(boolean val)
    {
        helpOnce = val;
    }
    
    /**
     * Zjistí, jetsli je aktivována dočasná (automatická) nápověda
     * @return true pokuj e aktivovaná dočasná (automatická) nápověda
     */
    public boolean isTempHelpEnabled()
    {
        return helpOnce;
    }
    
    /**
     * Zjistí, jestli je aktivovaná trvalá nápověda
     * @return true pokud ano, jinak false
     */
    public boolean isRealHelpEnabled()
    {
        return helpEnabled;
    }
    
    /**
     * Nastaví trvalou nápovědu
     * @param he true -> zapnout, false -> vypnout
     * @return bool nový stav nápovědy
     */
    public boolean setHelpEnabled(boolean he){
        return helpEnabled = he;
    }
    
    /**
     * Přiřadí desce síťové spojení
     * @param netlink Objekt síťového spojení
     */
    public void setNetLink(NetLink netlink){
        this.netlink = netlink;
        if(netlink == null)
        {
            getWhitePlayer().settype(Player.Type.HUMAN);
            getBlackPlayer().settype(Player.Type.HUMAN);
            Game.getWindow().update();
        }
        updateAllBackgrounds();
    }
    
    /**
     * Vrátí objekt síťového spojení
     * @return Objekt síťového spojení
     */
    public NetLink getNetLink(){
        return netlink;
    }
    
    /**
     * Zahájí novou hru
     */
    public void newGame()
    {
        player = playerWhite;
        resetDesk();
        updateAllBackgrounds();
        history.clearItems();
    }
    
    /**
     * Nastaví, že je hra ukončená (již nelze táhnout).
     */
    public void endGame()
    {
        gameEnded = true;
    }
    
    /**
     * Zjistí, jestli je hra ukončená.
     * @return bool true -> je ukončená, jinak false
     */
    public boolean isGameEnded()
    {
        return gameEnded;
    }
    
    /**
     * Rozestaví figurky na desce do výchozí polohy
     */
    public void resetDesk()
    {
        getWhitePlayer().purgeFigures();
        getBlackPlayer().purgeFigures();
        
        selected = null;
        for( int x = 0 ; x < 8 ; x++ ){
            for( int y = 0 ; y < 8 ; y++ ){
                positions[x][y].resetPosition();
            }
        }
        history.deskWasResetted();
    }
    
    /**
     * Předá tah dalšímu hráči.
     */
    public void nextPlayer()
    {
        player = player.opponent();
        while (player.type() == Player.Type.AI && !isGameEnded())
        {
            player.getAI().makeMove();
            player = player.opponent();
        }
        updateAllBackgrounds();
    }
    
    /**
     * Získá hráče na tahu
     * @return hráč, který je momentálně na tahu.
     */
    public Player getPlayer()
    {
        return player;
    }
    
    /**
     * Získá bílého hráče
     * @return objekt bílého hráče.
     */
    public Player getWhitePlayer()
    {
        return playerWhite;
    }
    
    /**
    * Získá černého hráče
    * @return objekt černého hráče.
    */
    public Player getBlackPlayer()
    {
        return playerBlack;
    }
    
    /**
     * Získá označené políčko
     * @return objekt aktuálně označeného políčka, null pokud žádné není označené.
     */
    public Position selected()
    {
        return selected;
    }
    
    /**
     * Označí políčko
     * @param position Políčko k označení
     */
    public void select(Position position)
    {
        selected = position;
    }
    
    /**
     * Odznačí políčko
     */
    public void clearSelected()
    {
        selected = null;
        updateAllBackgrounds();
    }
    
    /**
     * Provede překreslení pozadí políček
     */
    public void updateAllBackgrounds(){
        for( int x=0 ; x<8 ; x++ ){
            for( int y=0 ; y<8 ; y++ ){
                positions[x][y].updateBackground();
            }
        }
    }
    
    private boolean isAtDesk(int column, int row)
    {
        return ( ( column >= 0 && column < 8 ) && ( row >= 0 && row < 8 ) );
    }

    /**
     * Získá políčko na zadané pozici
     * @param column sloupec políčka (0-7)
     * @param row řádek políčka (0-7)
     * @return políčko na uadané pozici, null pokud takové políčko neexistuje
     */
    public Position getPositionAt(int column, int row)
    {
            if(isAtDesk(column, row)){
                return positions[column][row];
            }else{
                return null;
            }
    }
}