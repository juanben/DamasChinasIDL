

package red;
import java.net.*;
import java.nio.ByteBuffer;
import java.io.*;

import javax.swing.JOptionPane;

import base.*;


public class NetLink extends Thread {
    
    private Socket socket;
    private BufferedInputStream is;
    private BufferedOutputStream os;
    private Desk desk;
    private boolean allowInit = false;
    private boolean whitePlayer = false;
    private boolean killThread = false;
    
    
    NetLink(Socket socket, Desk desk, boolean allowInit, boolean white){
        this.socket = socket;
        this.desk = desk;
        this.allowInit = allowInit;
        this.whitePlayer = white;
    }
    
    
    @Override public void run(){
        System.out.println("Pripojen klient z "+socket.toString());
        listen();
        System.out.println("Odpojen klient z "+socket.toString());
    }
    
    
    private void listen(){
        try{
            while(true){
                switch(readByte()){
                    case 1: case '1': 
                    {
                        byte x1 = readSmallNumber((byte)7);
                        byte y1 = readSmallNumber((byte)7);
                        byte x2 = readSmallNumber((byte)7);
                        byte y2 = readSmallNumber((byte)7);
                        acceptMove(x1,y1,x2,y2,false);
                    }
                    break;
                    case 2: case '2': 
                    {
                        if(!allowInit){ 
                            System.err.println("Inicialiacion no autorizada en el sitio");
                            JOptionPane.showMessageDialog(null, "Clientes de red trataron inicio ilegal",
                               "Error de red del cliente", JOptionPane.ERROR_MESSAGE);
                            
                            
                            readSmallNumber((byte)1); 
                            int pocetTahu = readInteger();
                            for( int i = 0 ; i < pocetTahu*4 ; i++ ){
                                readSmallNumber((byte)7);
                            }
                            
                        }else{
                            System.out.println("Inicializacion sentada");
                            allowInit = false; 
                            desk.newGame(); 
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
                    case 3: case '3': 
                    {
                        System.out.println("Acepto despedida");
                        closeSocket();
                        afterClose();
                    }
                    return;
                    default:
                    {
                        System.out.println("Acepto mensaje ignorado");
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
                        "Por otro lado a terminado la conexion",
                        "El juego de red termino",
                        JOptionPane.INFORMATION_MESSAGE);
            }else{
                System.err.println(e.toString());
                JOptionPane.showMessageDialog(null,
                        "Se produjo un error de comunicacion de red!",
                        "Juego de red se interrumpe",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
    private void acceptMove(byte x1, byte y1, byte x2, byte y2, boolean init){
        try{
            Position source = desk.getPositionAt(x1,y1);
            Position destination = desk.getPositionAt(x2,y2);
            desk.getHistory().goToPresent();
            
            if(source.getFigure() == null){
                System.err.println("Requerido por la red movimiento en un campo vacio! ("+x1+","+y1+")("+x2+","+y2+")");
                JOptionPane.showMessageDialog(null, "Requerid para pasar de un campo vacio!", "Error de cliente", JOptionPane.ERROR_MESSAGE);
            }else if(!init && source.getFigure().getPlayer().type() != Player.Type.REMOTE){
                System.err.println("Requiere de avance! ("+x1+","+y1+")("+x2+","+y2+")");
                JOptionPane.showMessageDialog(null, "Compañero de red trato de hacer algo extraño!", "Error!", JOptionPane.ERROR_MESSAGE);
            }else if(!init && desk.getPlayer().type() != Player.Type.REMOTE){
                System.err.println("Requiere avance! ("+x1+","+y1+")("+x2+","+y2+")");
                JOptionPane.showMessageDialog(null, "Trataron de tirar dos veces›!", "Error", JOptionPane.ERROR_MESSAGE);
            }else if(source.getFigure().canCapture(destination)){
                source.getFigure().capture(destination);
                desk.getHistory().addCapture(source, destination);
                desk.nextPlayer();
            }else if(source.getFigure().canMove(destination)){
                source.getFigure().move(destination);
                desk.getHistory().addMove(source, destination);
                desk.nextPlayer();
            }else{
                System.err.println("Requiere de avance de reglas! ("+x1+","+y1+")("+x2+","+y2+")");
                JOptionPane.showMessageDialog(null, "Trataron de ir contra las reglas!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch(Exception e){
            System.err.println("Excepcion al intentar dar un salto ("+x1+","+y1+")("+x2+","+y2+")");
            e.printStackTrace(System.err);
            JOptionPane.showMessageDialog(null, "Error de cliente!", "Hubo una excepcion!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    public synchronized void send(byte[] buffer) throws NetException {
        try{
            socket.getOutputStream().write(buffer);
        }
        catch(IOException e){
            throw new NetException("Nelze zapisovat do socketu: "+e.toString());
        }
    }
    
    
    public void sendMove(Position source, Position destination) throws NetException {
        byte[] buffer = new byte[5];
        buffer[0] = 0x01;
        buffer[1] = source.getColumn();
        buffer[2] = source.getRow();
        buffer[3] = destination.getColumn();
        buffer[4] = destination.getRow();
        send(buffer);
    }
    
    
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
    
    
    private byte[] readBytes(int count) throws NetException {
        byte[] buffer = new byte[count];
        int readed = 0;
        try{
            readed = socket.getInputStream().read(buffer, 0, count);
        }
        catch(IOException e){
            throw new NetException("Error al leer el socket! "+e.toString());
        }
        if(readed < count){
            throw new NetException("No se recibe la cantidad necesaria!");
        }
        return buffer;
    }
    
    
    private byte readByte() throws NetException {
        byte[] b = readBytes(1);
        return b[0];
    }
    
    
    private byte readSmallNumber(byte maximum) throws NetException {
        assert maximum < '0' : "readSmallNumber es un numero de una sola cifra para lectura!";
        byte b = readByte();
        if(b >= '0') b -= '0';
        if(b < 0 || b > maximum){
            throw new NetException("Valores recibidos fuera de rango <0,"+maximum+">: "+String.format("%02X", b&0xff));
        }
        return b;
    }
    
    
    private int readInteger() throws NetException {
        return ByteBuffer.wrap(readBytes(4)).getInt();
    }
    
    
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
    
    
    private void closeSocket(){
        killThread = true;
        try{
            socket.close();
        }
        // ignorovat chyby pri zavirani
        catch(Exception e){}
    }
    
    
    private void afterClose(){
        desk.getWhitePlayer().settype(Player.Type.HUMAN);
        desk.getBlackPlayer().settype(Player.Type.HUMAN);
        desk.setNetLink(null);
        Game.getWindow().update();
    }
    
    
    void setAllowInit(boolean allow){
        allowInit = allow;
    }
    
    
    @Override protected void finalize() throws Throwable{
        this.close();
        super.finalize();
    }
}
