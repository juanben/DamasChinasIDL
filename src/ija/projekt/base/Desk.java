

package ija.projekt.base;
import javax.swing.*;
import ija.projekt.network.*;


public class Desk extends JPanel {
    
    private Position positions[][]; 
    private Position selected = null; 
    private Player playerWhite, playerBlack; 
    private Player player; 
    private History history; 
    
    private boolean helpEnabled;
    private boolean helpOnce = false;
    
    private NetLink netlink = null;
    
    private boolean gameEnded = false;
    
    
    public Desk()
    {
        
        playerWhite = new Player(Player.Color.WHITE);
        playerBlack = new Player(Player.Color.BLACK);
        playerWhite.setOpponent(playerBlack);
        playerBlack.setOpponent(playerWhite);
        
        playerWhite.settype(Player.Type.HUMAN);
        playerBlack.settype(Player.Type.HUMAN);
        
        
        
        positions = new Position[8][8];
        for( int y=7 ; y>=0 ; y-- ){
            for( int x=0 ; x<=7 ; x++ ){
                positions[x][y] = new Position(this,x,y);
                
                add(positions[x][y]);
            }
        }
        
        helpEnabled = false;
    }
    
    
    public void setHistory(History history)
    {
        assert this.history == null : "El metodo historia no puede ser llamado varias veces";
        this.history = (History)history;
    }
    
    
    public History getHistory()
    {
        return history;
    }
    
    
    public boolean isAIEnabled()
    {
        return ( getWhitePlayer().type() == Player.Type.AI 
                || getBlackPlayer().type() == Player.Type.AI);
    }
    
    
    public boolean isHelpEnabled(){
        return helpEnabled || helpOnce;
    }
    
    
    public void setTempHelp(boolean val)
    {
        helpOnce = val;
    }
    
    
    public boolean isTempHelpEnabled()
    {
        return helpOnce;
    }
    
    
    public boolean isRealHelpEnabled()
    {
        return helpEnabled;
    }
    
    
    public boolean setHelpEnabled(boolean he){
        return helpEnabled = he;
    }
    
    
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
    
    
    public NetLink getNetLink(){
        return netlink;
    }
    
    
    public void newGame()
    {
        player = playerWhite;
        resetDesk();
        updateAllBackgrounds();
        history.clearItems();
    }
    
    
    public void endGame()
    {
        gameEnded = true;
    }
    
    
    public boolean isGameEnded()
    {
        return gameEnded;
    }
    
    
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
    
    
    public Player getPlayer()
    {
        return player;
    }
    
    
    public Player getWhitePlayer()
    {
        return playerWhite;
    }
    
    
    public Player getBlackPlayer()
    {
        return playerBlack;
    }
    
    
    public Position selected()
    {
        return selected;
    }
    
    
    public void select(Position position)
    {
        selected = position;
    }
    
    
    public void clearSelected()
    {
        selected = null;
        updateAllBackgrounds();
    }
    
    
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

    
    public Position getPositionAt(int column, int row)
    {
            if(isAtDesk(column, row)){
                return positions[column][row];
            }else{
                return null;
            }
    }
}