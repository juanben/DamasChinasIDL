
package gui;

import java.util.ArrayList;
import javax.swing.DefaultListModel;

import base.*;

@SuppressWarnings("unchecked")

public class AvailableMoves extends javax.swing.JFrame
{

    private Desk desk;
    private ArrayList<Move> moves;
    
    
    public AvailableMoves(Desk mydesk)
    {
        desk = mydesk;
        
        initComponents();
    }

    
    @SuppressWarnings("unchecked")
    
    private void initComponents()
    {

        jScrollPane1 = new javax.swing.JScrollPane();
        movesList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        buttonMove = new javax.swing.JButton();

        addWindowFocusListener(new java.awt.event.WindowFocusListener()
        {
            public void windowGainedFocus(java.awt.event.WindowEvent evt)
            {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt)
            {
            }
        });

        movesList.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(movesList);

        jLabel1.setText("Movimientos disponibles");

        buttonMove.setText("Hacer");
        buttonMove.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                buttonMoveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(buttonMove)
                .addGap(42, 42, 42))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(buttonMove)
                .addContainerGap())
        );

        pack();
    }

    private void buttonMoveActionPerformed(java.awt.event.ActionEvent evt)
    {
        if (movesList.getSelectedIndex() == -1)
            return; 
        
        int index = movesList.getSelectedIndex();
        
        moves.get(index).getSource().onClick();
        moves.get(index).getDestination().onClick();
        
        this.setVisible(false);
    }

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt)
    {
                DefaultListModel<String> model = new DefaultListModel<>();
        
        
        if(desk.isGameEnded()){
            return;
        }

        
        if(!desk.getHistory().inPresent()){
            return;
        }
        
        
        if(desk.getPlayer().type() != Player.Type.HUMAN){
            return;
        }
        
                                                      
        
        boolean capture = false;
        moves = new ArrayList<>();
        
        for (Figure fig : desk.getPlayer().getFigures())
        {
            if (fig.canCapture())
            {
                capture = true;
                break;
            }
        }
        
        if (capture)
        {
            for (Figure fig : desk.getPlayer().getFigures())
                for (int i = 0; i < 8; i++)
                {
                    for (int j = 0; j < 8; j++)
                    {
                        Position p = fig.getPosition().getDesk().getPositionAt(i, j);
                        if (fig.canCapture(p))
                        {
                            Move m = new Move(fig.getPosition(), p, true);
                            moves.add(m);
                        }
                    }
                }
        }
        else
        {
            for (Figure fig : desk.getPlayer().getFigures())
                for (int i = 0; i < 8; i++)
                {
                    for (int j = 0; j < 8; j++)
                    {
                        Position p = fig.getPosition().getDesk().getPositionAt(i, j);
                        if (fig.canMove(p))
                        {
                            Move m = new Move(fig.getPosition(), p, false);
                            moves.add(m);
                        }
                    }
                }
        }
        
        
        for (Move m : moves)
        {
            model.addElement(m.toString());
        }
        
        movesList.setModel(model);
    }


    
    private javax.swing.JButton buttonMove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList movesList;
    
}
