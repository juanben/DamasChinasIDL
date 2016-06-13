/**
 * IJA - projekt 2013
 * soubor: Move.java
 * Trida reprezentujici pohyb figurky
 * 
 * Autori:
 *         @author Michal Dobes (xdobes13)
 *         @author Jan Kalina   (xkalin03)
 */

package ija.projekt.base;

/**
 * Tah (presun nebo brani)
 * @author Jan Kalina <xkalin03@stud.fit.vutbr.cz>
 * @author Michal Dobes <xdobes13@stud.fit.vutbr.cz>
 */
public class Move {
    private Position source;
    private Position destination;
    private boolean capture;

    /**
     * Konstruktor
     * @param s počáteční pozice
     * @param d cílová pozice
     * @param c dochází při tahu k braní soupeřovy figury?
     */
    public Move(Position s, Position d, boolean c) {
        source = s;
        destination = d;
        capture = c;
    }

    /**
     * Získá sloupec počáteční souřadnice
     * @return sloupec počáteční souřadnice (0-7)
     */
    public byte getX1() {
        return source.getColumn();
    }

    /**
     * Získá řádek počáteční souřadnice
     * @return řádek počáteční souřadnice (0-7)
     */
    public byte getY1() {
        return source.getRow();
    }

    /**
     * Získá sloupec cílové souřadnice
     * @return sloupec cílové souřadnice (0-7)
     */
    public byte getX2() {
        return destination.getColumn();
    }

    /**
     * Získá řádek cílové souřadnice
     * @return řádek cílové souřadnice (0-7)
     */
    public byte getY2() {
        return destination.getRow();
    }

    /**
     * Získá počáteční pozici
     * @return počáteční pozice
     */
    public Position getSource() {
        return source;
    }

    /**
     * Získá cílovou pozici
     * @return cílová pozice
     */
    public Position getDestination() {
        return destination;
    }

    /**
     * Zjistí, jestli při tahu dochází k braní
     * @return true pokud při tahu dochází k braní, jinak false
     */
    public boolean isCapture() {
        return capture;
    }

    /**
     * Převede tah do standardní notace
     * @return tah ve standardní notaci
     */
    @Override
    public String toString() {
        return source.toString() + (capture ? "x" : "-") + destination.toString();
    }

    /**
     * Konstruktor
     * @param desk deska, na které je tah prováděn
     * @param s tah ve standardní notaci
     * @throws ija.projekt.base.Move.MoveException
     */
    public Move(Desk desk, String s) throws MoveException {
        if ((s.length() != 5) || (s.charAt(0) < 'a' || s.charAt(0) > 'h') || (s.charAt(1) < '1' || s.charAt(1) > '8') || (s.charAt(2) != '-' && s.charAt(2) != 'x') || (s.charAt(3) < 'a' || s.charAt(3) > 'h') || (s.charAt(4) < '1' || s.charAt(4) > '8')) {
            throw new MoveException("Tah není zapsán ve správné notaci!");
        }
        if (desk == null) {
            throw new MoveException("Desk je null!");
        }
        // samotna konverze
        source = desk.getPositionAt((int) s.charAt(0) - 'a', (int) s.charAt(1) - '1');
        capture = (s.charAt(2) == 'x');
        destination = desk.getPositionAt((int) s.charAt(3) - 'a', (int) s.charAt(4) - '1');
    }
    
    /**
     * Třída vyjímek pro třídu tah
     */
    public class MoveException extends Exception {
        public MoveException(String message){
            super(message);
        }
    }
}
