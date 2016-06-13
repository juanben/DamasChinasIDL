

package archivos;
import java.io.*;
import java.util.Scanner;

import base.*;


public class TxtFile extends javax.swing.filechooser.FileFilter {
    
    
    public static boolean acceptable(File path){
        if (path.isDirectory()) {return true;}
        return (path.getAbsolutePath().endsWith(".txt"));
    }
    
    
    @Override public boolean accept(File path){
        return acceptable(path);
    }
    
    
    @Override public String getDescription(){
        return "ZÃ¡kladnÃ­ notace (*.txt)";
    }
    
    
    public static void loadFile(File path, History history)
            throws FileNotFoundException, IOException, Move.MoveException {
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        
        history.getDesk().newGame();
        
        String line; 
        while((line = br.readLine()) != null){
            Scanner scanner = new Scanner(line);
            scanner.next(); 
            
            while(scanner.hasNext()){ 
                String temp = scanner.next();
                history.addFromString(temp); 
            }
        }
        
        br.close();
        fr.close();
    }
    
    
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
