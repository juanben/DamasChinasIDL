/**
 * IJA - projekt 2013
 * soubor: NetException.java
 * Vyjimka pro sitove spojeni.
 * 
 * Autori:
 *         @author Michal Dobes (xdobes13)
 *         @author Jan Kalina   (xkalin03)
 */

package ija.projekt.network;

/**
 * Vyjímka představující chybu při práci se sítí
 * @author Jan Kalina <xkalin03@stud.fit.vutbr.cz>
 * @author Michal Dobes <xdobes13@stud.fit.vutbr.cz>
 */
public class NetException extends Exception {
    public NetException(String message){
        super(message);
    }
}
