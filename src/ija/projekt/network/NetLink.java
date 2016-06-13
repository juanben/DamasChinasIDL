/**
 * IJA - projekt 2013
 * soubor: NetLink.java
 * Implementace sitoveho spojeni
 * 
 * Autori:
 *         @author Michal Dobes (xdobes13)
 *         @author Jan Kalina   (xkalin03)
 */

package ija.projekt.network;
import java.net.*;
import java.nio.ByteBuffer;
import java.io.*;
import ija.projekt.base.*;
import javax.swing.JOptionPane;

/**
 * Vlákno obsluhující spojení se síťovým klientem
 * @author Jan Kalina <xkalin03@stud.fit.vutbr.cz>
 * @author Michal Dobes <xdobes13@stud.fit.vutbr.cz>
 */
public class NetLink extends Thread {
    
    private Socket socket;
    private BufferedInputStream is;
    private BufferedOutputStream os;
    private Desk desk;
    private boolean allowInit = false;
    private boolean whitePlayer = false;
    private boolean killThread = false;
    
    /**
     * Konstruktor obsluhujícího vlákna (volaný výhradně z NetConnect)
     * @param socket Socket pro komunikaci s druhou stranou spojení
     * @param desk Hrací plocha
     * @param allowInit Povolit inicializaci druhou stranou? (true pro klienta)
     * @param white Hraje tato strana za bílé? (A síťový klient za černé?)
     */
    NetLink(Socket socket, Desk desk, boolean allowInit, boolean white){
        this.socket = socket;
        this.desk = desk;
        this.allowInit = allowInit;
        this.whitePlayer = white;
    }
    
    /**
     * Tělo vlákna
     */
    @Override public void run(){
        System.out.println("Pripojen klient z "+socket.toString());
        listen();
        System.out.println("Odpojen klient z "+socket.toString());
    }
    
