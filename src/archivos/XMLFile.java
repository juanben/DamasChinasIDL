

package archivos;
import java.io.*;
import java.util.Iterator;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import base.History;


public class XMLFile extends javax.swing.filechooser.FileFilter {
    
    
    public static boolean acceptable(File path){
        if (path.isDirectory()) {return true;}
        return (path.getAbsolutePath().endsWith(".xml"));
    }
    
    
    @Override public boolean accept(File path){
        return acceptable(path);
    }
    
    
    @Override public String getDescription(){
        return "Dentro de la notacion (*.xml)";
    }
    
    
    public static void loadFile(File path, History history)
            throws FileNotFoundException, IOException, DocumentException {
        
        SAXReader reader = new SAXReader();
        Document doc = reader.read(path);
        Element root = doc.getRootElement();
        
        history.getDesk().newGame();
        
        for ( Iterator i = root.elementIterator(); i.hasNext(); ) {
            Element el = (Element) i.next();
            switch (el.getName()) {
                case "move":
                {
                    int from_col = Integer.parseInt(el.attributeValue("from_col"));
                    int from_row = Integer.parseInt(el.attributeValue("from_row"));
                    int to_col = Integer.parseInt(el.attributeValue("to_col"));
                    int to_row = Integer.parseInt(el.attributeValue("to_row"));
                    
                    history.addMove(history.getDesk().getPositionAt(from_col, from_row), 
                            history.getDesk().getPositionAt(to_col, to_row));
                }
                    break;
                case "capt":
                {
                    int from_col = Integer.parseInt(el.attributeValue("from_col"));
                    int from_row = Integer.parseInt(el.attributeValue("from_row"));
                    int to_col = Integer.parseInt(el.attributeValue("to_col"));
                    int to_row = Integer.parseInt(el.attributeValue("to_row"));
                    history.addCapture(history.getDesk().getPositionAt(from_col, from_row), 
                            history.getDesk().getPositionAt(to_col, to_row));
                }
                    break;
            }
        }
    }
    
    
    public static void saveFile(File path, History history)
            throws FileNotFoundException, IOException {
        FileWriter fw = new FileWriter(path);
        
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("game");
        
        
        for(int i = 0; i < history.getCount(); i++)
        {
            Element newEl;
            if (history.getItem(i).isCapture())
            {
                newEl = root.addElement("capt");
            }
            else
            {
                newEl = root.addElement("move");
            }
            
            newEl.addAttribute("from_col", ""+history.getItem(i).getX1());
            newEl.addAttribute("from_row", ""+history.getItem(i).getY1());
            newEl.addAttribute("to_col", ""+history.getItem(i).getX2());
            newEl.addAttribute("to_row", ""+history.getItem(i).getY2());
        }
        
        
        final StringWriter sw;

        try {
            final OutputFormat format = OutputFormat.createPrettyPrint();
            sw = new StringWriter();
            final XMLWriter writer = new XMLWriter(sw, format);
            writer.write(doc);
        }
        catch (Exception e) {
            throw new RuntimeException("Error de impresion xml", e);
        }
        
        fw.write(sw.toString());
        fw.close();
    }
}
