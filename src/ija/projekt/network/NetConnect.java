/**
 * IJA - projekt 2013
 * soubor: NetConnect.java
 * Trida pro vytvoreni sitoveho pripojeni.
 * 
 * Autori:
 *         @author Michal Dobes (xdobes13)
 *         @author Jan Kalina   (xkalin03)
 */

package ija.projekt.network;
import java.net.*;
import java.io.*;
import ija.projekt.base.*;
import ija.projekt.gui.NetworkDialog;
import javax.swing.JOptionPane;

/**
 * Navazovač spojení se síťovým klientem
 * (při navázání spojení vytvoří instanci NetLink)
 * @author Jan Kalina <xkalin03@stud.fit.vutbr.cz>
 * @author Michal Dobes <xdobes13@stud.fit.vutbr.cz>
 */
public class NetConnect {
    
    NetLink netlink;
    ServerSocket server = null;
    Thread serverThread = null;
    int serverPort;
    Socket client = null;
    Desk desk;
    NetworkDialog dialog;
    
    /**
     * Konstruktor navazovače volaný z NetworkDialog
     * @param desk Hrací plocha
     * @param dialog NetworkDialog vyvolávající navázání spojení
     */
    public NetConnect(Desk desk, NetworkDialog dialog){
        this.desk   = desk;
        this.dialog = dialog;
    }
    
    /**
     * Otevření portu pro umožnění připojení jiného síťového klienta
     * a následné čekání na připojení ve zvláštním vlákně
     * @param port Port který bude otevřen
     * @param white Hraje tato strana za bílé? (A síťový klient za černé?)
     */
    public void startServerThread(int port, final boolean white){
        serverPort = port;
        serverThread = new Thread() {
            @Override public void run() {
                startServer(serverPort, white);
            }
        };
        serverThread.setDaemon(true);
        serverThread.start();
    }
    
    /**
     * Otevření portu pro umožnění připojení jiného síťového klienta
     * ale s čekáním ve stávajícím vlákně
     * @param port Port který bude otevřen
     * @param white Hraje tato strana za bílé? (A síťový klient za černé?)
     */
    public boolean startServer(int port, boolean white){
        if(server!=null){
            stopServer();
        }
        try{
            server = new ServerSocket();
            server.setReuseAddress(true);
            server.bind(new InetSocketAddress(port));
            System.out.println("Vyckavani na spojeni...");
            netlink = new NetLink(server.accept(), desk, false, white);
            dialog.setVisible(false);
            dialog.dispose();
            desk.setNetLink(netlink);
            netlink.start();
            netlink.sendInit();
            stopServer();
            if(white)
            {
                desk.getBlackPlayer().settype(Player.Type.REMOTE);
            }
            else
            {
                desk.getWhitePlayer().settype(Player.Type.REMOTE);
            }
            Game.getWindow().update();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(Game.getWindow(), "Při zakládání hry došlo k chybě:\n"+e.toString(),
               "Spojení se nezdařilo", JOptionPane.WARNING_MESSAGE);
            System.err.println("Vyjimka v NetConnect.startServer(): "+e.toString());
            return false;
        }
        return true;
    }
    
    /**
     * Ukončení naslouchání na portu pro umožnění připojení ze sítě
     */
    public void stopServer(){
        try{
            server.close();
        }
        catch(IOException e){
            System.err.println("Varovani: Nepodarilo se korektne ukoncit naslouchani na portu!");
        }
        server = null;
    }
    
    /**
     * Navázání spojení se serverem v síti
     * @param hostname Adresa serveru
     * @param port Port serveru
     * @return Zdali se spojení zdařilo
     */
    public boolean startClient(String hostname, int port){
        if(client!=null){
            stopClient();
        }
        try {
            client = new Socket(hostname, port);
            netlink = new NetLink(client, desk, true, false);
            dialog.setVisible(false);
            dialog.dispose();
            desk.setNetLink(netlink);
            netlink.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Game.getWindow(), "Spojení se nezdařilo:\n"+e.toString(),
               "Spojení se nezdařilo", JOptionPane.WARNING_MESSAGE);
            System.err.println(e.toString());
            return false;
        }
        return true;
    }
    
    private void stopClient(){
        try{
            client.close();
        }
        catch(IOException e){
            System.err.println("Varovani: Nepodarilo se korektne ukoncit klientsky socket!");
        }
        client = null;
    }
}
