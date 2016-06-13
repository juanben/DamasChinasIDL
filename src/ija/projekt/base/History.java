/**
 * IJA - projekt 2013
 * soubor: History.java
 * Implementace historie hry
 * 
 * Autori:
 *         @author Michal Dobes (xdobes13)
 *         @author Jan Kalina   (xkalin03)
 */

package ija.projekt.base;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Historie - seznam provedenych tahu
 * @author Jan Kalina <xkalin03@stud.fit.vutbr.cz>
 * @author Michal Dobes <xdobes13@stud.fit.vutbr.cz>
 */
public class History extends DefaultListModel<String> {
        
    private Desk desk;
    private LinkedList<Move> list = new LinkedList<>();
    private int currentId = -1;
    private javax.swing.Timer playTimer = null;
    private JList jlist;
    private JButton playButton;
    
    /**
     * Přiřadí desku, seznam a tlačítko
     * @param desk deske
     * @param jlist seznam
     * @param playButton tlačítko pro přehrání
     */
    public void setDeskJListAndPlayButton(Desk desk, JList jlist, JButton playButton){
        this.desk = desk;
        this.jlist = jlist;
        this.playButton = playButton;
    }
    
    /**
     * Získá desku
     * @return deska, ke které historie patří
     */
    public Desk getDesk(){
        return this.desk;
    }
    
    /**
     * Přidá do historie tah (přemístění)
     * @param source počáteční pozice
     * @param destination cílová pozice
     */
    public void addMove(Position source, Position destination){
        Move item = new Move(source, destination, false);
        addItem(item);
    }
    
    /**
     * Přidá do historie tah (braní)
     * @param source počáteční pozice
     * @param destination cílová pozice
     */
    public void addCapture(Position source, Position destination){
        Move item = new Move(source, destination, true);
        addItem(item);
    }
    
    /**
     * Přidá tah z řetězce
     * @param s Tah (resp. půltah) zadaný v řetězci v klasické notaci
     * @throws ija.projekt.base.Move.MoveException
     */
    public void addFromString(String s) throws Move.MoveException {
        Move item = new Move(desk, s);
        addItem(item);
    }
    
    private void addItem(Move item){
        list.addLast(item);
        
        if( list.size() % 2 == 1 ){ // lichy
            super.addElement(""+(list.size()+1)/2+". "+item.toString());
        }else{ // sudy
            super.addElement("                  "+item.toString());
        }
        setCurrent(getCount()-1);
    }
    
    /**
     * Získá počet položek v historii
     * @return počet položek v historii
     */
    public int getCount(){
        return list.size();
    }
    
    /**
     * Získá položku historie
     * @param i pořadové číslo (index) v historii
     * @return tah z historie na zadaném indexu
     */
    public Move getItem(int i){
        return list.get(i);
    }
    
    /**
     * Vyčistí historii
     */
    public void clearItems(){
        list.clear();
        super.clear();
    }
    
    /**
     * Nastaví historii, aby korespondovala s výchozím stavem desky
     */
    public void deskWasResetted(){
        setCurrent(-1);
    }
    
    /**
     * Získá aktuální položku historie
     * @return Aktuální položka historie
     */
    public int getCurrent(){
        return currentId;
    }
    
    /**
     * Nastavi aktualne zobrazeny cas partie
     * (-1 = pocatecni stav a nevybrano nic v JListu)
     */
    private void setCurrent(int i){
        currentId = i;
        if(i == -1) jlist.clearSelection();
        else jlist.setSelectedIndex(i);
    }
    
    /**
     * Zjistí, jestli je aktuálně vybraná položka v přítomnosti (tj. poslední)
     * @return true pokud je v přítomnosti, jinak false
     */
    public boolean inPresent(){
        return currentId == getCount()-1;
    }
    
    /**
     * Přejde na poslední tah (a tedy do přítomnosti)
     * @throws ija.projekt.base.History.HistoryException
     */
    public void goToPresent() throws History.HistoryException {
        goToHistoryItem(getCount()-1, true);
    }
    
    /**
     * Přejde na položku historie
     * @param destinationId index cílové položky
     * @throws ija.projekt.base.History.HistoryException
     */
    public void goToHistoryItem(int destinationId) throws History.HistoryException {
        goToHistoryItem(destinationId, false);
    }
    
    /**
     * Přejde na položku historie
     * @param destinationId index cílové položky
     * @param repeat opakovat skok?
     * @throws ija.projekt.base.History.HistoryException
     */
    public void goToHistoryItem(int destinationId, boolean repeat) throws History.HistoryException {
        
        if( destinationId < -1 || destinationId >= getCount() ){
            throw new HistoryException("Nelze prejit na tah "+destinationId+", ktery je mimo rozsah!");
        }
        
        if( destinationId == currentId && !repeat ){
            return; // jiz tam je
        }
        
        desk.resetDesk();
        desk.clearSelected();
        setCurrent(destinationId);
        
        for( int i = 0; i <= destinationId; i++ ){
            Move item = getItem(i);
            if(item.getSource().getFigure() == null)
               throw new HistoryException("Zdroj skoku ("+item.getSource().toString()+") nema figurku!");
            if(item.isCapture()){
                item.getSource().getFigure().capture(item.getDestination());
            }else{
                item.getSource().getFigure().move(item.getDestination());
            }
        }
        
        desk.updateAllBackgrounds();
    }
    
    /**
     * Přehraje historii až k aktuální položce
     * @param interval časová mezera mezi tahy
     */
    public void playHistory(int interval){
        System.out.println("Zahajeno prehravani partie");
        final int destinationId = getCurrent();
        desk.resetDesk();
        playTimer = new javax.swing.Timer(interval, new ActionListener(){
            @Override public void actionPerformed(ActionEvent evt){
                
                if(getCurrent() >= destinationId){
                    System.out.println("Prehravani partie dokonceno");
                    stopPlayingHistory();
                    desk.updateAllBackgrounds();
                    return;
                }
                
                try{
                    goToHistoryItem(getCurrent()+1);
                } catch (Exception ex) {
                    System.err.println("Vyjimka pri prehravani partie:");
                    ex.printStackTrace(System.err);
                    JOptionPane.showMessageDialog(null, ex.toString(),
                        "Chyba při přehrávání", JOptionPane.ERROR_MESSAGE);
                }
                
            }
        });
        playTimer.start();
        playButton.setText("Stop");
    }
    
    /**
     * Zastaví přehrávání historie
     */
    public void stopPlayingHistory(){
        playTimer.stop();
        playTimer = null;
        playButton.setText("Přehrát");
    }
    
    /**
     * Zjistí, jestli se přehrává historie
     * @return true pokud se přehrává historie, jinak false
     */
    public boolean isPlayingHistory(){
        return (playTimer != null);
    }
    
    /**
     * Třída vyjímek pro historii
     */
    public class HistoryException extends Exception {
        public HistoryException(String message){
            super(message);
        }
    }
}
