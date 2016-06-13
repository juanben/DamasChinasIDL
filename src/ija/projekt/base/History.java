

package ija.projekt.base;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class History extends DefaultListModel<String> {
        
    private Desk desk;
    private LinkedList<Move> list = new LinkedList<>();
    private int currentId = -1;
    private javax.swing.Timer playTimer = null;
    private JList jlist;
    private JButton playButton;
    
    
    public void setDeskJListAndPlayButton(Desk desk, JList jlist, JButton playButton){
        this.desk = desk;
        this.jlist = jlist;
        this.playButton = playButton;
    }
    
    
    public Desk getDesk(){
        return this.desk;
    }
    
    
    public void addMove(Position source, Position destination){
        Move item = new Move(source, destination, false);
        addItem(item);
    }
    
    
    public void addCapture(Position source, Position destination){
        Move item = new Move(source, destination, true);
        addItem(item);
    }
    
    
    public void addFromString(String s) throws Move.MoveException {
        Move item = new Move(desk, s);
        addItem(item);
    }
    
    private void addItem(Move item){
        list.addLast(item);
        
        if( list.size() % 2 == 1 ){ 
            super.addElement(""+(list.size()+1)/2+". "+item.toString());
        }else{ 
            super.addElement("                  "+item.toString());
        }
        setCurrent(getCount()-1);
    }
    
    
    public int getCount(){
        return list.size();
    }
    
    
    public Move getItem(int i){
        return list.get(i);
    }
    
    
    public void clearItems(){
        list.clear();
        super.clear();
    }
    
    
    public void deskWasResetted(){
        setCurrent(-1);
    }
    
    
    public int getCurrent(){
        return currentId;
    }
    
    
    private void setCurrent(int i){
        currentId = i;
        if(i == -1) jlist.clearSelection();
        else jlist.setSelectedIndex(i);
    }
    
    
    public boolean inPresent(){
        return currentId == getCount()-1;
    }
    
    
    public void goToPresent() throws History.HistoryException {
        goToHistoryItem(getCount()-1, true);
    }
    
    
    public void goToHistoryItem(int destinationId) throws History.HistoryException {
        goToHistoryItem(destinationId, false);
    }
    
    
    public void goToHistoryItem(int destinationId, boolean repeat) throws History.HistoryException {
        
        if( destinationId < -1 || destinationId >= getCount() ){
            throw new HistoryException("No se puede ir a la jugada "+destinationId+", que esta fuera de rango");
        }
        
        if( destinationId == currentId && !repeat ){
            return; 
        }
        
        desk.resetDesk();
        desk.clearSelected();
        setCurrent(destinationId);
        
        for( int i = 0; i <= destinationId; i++ ){
            Move item = getItem(i);
            if(item.getSource().getFigure() == null)
               throw new HistoryException("Salto fuente ("+item.getSource().toString()+") ninguna figura");
            if(item.isCapture()){
                item.getSource().getFigure().capture(item.getDestination());
            }else{
                item.getSource().getFigure().move(item.getDestination());
            }
        }
        
        desk.updateAllBackgrounds();
    }
    
    
    public void playHistory(int interval){
        System.out.println("Empieza a jugar");
        final int destinationId = getCurrent();
        desk.resetDesk();
        playTimer = new javax.swing.Timer(interval, new ActionListener(){
            @Override public void actionPerformed(ActionEvent evt){
                
                if(getCurrent() >= destinationId){
                    System.out.println("Jugar esta completo");
                    stopPlayingHistory();
                    desk.updateAllBackgrounds();
                    return;
                }
                
                try{
                    goToHistoryItem(getCurrent()+1);
                } catch (Exception ex) {
                    System.err.println("Excepcion al jugar");
                    ex.printStackTrace(System.err);
                    JOptionPane.showMessageDialog(null, ex.toString(),
                        "Error de reproduccion", JOptionPane.ERROR_MESSAGE);
                }
                
            }
        });
        playTimer.start();
        playButton.setText("Stop");
    }
    
    
    public void stopPlayingHistory(){
        playTimer.stop();
        playTimer = null;
        playButton.setText("jugar");
    }
    
    
    public boolean isPlayingHistory(){
        return (playTimer != null);
    }
    
    
    public class HistoryException extends Exception {
        public HistoryException(String message){
            super(message);
        }
    }
}