    /**
     * Naslouchání druhé straně spojení
     */
    private void listen(){
        try{
            while(true){
                switch(readByte()){
                    case 1: case '1': // Tah
                    {
                        byte x1 = readSmallNumber((byte)7);
                        byte y1 = readSmallNumber((byte)7);
                        byte x2 = readSmallNumber((byte)7);
                        byte y2 = readSmallNumber((byte)7);
                        acceptMove(x1,y1,x2,y2,false);
                    }
                    break;
                    case 2: case '2': // Inicializace
                    {
                        if(!allowInit){ // nepovolena inicializace
                            System.err.println("Nepovolena inicializace ze site!");
                            JOptionPane.showMessageDialog(null, "Klient síťového spoluhráče se pokusil neoprávněně inicializovat šachovnici!",
                               "Chyba klienta síťového spoluhráče!", JOptionPane.ERROR_MESSAGE);
                            
                            // preskoceni pro ladeni s jinymi klienty
                            readSmallNumber((byte)1); // preskocit white/black
                            int pocetTahu = readInteger();
                            for( int i = 0 ; i < pocetTahu*4 ; i++ ){
                                readSmallNumber((byte)7);
                            }
                            
                        }else{ // povolena inicializace
                            System.out.println("Probehla inicializace ze site");
                            allowInit = false; // vickrat uz ne
                            desk.newGame(); // resetovat sachovnici
                            whitePlayer = ( readSmallNumber((byte)1)==1 ? true : false );
                            int pocetTahu = readInteger();
                            for( int i = 0 ; i < pocetTahu ; i++ ){
                                byte x1 = readSmallNumber((byte)7);
                                byte y1 = readSmallNumber((byte)7);
                                byte x2 = readSmallNumber((byte)7);
                                byte y2 = readSmallNumber((byte)7);
                                acceptMove(x1,y1,x2,y2,true);
                            }
                            
                            System.out.println("Client: whitePlayer="+whitePlayer);
                            if (whitePlayer){
                                desk.getBlackPlayer().settype(Player.Type.REMOTE);
                            }else{
                                desk.getWhitePlayer().settype(Player.Type.REMOTE);
                            }
                            Game.getWindow().update();
                        }
                    }
                    break;
                    case 3: case '3': // Ukonceni spojeni
                    {
                        System.out.println("Prijmam rozlouceni");
                        closeSocket();
                        afterClose();
                    }
                    return; // vybreaknout z cele smycky
                    default:
                    {
                        System.out.println("Prijmam ignorovanou zpravu");
                        int size = readInteger();
                        readBytes(size);
                    }
                    break;
                }
            }
        }
        catch(NetException e){
            if(killThread){
                afterClose();
                JOptionPane.showMessageDialog(null,
                        "Druhá strana ukončila síťové spojení.",
                        "Síťová hra ukončena",
                        JOptionPane.INFORMATION_MESSAGE);
            }else{
                System.err.println(e.toString());
                JOptionPane.showMessageDialog(null,
                        "Došlo k chybě síťové komunikace!",
                        "Síťová hra přerušena",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Příjem tahu ze sítě
     */
    private void acceptMove(byte x1, byte y1, byte x2, byte y2, boolean init){
        try{
            Position source = desk.getPositionAt(x1,y1);
            Position destination = desk.getPositionAt(x2,y2);
            desk.getHistory().goToPresent();
            
            if(source.getFigure() == null){
                System.err.println("Siti vyzadovan tah z prazdneho pole! ("+x1+","+y1+")("+x2+","+y2+")");
                JOptionPane.showMessageDialog(null, "Vyžadován tah z prázného pole!", "Chyba klienta síťového spoluhráče!", JOptionPane.ERROR_MESSAGE);
            }else if(!init && source.getFigure().getPlayer().type() != Player.Type.REMOTE){
                System.err.println("Siti vyzadovan tah s cizi figurkou! ("+x1+","+y1+")("+x2+","+y2+")");
                JOptionPane.showMessageDialog(null, "Klient síťového spoluhráče se pokusil táhnout s cizí figurkou!", "Chyba klienta síťového spoluhráče!", JOptionPane.ERROR_MESSAGE);
            }else if(!init && desk.getPlayer().type() != Player.Type.REMOTE){
                System.err.println("Siti vyzadovan tah kdyz nebyla sit na tahu! ("+x1+","+y1+")("+x2+","+y2+")");
                JOptionPane.showMessageDialog(null, "Klient síťového spoluhráče se pokusil táhnout dvakrát po sobě!", "Chyba klienta síťového spoluhráče!", JOptionPane.ERROR_MESSAGE);
            }else if(source.getFigure().canCapture(destination)){
                source.getFigure().capture(destination);
                desk.getHistory().addCapture(source, destination);
                desk.nextPlayer();
            }else if(source.getFigure().canMove(destination)){
                source.getFigure().move(destination);
                desk.getHistory().addMove(source, destination);
                desk.nextPlayer();
            }else{
                System.err.println("Siti vyzadovan tah proti pravidlum! ("+x1+","+y1+")("+x2+","+y2+")");
                JOptionPane.showMessageDialog(null, "Klient síťového spoluhráče se pokusil táhnout v rozporu s pravidly!", "Chyba klienta síťového spoluhráče!", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch(Exception e){
            System.err.println("Vyjimka pri pokusu o skok vyzadovany siti ("+x1+","+y1+")("+x2+","+y2+")");
            e.printStackTrace(System.err);
            JOptionPane.showMessageDialog(null, "Chyba klienta síťového spoluhráče!", "Nastala vyjímka při pokusu o sítí vyžádaný skok!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Odeslání obecných dat druhé straně
     * @param buffer Odesílaná data
     * @throws NetException Jestliže se data nepodařilo odeslat
     */
    public synchronized void send(byte[] buffer) throws NetException {
        try{
            socket.getOutputStream().write(buffer);
        }
        catch(IOException e){
            throw new NetException("Nelze zapisovat do socketu: "+e.toString());
        }
    }
    
    /**
     * Odeslání tahu druhé straně
     * @param source Pole kde se herní kámen nachází nyní
     * @param destination Pole kde se bude herní kámen nacházet po provedení tahu
     * @throws NetException Jestliže se tah nepodařilo odeslat
     */
    public void sendMove(Position source, Position destination) throws NetException {
        byte[] buffer = new byte[5];
        buffer[0] = 0x01;
        buffer[1] = source.getColumn();
        buffer[2] = source.getRow();
        buffer[3] = destination.getColumn();
        buffer[4] = destination.getRow();
        send(buffer);
    }
    
    /**
     * Odeslání inicializace herní plochy
     * @throws NetException Jestliže se inicializaci nepodařilo odeslat
     */
    public void sendInit() throws NetException {
        int count = desk.getHistory().getCount();
        byte[] buffer = new byte[6+4*count];
        buffer[0] = 0x02;
        buffer[1] = ( whitePlayer ? (byte)0 : (byte)1 );
        
        System.out.println("Server: whitePlayer="+whitePlayer+", sending"+buffer[1]);
        
        byte[] length = ByteBuffer.allocate(4).putInt(count).array();
        for( byte i=0 ; i<4 ; i++ ) buffer[i+2] = length[i];
        
        for( int i=0 ; i<count ; i++ ){
            Move item = desk.getHistory().getItem(i);
            buffer[6+i*4+0] = item.getX1();
            buffer[6+i*4+1] = item.getY1();
            buffer[6+i*4+2] = item.getX2();
            buffer[6+i*4+3] = item.getY2();
        }
        
        send(buffer);
    }
    
    /**
     * Přečíst zadaný počet bytů ze sítě
     * @param count Počet bytů
     * @return Načtené byty
     * @throws NetException Jestliže se načtení nezdaří
     */
    private byte[] readBytes(int count) throws NetException {
        byte[] buffer = new byte[count];
        int readed = 0;
        try{
            readed = socket.getInputStream().read(buffer, 0, count);
        }
        catch(IOException e){
            throw new NetException("Chyba pri cteni ze socketu! "+e.toString());
        }
        if(readed < count){
            throw new NetException("Neobdrzen vyzadovany pocet bytu!");
        }
        return buffer;
    }
    
    /**
     * Přečíst 1 byte ze sítě
     * @return Přečtený byte
     * @throws NetException Jestliže se načtení nezdaří
     */
    private byte readByte() throws NetException {
        byte[] b = readBytes(1);
        return b[0];
    }
    
    /**
     * Přečíst malé (v desítkové soustavě jednociferné) číslo ze sítě
     * Používá se pro usnadění ladění pomocí telnetu - mimo
     * 0x01 pak server akceptuje také '1'
     */
    private byte readSmallNumber(byte maximum) throws NetException {
        assert maximum < '0' : "readSmallNumber je urcen jen ke cteni jednocifernych cisel!";
        byte b = readByte();
        if(b >= '0') b -= '0';
        if(b < 0 || b > maximum){
            throw new NetException("Obdrzena hodnota mimo rozsah <0,"+maximum+">: "+String.format("%02X", b&0xff));
        }
        return b;
    }
    
    /**
     * Přečíst integer ze sítě
     * @return Přečtený integer
     * @throws NetException Jestliže se načtení nezdaří
     */
    private int readInteger() throws NetException {
        return ByteBuffer.wrap(readBytes(4)).getInt();
    }
    
    /**
     * Korektní uzavření spojení s druhou stranou
     */
    public void close(){
        killThread = true;
        try{
            byte buffer[] = {0x03};
            send(buffer);
        }
        // ignorovat chyby pri zavirani
        catch(NetException e){}
        closeSocket();
        afterClose();
    }
    
    /**
     * Uzavření socketu
     */
    private void closeSocket(){
        killThread = true;
        try{
            socket.close();
        }
        // ignorovat chyby pri zavirani
        catch(Exception e){}
    }
    
    /**
     * Událost po uzavření spojení
     */
    private void afterClose(){
        desk.getWhitePlayer().settype(Player.Type.HUMAN);
        desk.getBlackPlayer().settype(Player.Type.HUMAN);
        desk.setNetLink(null);
        Game.getWindow().update();
    }
    
    /**
     * Nastavení, zdali je inicializace hrací plochy druhou stranou povolena
     * @param allow Povolit
     */
    void setAllowInit(boolean allow){
        allowInit = allow;
    }
    
    /**
     * Destruktor objektu uzavírající síťové spojení
     */
    @Override protected void finalize() throws Throwable{
        this.close();
        super.finalize();
    }
}
