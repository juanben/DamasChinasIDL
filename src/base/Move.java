

package base;


public class Move {
    private Position source;
    private Position destination;
    private boolean capture;

    
    public Move(Position s, Position d, boolean c) {
        source = s;
        destination = d;
        capture = c;
    }

    
    public byte getX1() {
        return source.getColumn();
    }

    
    public byte getY1() {
        return source.getRow();
    }

    
    public byte getX2() {
        return destination.getColumn();
    }

    
    public byte getY2() {
        return destination.getRow();
    }

    
    public Position getSource() {
        return source;
    }

    
    public Position getDestination() {
        return destination;
    }

    
    public boolean isCapture() {
        return capture;
    }

    
    @Override
    public String toString() {
        return source.toString() + (capture ? "x" : "-") + destination.toString();
    }

    
    public Move(Desk desk, String s) throws MoveException {
        if ((s.length() != 5) || (s.charAt(0) < 'a' || s.charAt(0) > 'h') || (s.charAt(1) < '1' || s.charAt(1) > '8') || (s.charAt(2) != '-' && s.charAt(2) != 'x') || (s.charAt(3) < 'a' || s.charAt(3) > 'h') || (s.charAt(4) < '1' || s.charAt(4) > '8')) {
            throw new MoveException("Movimiento no registrado en la notacion correcta");
        }
        if (desk == null) {
            throw new MoveException("Disco nulo");
        }
        
        source = desk.getPositionAt((int) s.charAt(0) - 'a', (int) s.charAt(1) - '1');
        capture = (s.charAt(2) == 'x');
        destination = desk.getPositionAt((int) s.charAt(3) - 'a', (int) s.charAt(4) - '1');
    }
    
    
    public class MoveException extends Exception {
        public MoveException(String message){
            super(message);
        }
    }
}
