/**
 * IJA - projekt 2013
 * soubor: DeskWithQueenTest.java
 * Test pohybu damy
 * 
 * Autori:
 *         @author Michal Dobes (xdobes13)
 *         @author Jan Kalina   (xkalin03)
 */

package ija.projekt;
import ija.projekt.base.*;
import ija.projekt.figures.*;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JList;

/**
 * Test hraci plochy, kde se jiz vyskytuje dama
 * @author Jan Kalina <xkalin03@stud.fit.vutbr.cz>
 * @author Michal Dobes <xdobes13@stud.fit.vutbr.cz>
 */
public class DeskWithQueenTest {
    
    private Desk desk;
    
    public DeskWithQueenTest() {
        desk = new Desk();
        History history = new History();
        desk.setHistory(history);
        history.setDeskJListAndPlayButton(desk, new JList(), new JButton());
        desk.newGame();
        
        desk.getPositionAt(4,2).removeFigure();
        desk.getPositionAt(6,6).removeFigure();
        desk.getPositionAt(6,0).removeFigure();
        desk.getPositionAt(3,3).placeFigure(new Queen(desk.getPositionAt(3,3),desk.getWhitePlayer()));
        desk.getPositionAt(5,3).placeFigure(new Stone(desk.getPositionAt(5,3),desk.getBlackPlayer()));
        
        desk.getPositionAt(5, 1).removeFigure();
        desk.getPositionAt(5, 1).placeFigure(new Stone(desk.getPositionAt(5,1), desk.getBlackPlayer()));
    }
    
    @Test public void testCanMoveQueenFromD4() {
        Position p1 = desk.getPositionAt(3,3);
        HashSet<Position> allowed = new HashSet();
        allowed.add(desk.getPositionAt(2,4));
        allowed.add(desk.getPositionAt(4,4));
        allowed.add(desk.getPositionAt(4,2));
        
        for( int column=0 ; column<8 ; column++ ){
            for( int row=0 ; row<8 ; row++ ){
                Position p2 = desk.getPositionAt(column,row);
                if(allowed.contains(p2)){
                    assertTrue("Povoleni korektniho tahu "+p1.toString()+"-"+p2.toString(), p1.getFigure().canMove(p2));
                }else{
                    assertFalse("Nepovoleni nekorektniho tahu "+p1.toString()+"-"+p2.toString(), p1.getFigure().canMove(p2));
                }
            }
        }
    }
    
    @Test public void testCanCaptureWithStoneFromG3() {
        Position p1 = desk.getPositionAt(6,2);
        HashSet<Position> allowed = new HashSet();
        allowed.add(desk.getPositionAt(4,4));
        
        for( int column=0 ; column<8 ; column++ ){
            for( int row=0 ; row<8 ; row++ ){
                Position p2 = desk.getPositionAt(column,row);
                if(allowed.contains(p2)){
                    assertTrue("Povoleni korektniho tahu "+p1.toString()+"x"+p2.toString(), p1.getFigure().canCapture(p2));
                }else{
                    assertFalse("Nepovoleni nekorektniho tahu "+p1.toString()+"x"+p2.toString(), p1.getFigure().canCapture(p2));
                }
            }
        }
    }
    
    @Test public void testCanCaptureWithQueenFromD4() {
        Position p1 = desk.getPositionAt(3,3);
        HashSet<Position> allowed = new HashSet();
        allowed.add(desk.getPositionAt(6,6));
        allowed.add(desk.getPositionAt(6,0));
        
        for( int column=0 ; column<8 ; column++ ){
            for( int row=0 ; row<8 ; row++ ){
                Position p2 = desk.getPositionAt(column,row);
                if(allowed.contains(p2)){
                    assertTrue("Povoleni korektniho tahu "+p1.toString()+"x"+p2.toString(), p1.getFigure().canCapture(p2));
                }else{
                    assertFalse("Nepovoleni nekorektniho tahu "+p1.toString()+"x"+p2.toString(), p1.getFigure().canCapture(p2));
                }
            }
        }
    }
    
}