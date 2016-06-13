package gui;
import javax.swing.JOptionPane;

import ai.Skynet;
import base.*;


public class AIDialog extends javax.swing.JDialog {
    
    Desk desk;

    
    public AIDialog(java.awt.Frame parent, boolean modal, Desk desk) {
        super(parent, modal);
        initComponents();
        this.desk = desk;
    }

    
    @SuppressWarnings("unchecked")
    
    private void initComponents()
    {

        RG_player = new javax.swing.ButtonGroup();
        RG_AIMachine = new javax.swing.ButtonGroup();
        RB_white = new javax.swing.JRadioButton();
        RB_black = new javax.swing.JRadioButton();
        RB_Skynet = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        confirmButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Jugar contra la computadora");
        setResizable(false);

        RG_player.add(RB_white);
        RB_white.setSelected(true);
        RB_white.setText("Blanco");

        RG_player.add(RB_black);
        RB_black.setText("Negro");

        RG_AIMachine.add(RB_Skynet);
        RB_Skynet.setSelected(true);
        RB_Skynet.setText("Skynet");

        jLabel1.setText("Opciones");
        jLabel1.setToolTipText("");

        jLabel2.setText("Adversario");

        jLabel3.setText("Color");

        confirmButton.setText("OK");
        confirmButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                confirmButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(70, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(RB_Skynet)
                            .addComponent(jLabel2))
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(RB_black)
                            .addComponent(RB_white))
                        .addGap(61, 61, 61))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(confirmButton)
                        .addGap(125, 125, 125))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RB_white)
                    .addComponent(RB_Skynet))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RB_black)
                .addGap(18, 18, 18)
                .addComponent(confirmButton)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        
        Player aiPlayer = RB_white.isSelected() ? desk.getWhitePlayer() : desk.getBlackPlayer();
        GenericAI aiLogic = null;
        
        
        if (aiPlayer.type() == Player.Type.REMOTE)
        {
            int answer = JOptionPane.showConfirmDialog(rootPane, "Quiere terminar la conexion", "Tener cuidado", JOptionPane.YES_NO_OPTION);
            
            if(answer == JOptionPane.YES_OPTION){
                desk.getNetLink().close(); 
                desk.setNetLink(null);
            }else{
                return;
            }
        }
        
        
        if (RB_Skynet.isSelected())
        {
            aiLogic = new Skynet(aiPlayer, desk);
        }
        
        aiPlayer.setAI(aiLogic);
        Game.getWindow().update();
        
        
        if (desk.getPlayer() == aiPlayer && !desk.isGameEnded())
        {
            aiPlayer.getAI().makeMove();
            desk.nextPlayer();
        }
        
        this.setVisible(false);
        this.dispose();
    }

    
    private javax.swing.JRadioButton RB_Skynet;
    private javax.swing.JRadioButton RB_black;
    private javax.swing.JRadioButton RB_white;
    private javax.swing.ButtonGroup RG_AIMachine;
    private javax.swing.ButtonGroup RG_player;
    private javax.swing.JButton confirmButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    
}
