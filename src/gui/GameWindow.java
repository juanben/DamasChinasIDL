

package gui;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import archivos.*;
import base.*;


public class GameWindow extends javax.swing.JFrame {
    
    
    private int maxTabNumber = 0;
    
    
    public GameWindow() {
        initComponents();
        newTab();
    }
    
    
    public final void newTab(){
        maxTabNumber++;
        tabs.addTab("Hra "+maxTabNumber, new GamePanel()); 
        tabs.setSelectedIndex( tabs.getTabCount()-1 );
    }
    
    
    public void closeCurrentTab(){
        
        if(getDesk().getNetLink() != null){
            getDesk().getNetLink().close();
        }
        
        if(tabs.getTabCount() > 1){
            tabs.removeTabAt(tabs.getSelectedIndex());
        }else{ 
            System.exit(0);
        }
    }
    
    
    public Desk getDesk(){
        return ((GamePanel)tabs.getSelectedComponent()).getDesk();
    }

    
    @SuppressWarnings("unchecked")
    
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
        setTitle("Damas");

        tabs.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                tabsStateChanged(evt);
            }
        });

        jMenuGame.setText("Juego");
        jMenuGame.setToolTipText("");

        menuItemNewGame.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        menuItemNewGame.setText("Nuevo juego");
        menuItemNewGame.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemNewGameActionPerformed(evt);
            }
        });
        jMenuGame.add(menuItemNewGame);

        menuItemSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        menuItemSave.setText("Guardar juego");
        menuItemSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemSaveActionPerformed(evt);
            }
        });
        jMenuGame.add(menuItemSave);

        menuItemOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        menuItemOpen.setText("Partido abierto");
        menuItemOpen.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemOpenActionPerformed(evt);
            }
        });
        jMenuGame.add(menuItemOpen);

        menuItemHelpEnabled.setSelected(true);
        menuItemHelpEnabled.setText("Rapido");
        menuItemHelpEnabled.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemHelpEnabledActionPerformed(evt);
            }
        });
        jMenuGame.add(menuItemHelpEnabled);

        showMoves.setText("Mostrar variantes");
        showMoves.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                showMovesActionPerformed(evt);
            }
        });
        jMenuGame.add(showMoves);

        menuItemExitGame.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, java.awt.event.InputEvent.CTRL_MASK));
        menuItemExitGame.setText("Final del juego");
        menuItemExitGame.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemExitGameActionPerformed(evt);
            }
        });
        jMenuGame.add(menuItemExitGame);

        jMenuBar.add(jMenuGame);

        jMenuPlayers.setText("Jugadores");

        menuItemNetworkGame.setText("Lance un juego en red");
        menuItemNetworkGame.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemNetworkGameActionPerformed(evt);
            }
        });
        jMenuPlayers.add(menuItemNetworkGame);

        menuItemCancelNetwork.setText("Desconectarse de la red");
        menuItemCancelNetwork.setEnabled(false);
        menuItemCancelNetwork.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemCancelNetworkActionPerformed(evt);
            }
        });
        jMenuPlayers.add(menuItemCancelNetwork);

        menuItemUI.setText("Juega contra el ordenardor");
        menuItemUI.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemUIActionPerformed(evt);
            }
        });
        jMenuPlayers.add(menuItemUI);

        menuItemCancelAI.setText("Cancelar partido contra el equipo");
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
    }

    private void menuItemNewGameActionPerformed(java.awt.event.ActionEvent evt) {
        newTab();
    }

    private void menuItemExitGameActionPerformed(java.awt.event.ActionEvent evt) {
        closeCurrentTab();
    }

    private void menuItemNetworkGameActionPerformed(java.awt.event.ActionEvent evt) {
        NetworkDialog ng = new NetworkDialog(this,false,getDesk());
        ng.setVisible(true);
    }

    private void menuItemHelpEnabledActionPerformed(java.awt.event.ActionEvent evt) {
        getDesk().setHelpEnabled(menuItemHelpEnabled.getState());
        getDesk().updateAllBackgrounds();
    }

    private void tabsStateChanged(javax.swing.event.ChangeEvent evt) {
        
        menuItemHelpEnabled.setState(getDesk().isHelpEnabled());
        menuItemCancelNetwork.setEnabled(getDesk().getNetLink() != null);
        menuItemCancelAI.setEnabled(getDesk().isAIEnabled());
    }

    
    public void update()
    {
        tabsStateChanged(null);
    }
    
    public File getExamplesFolder(){
        
        File folder = new File(GameWindow.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        folder = new File(folder.getParentFile().getParentFile(),"examples");
        if(!folder.isDirectory()){
            
            folder = new File("examples");
        }
        System.out.println(folder.getAbsolutePath());
        return folder;
    }
    
    private void menuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {
        
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
                System.err.println("Error: "+e.toString());
                JOptionPane.showMessageDialog(this, "Error: "+e.toString(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void menuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {
        
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
                
                
                getDesk().getHistory().goToPresent();
                
                
                if ( (getDesk().getHistory().getCount() % 2 == 0) != getDesk().getPlayer().isWhite())
                {
                    getDesk().nextPlayer();
                }

                
                if ( getDesk().getHistory().getCurrent() > 1 && 
                        (getDesk().getWhitePlayer().loses() && getDesk().getPlayer().isWhite() 
                        || getDesk().getBlackPlayer().loses() && getDesk().getPlayer().isBlack()))
                    getDesk().endGame();
            }            
            catch(Exception e){
                System.err.println("Error al abrir:");
                e.printStackTrace(System.err);
                JOptionPane.showMessageDialog(this, "Error al abrir: "+e.toString(),
                        "Error al abrir", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void menuItemUIActionPerformed(java.awt.event.ActionEvent evt) {
        AIDialog aid = new AIDialog(this,false,getDesk());
        aid.setVisible(true);
    }

    private void menuItemCancelNetworkActionPerformed(java.awt.event.ActionEvent evt) {
        this.getDesk().getNetLink().close();
        
    }

    private void menuItemCancelAIActionPerformed(java.awt.event.ActionEvent evt)
    {
        if (getDesk().getWhitePlayer().type() == Player.Type.AI)
        {
            getDesk().getWhitePlayer().settype(Player.Type.HUMAN);
        }
        
                                                             
        if (getDesk().getBlackPlayer().type() == Player.Type.AI)
        {
            getDesk().getBlackPlayer().settype(Player.Type.HUMAN);
        }
    }

    private void showMovesActionPerformed(java.awt.event.ActionEvent evt)
    {
        AvailableMoves am = new AvailableMoves(getDesk());
        am.setVisible(true);
    }

    
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
    
}
