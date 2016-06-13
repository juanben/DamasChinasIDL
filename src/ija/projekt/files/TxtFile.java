/**
 * IJA - projekt 2013
 * soubor: TxtFile.java
 * Implementace vystupu do souboru v zakladni notaci
 * 
 * Autori:
 *         @author Michal Dobes (xdobes13)
 *         @author Jan Kalina   (xkalin03)
 */

package ija.projekt.files;
import ija.projekt.base.*;
import java.io.*;
import java.util.Scanner;

/**
 * Práce se souborem v základní notaci
 * @author Jan Kalina <xkalin03@stud.fit.vutbr.cz>
 * @author Michal Dobes <xdobes13@stud.fit.vutbr.cz>
 */
public class TxtFile extends javax.swing.filechooser.FileFilter {
    
    /**
     * Je soubor s danou adresou v této notaci?
     * @param path Adresa souboru
     * @return Zda-li je v této notaci
     */
    public static boolean acceptable(File path){
        if (path.isDirectory()) {return true;}
        return (path.getAbsolutePath().endsWith(".txt"));
    }
    
    /**
     * Je soubor s danou adresou v této notaci?
     * @param path Adresa souboru
     * @return Zda-li je v této notaci
     */
    @Override public boolean accept(File path){
        return acceptable(path);
    }
    
    /**
     * Popis typu souboru ve JFileChooseru
     * @return Popis typu
     */
    @Override public String getDescription(){
        return "Základní notace (*.txt)";
    }
    
    /**
     * Načtení partie ze souboru
     * @param path Adresa souboru
     * @param history Historie tahů
     * @throws FileNotFoundException Pokud soubor nebyl nalezen
     * @throws IOException Při chybě čtení souboru
     * @throws ija.projekt.base.Move.MoveException Při chybném tahu v souboru
     */
    public static void loadFile(File path, History history)
            throws FileNotFoundException, IOException, Move.MoveException {
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        
        history.getDesk().newGame();
        
        String line; // 1. c3-b4 b6-c5
        while((line = br.readLine()) != null){
            Scanner scanner = new Scanner(line);
            scanner.next(); // prvni slovo radku (1.) ignorovat
            
            while(scanner.hasNext()){ // vsechna ostatni slova (c3-b4)
                String temp = scanner.next();
                history.addFromString(temp); // pridat jako tahy
            }
        }
        
        br.close();
        fr.close();
    }
    
    /**
     * Uložení partie do souboru
     * @param path Adresa souboru
     * @param history Historie tahů
     * @throws FileNotFoundException Pokud soubor ne
     * @throws IOException Při chybě zápisu do souboru
     */
    public static void saveFile(File path, History history)
            throws IOException {
        FileWriter fw = new FileWriter(path);
        for(int i = 0; i*2 < history.getCount(); i++){
            fw.write(""+(i+1)+". "+history.getItem(i*2).toString());
            if(i*2+1 < history.getCount()){
                fw.write(" "+history.getItem(i*2+1).toString());
            }
            fw.write("\n");
        }
        fw.close();
    }
}
