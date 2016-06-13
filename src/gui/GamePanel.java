

package gui;
import java.util.Scanner;
import javax.swing.*;

import base.*;


public class GamePanel extends javax.swing.JPanel {

    private History history = new History();
    
    
    public GamePanel() {
        initComponents();
        ((Desk)desk).setHistory(history);
        history.setDeskJListAndPlayButton((Desk)desk, historyList, buttonPlay);
        ((Desk)desk).newGame();
    }
    
    
    public Desk getDesk(){
        return (Desk)desk;
    }

    
    @SuppressWarnings("unchecked")
    
    private void initComponents() {

        desk = new Desk();
        jScrollPane1 = new javax.swing.JScrollPane();
        historyList = new JList();
        jToolBar1 = new javax.swing.JToolBar();
        timerField = new javax.swing.JTextField();
        buttonPlay = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        actionField = new javax.swing.JTextField();
        buttonAction = new javax.swing.JButton();

        desk.setLayout(new java.awt.GridLayout(8, 8));

        historyList.setModel(history);
        historyList.setVisibleRowCount(-1);
        historyList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                historyListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(historyList);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        timerField.setText("1000");
        timerField.setPreferredSize(new java.awt.Dimension(60, 40));
        jToolBar1.add(timerField);

        buttonPlay.setText("Jugar");
        buttonPlay.setFocusable(false);
        buttonPlay.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonPlay.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlayActionPerformed(evt);
            }
        });
        jToolBar1.add(buttonPlay);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        actionField.setMinimumSize(new java.awt.Dimension(10, 40));
        actionField.setPreferredSize(new java.awt.Dimension(60, 40));
        jToolBar2.add(actionField);

        buttonAction.setText("Tirar");
        buttonAction.setFocusable(false);
        buttonAction.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonAction.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonActionActionPerformed(evt);
            }
        });
        jToolBar2.add(buttonAction);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(desk, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desk, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }

    private void historyListValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if(evt.getValueIsAdjusting() == false){ 
            if(historyList.getSelectedIndex() != -1){ 
                try{
                    history.goToHistoryItem(historyList.getSelectedIndex());
                }
                catch(History.HistoryException e){
                    System.err.println("Excepcion al saltar sobre historia:");
                    e.printStackTrace(System.err);
                    JOptionPane.showMessageDialog(null,
                        e.toString(),
                        "Error mientras salta sobre una entrada de la historia",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void buttonPlayActionPerformed(java.awt.event.ActionEvent evt) {
        if(history.isPlayingHistory()){
            history.stopPlayingHistory();
        }else{
            try{
                int timerValue = Integer.parseInt(timerField.getText());
                history.playHistory(timerValue);
            }
            catch(NumberFormatException e){
                System.err.println("Entrada incorrecta intervalo de reproduccion");
                JOptionPane.showMessageDialog(null,
                        "Intervalo de reproduccion no se introduce correctamente",
                        "Intervalo de reproduccion incorrecta",
                        JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception e){
                System.err.println("Excepcion al jugar");
                    e.printStackTrace(System.err);
                    JOptionPane.showMessageDialog(null,
                        e.toString(),
                        "Error en el juego",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buttonActionActionPerformed(java.awt.event.ActionEvent evt) {
        try{
            
            Scanner s = new Scanner(actionField.getText());
            while(s.hasNext()){
                String move = s.next();
                
                
                if(move.charAt(0) >= '0' && move.charAt(0) <= '9'){
                    continue;
                }
                
                doAction(move);
                actionField.setText("");
            }
        }
        catch(Move.MoveException e){
            System.err.println("Entrada incorrecta");
            JOptionPane.showMessageDialog(null,
                "La medida especificada no esta en la notacion correcta",
                "Mal escrito",
                JOptionPane.WARNING_MESSAGE);
        }
        catch(DoActionException e){
            JOptionPane.showMessageDialog(Game.getWindow(),
                e.getMessage(), e.getTitle(), JOptionPane.INFORMATION_MESSAGE);
            System.err.println(e.getMessage());
        }
        catch(Exception e){
            System.err.println("Excepcion al saltar por una cadena");
            e.printStackTrace(System.err);
            JOptionPane.showMessageDialog(Game.getWindow(),
                e.toString(),
                "Salto de error cuando se especifica manualmente",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void doAction(String move) throws Move.MoveException, DoActionException{
        
        System.out.println("doAction("+move+")");
        
        
        if(getDesk().isGameEnded()){
            throw new DoActionException(
                 "El jugador a terminado y se felicita a los ganadores.\n"+
                 "Para un nuevo juego seleccione Juego -> Nuevo juego.", "No se puede reproducir");
        }

        
        if(!this.getDesk().getHistory().inPresent()){
            throw new DoActionException(
                 "Estan tratando de cambiar la histria", "Eso no es posible");
        }
        
        
        if(this.getDesk().getPlayer().type() != Player.Type.HUMAN){
            throw new DoActionException(
                 "Ahora es el ultimo turno del segundo jugador", "Usted no esta en el movimiento");
        }
        
        Move m = new Move((Desk)desk,move);
        if(m.getSource().getFigure() == null){
            throw new DoActionException(
                 "En el campo de origen no figura cualquier pieza!", "Movimiento ilegal");
        }
        if(m.isCapture()){
            if(m.getSource().getFigure().canCapture(m.getDestination())){
                m.getSource().onClick(); 
                m.getDestination().onClick();
            }else{
                throw new DoActionException(
                     "La medida especificada no es posible", "Movimiento ilegal");
            }
        }else{
            if(m.getSource().getFigure().canMove(m.getDestination())){
                m.getSource().onClick(); 
                m.getDestination().onClick();
            }else{
                throw new DoActionException(
                     "La medida especificada no es posible", "Movimiento ilegal");
            }
        }
    }
    
    class DoActionException extends Exception {
        private String title;
        public DoActionException(String message, String title){
            super(message);
            this.title = title;
        }
        public String getTitle(){
            return title;
        }
    }
    
    
    
    private javax.swing.JTextField actionField;
    private javax.swing.JButton buttonAction;
    private javax.swing.JButton buttonPlay;
    private javax.swing.JPanel desk;
    private javax.swing.JList historyList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JTextField timerField;
    
}
