

package ija.projekt.gui;
import ija.projekt.files.*;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import ija.projekt.base.*;


public class GameWindow extends javax.swing.JFrame {
    
    
    private int maxTabNumber = 0;
    
    
    public GameWindow() {
        initComponents();
        newTab();
    }
    
    
    public final void newTab(){
        maxTabNumber++;
        tabs.addTab("Hra "+maxTabNumber, new GamePanel()); // novy JTabbedPane
        tabs.setSelectedIndex( tabs.getTabCount()-1 );
    }
    
    
    public void closeCurrentTab(){
        
        if(getDesk().getNetLink() != null){
            getDesk().getNetLink().close();
        }
        
        if(tabs.getTabCount() > 1){
            tabs.removeTabAt(tabs.getSelectedIndex());
        }else{ // posledni tab, ukoncit celou aplikaci
            System.exit(0);
        }
    }
    
    
    public Desk getDesk(){
        return ((GamePanel)tabs.getSelectedComponent()).getDesk();
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        tabs = new javax.swing.JTabbedPane();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuGame = new javax.swing.JMenu();
        menuItemNewGame = new javax.swing.JMenuItem();
        menuItemSave = new javax.swing.JMenuItem();
        menuItemOpen = new javax.swing.JMenuItem();
        menuItemHelpEnabled = new javax.swing.JCheckBoxMenuItem();
        showMoves = new javax.swing.JMenuItem();
        menuItemExitGame = new javax.swing.JMenuItem();
        jMenuPlayers = new javax.swing.JMenu();
        menuItemNetworkGame = new javax.swing.JMenuItem();
        menuItemCancelNetwork = new javax.swing.JMenuItem();
        menuItemUI = new javax.swing.JMenuItem();
        menuItemCancelAI = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        jMenuItem3.setText("jMenuItem3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DÃ¡ma");

        tabs.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                tabsStateChanged(evt);
            }
        });

        jMenuGame.setText("Hra");
        jMenuGame.setToolTipText("");

        menuItemNewGame.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        menuItemNewGame.setText("NovÃ¡ hra");
        menuItemNewGame.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemNewGameActionPerformed(evt);
            }
        });
        jMenuGame.add(menuItemNewGame);

        menuItemSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        menuItemSave.setText("UloÅ¾it hru");
        menuItemSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemSaveActionPerformed(evt);
            }
        });
        jMenuGame.add(menuItemSave);

        menuItemOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        menuItemOpen.setText("OtevÅ™Ã­t hru");
        menuItemOpen.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemOpenActionPerformed(evt);
            }
        });
        jMenuGame.add(menuItemOpen);

        menuItemHelpEnabled.setSelected(true);
        menuItemHelpEnabled.setText("NapovÃ­dat");
        menuItemHelpEnabled.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemHelpEnabledActionPerformed(evt);
            }
        });
        jMenuGame.add(menuItemHelpEnabled);

        showMoves.setText("Zobrazit varianty tahu");
        showMoves.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                showMovesActionPerformed(evt);
            }
        });
        jMenuGame.add(showMoves);

        menuItemExitGame.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, java.awt.event.InputEvent.CTRL_MASK));
        menuItemExitGame.setText("UkonÄ�it hru");
        menuItemExitGame.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemExitGameActionPerformed(evt);
            }
        });
        jMenuGame.add(menuItemExitGame);

        jMenuBar.add(jMenuGame);

        jMenuPlayers.setText("HrÃ¡Ä�i");

        menuItemNetworkGame.setText("ZahÃ¡jit sÃ­Å¥ovou hru");
        menuItemNetworkGame.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemNetworkGameActionPerformed(evt);
            }
        });
        jMenuPlayers.add(menuItemNetworkGame);

        menuItemCancelNetwork.setText("Odpojit se od sÃ­Å¥ovÃ© hry");
        menuItemCancelNetwork.setEnabled(false);
        menuItemCancelNetwork.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemCancelNetworkActionPerformed(evt);
            }
        });
        jMenuPlayers.add(menuItemCancelNetwork);

        menuItemUI.setText("HrÃ¡t proti poÄ�Ã­taÄ�i");
        menuItemUI.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemUIActionPerformed(evt);
            }
        });
        jMenuPlayers.add(menuItemUI);

        menuItemCancelAI.setText("ZruÅ¡it hru proti poÄ�Ã­taÄ�i");
        menuItemCancelAI.setEnabled(false);
        menuItemCancelAI.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemCancelAIActionPerformed(evt);
            }
        });
        jMenuPlayers.add(menuItemCancelAI);

        jMenuBar.add(jMenuPlayers);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 871, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuItemNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemNewGameActionPerformed
        newTab();
    }//GEN-LAST:event_menuItemNewGameActionPerformed

    private void menuItemExitGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemExitGameActionPerformed
        closeCurrentTab();
    }//GEN-LAST:event_menuItemExitGameActionPerformed

    private void menuItemNetworkGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemNetworkGameActionPerformed
        NetworkDialog ng = new NetworkDialog(this,false,getDesk());
        ng.setVisible(true);
    }//GEN-LAST:event_menuItemNetworkGameActionPerformed

    private void menuItemHelpEnabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemHelpEnabledActionPerformed
        getDesk().setHelpEnabled(menuItemHelpEnabled.getState());
        getDesk().updateAllBackgrounds();
    }//GEN-LAST:event_menuItemHelpEnabledActionPerformed

    private void tabsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabsStateChanged
        // Pri zmene vybrane zalozky
        menuItemHelpEnabled.setState(getDesk().isHelpEnabled());
        menuItemCancelNetwork.setEnabled(getDesk().getNetLink() != null);
        menuItemCancelAI.setEnabled(getDesk().isAIEnabled());
    }//GEN-LAST:event_tabsStateChanged

    // Prepocitani menu na zadost...
    public void update()
    {
        tabsStateChanged(null);
    }
    
    public File getExamplesFolder(){
        // "JAR/../../examples"
        File folder = new File(GameWindow.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        folder = new File(folder.getParentFile().getParentFile(),"examples");
        if(!folder.isDirectory()){
            // neni-li slozka nalezena vuci jaru, je pouzita vuci pracovnimu adresari (NetBeans)
            folder = new File("examples");
        }
        System.out.println(folder.getAbsolutePath());
        return folder;
    }
    
    private void menuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSaveActionPerformed
        
        JFileChooser fileDialog = new JFileChooser(getExamplesFolder());
        fileDialog.setAcceptAllFileFilterUsed(false);
        fileDialog.setFileFilter(new TxtFile());
        fileDialog.setFileFilter(new XMLFile());
        
        if(fileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
            File file = fileDialog.getSelectedFile();
            try{
                if(fileDialog.getFileFilter() instanceof TxtFile){
                    if (!file.getPath().endsWith(".txt") )
                    {
                        file = new File(file.getPath() + ".txt");
                    }
                    TxtFile.saveFile(file,getDesk().getHistory());
                }
                
                if(fileDialog.getFileFilter() instanceof XMLFile){
                    if (!file.getPath().endsWith(".xml") )
                    {
                        file = new File(file.getPath() + ".xml");
                    }
                    XMLFile.saveFile(file,getDesk().getHistory());
                }
            }
            catch(Exception e){
                System.err.println("Chyba pÅ™i uklÃ¡dÃ¡nÃ­: "+e.toString());
                JOptionPane.showMessageDialog(this, "Chyba pÅ™i uklÃ¡dÃ¡nÃ­: "+e.toString(),
                        "Chyba pÅ™i uklÃ¡dÃ¡nÃ­", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_menuItemSaveActionPerformed

    private void menuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemOpenActionPerformed
        
        JFileChooser fileDialog = new JFileChooser(getExamplesFolder());
        fileDialog.setAcceptAllFileFilterUsed(false);
        fileDialog.setFileFilter(new TxtFile());
        fileDialog.setFileFilter(new XMLFile());
        
        if(fileDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            
            newTab();
            File file = fileDialog.getSelectedFile();
            try{
                if(fileDialog.getFileFilter() instanceof TxtFile){
                    TxtFile.loadFile(file,getDesk().getHistory());
                }else if(fileDialog.getFileFilter() instanceof XMLFile){
                    XMLFile.loadFile(file,getDesk().getHistory());
                }else{
                    return;
                }
                
                // Do historie nacteno, ted promitnout na hraci plochu:
                getDesk().getHistory().goToPresent();
                
                // Zjistit, kdo ma byt na tahu:
                if ( (getDesk().getHistory().getCount() % 2 == 0) != getDesk().getPlayer().isWhite())
                {
                    getDesk().nextPlayer();
                }

                // Zablokovat hru, pokud nektery z hracu vyhral...
                if ( getDesk().getHistory().getCurrent() > 1 && 
                        (getDesk().getWhitePlayer().loses() && getDesk().getPlayer().isWhite() 
                        || getDesk().getBlackPlayer().loses() && getDesk().getPlayer().isBlack()))
                    getDesk().endGame();
            }            
            catch(Exception e){
                System.err.println("Chyba pÅ™i otevÃ­rÃ¡nÃ­:");
                e.printStackTrace(System.err);
                JOptionPane.showMessageDialog(this, "Chyba pÅ™i otevÃ­rÃ¡nÃ­: "+e.toString(),
                        "Chyba pÅ™i otevÃ­rÃ¡nÃ­", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_menuItemOpenActionPerformed

    private void menuItemUIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemUIActionPerformed
        AIDialog aid = new AIDialog(this,false,getDesk());
        aid.setVisible(true);
    }//GEN-LAST:event_menuItemUIActionPerformed

    private void menuItemCancelNetworkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemCancelNetworkActionPerformed
        this.getDesk().getNetLink().close();
        // prenastaveni sachovnice provede Netlink.afterClose()
    }//GEN-LAST:event_menuItemCancelNetworkActionPerformed

    private void menuItemCancelAIActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuItemCancelAIActionPerformed
    {//GEN-HEADEREND:event_menuItemCancelAIActionPerformed
        if (getDesk().getWhitePlayer().type() == Player.Type.AI)
        {
            getDesk().getWhitePlayer().settype(Player.Type.HUMAN);
        }
        
                                                             
        if (getDesk().getBlackPlayer().type() == Player.Type.AI)
        {
            getDesk().getBlackPlayer().settype(Player.Type.HUMAN);
        }
    }//GEN-LAST:event_menuItemCancelAIActionPerformed

    private void showMovesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_showMovesActionPerformed
    {//GEN-HEADEREND:event_showMovesActionPerformed
        AvailableMoves am = new AvailableMoves(getDesk());
        am.setVisible(true);
    }//GEN-LAST:event_showMovesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuGame;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenu jMenuPlayers;
    private javax.swing.JMenuItem menuItemCancelAI;
    private javax.swing.JMenuItem menuItemCancelNetwork;
    private javax.swing.JMenuItem menuItemExitGame;
    private javax.swing.JCheckBoxMenuItem menuItemHelpEnabled;
    private javax.swing.JMenuItem menuItemNetworkGame;
    private javax.swing.JMenuItem menuItemNewGame;
    private javax.swing.JMenuItem menuItemOpen;
    private javax.swing.JMenuItem menuItemSave;
    private javax.swing.JMenuItem menuItemUI;
    private javax.swing.JMenuItem showMoves;
    private javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables
}
