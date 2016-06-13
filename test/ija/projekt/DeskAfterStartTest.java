/**
 * IJA - projekt 2013
 * soubor: DeskAfterStartTest.java
 * Test pocatecniho stavu hraci plochy
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
import javax.swing.JList;
import javax.swing.JButton;

/**
 * Test hraci plochy v pocatecnim usporadani
 * @author Jan Kalina <xkalin03@stud.fit.vutbr.cz>
 * @author Michal Dobes <xdobes13@stud.fit.vutbr.cz>
 */
public class DeskAfterStartTest {
    
    private Desk desk;
    
    public DeskAfterStartTest() {
        desk = new Desk();
        History history = new History();
        desk.setHistory(history);
        history.setDeskJListAndPlayButton(desk, new JList(), new JButton());
        desk.newGame();
    }
    
    @Test public void testReadyE3F4() {
        Position p;
        p = desk.getPositionAt(4,2);
        assertNotNull("Na pozici "+p.toString()+" by mela lezet figurka", p.getFigure());
        assertTrue("Figurka na pozici "+p.toString()+" by mela byt kamen", p.getFigure() instanceof Stone);
        assertTrue("Figurka by mela byt korektne provazana s pozici", p.getFigure().isAtPosition(p));
        p = desk.getPositionAt(5,3);
        assertNull("Na pozici "+p.toString()+" by nemela lezet figurka", p.getFigure());
        p = desk.getPositionAt(3,3);
        assertNull("Na pozici "+p.toString()+" by nemela lezet figurka", p.getFigure());
    }
    
    @Test public void testCanMoveFromE3() {
        Position p1 = desk.getPositionAt(4,2);
        HashSet<Position> allowed = new HashSet();
        allowed.add(desk.getPositionAt(3,3));
        allowed.add(desk.getPositionAt(5,3));
        
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
    
    @Test public void testMoveE3F4() {
        Position p1 = desk.getPositionAt(4,2);
        Position p2 = desk.getPositionAt(5,3);
        Figure f = p1.getFigure();
        f.move(p2);
        assertTrue("Figurka by mela byt provazana s novou pozici",p2.getFigure().getPosition()==p2);
        assertNull("Puvodni pozice by mela byt uvolnena",p1.getFigure());
    }
    
    @Test public void testCanCaptureFromE3() {
        Position p1 = desk.getPositionAt(4,2);
        for( int column=0 ; column<8 ; column++ ){
            for( int row=0 ; row<8 ; row++ ){
                Position p2 = desk.getPositionAt(column,row);
                assertFalse("Nepovoleni nekorektniho tahu "+p1.toString()+"x"+p2.toString(), p1.getFigure().canCapture(p2));
            }
        }
    }
    
}