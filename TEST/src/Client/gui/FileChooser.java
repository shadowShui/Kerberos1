package Client.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class FileChooser extends JFrame{  

    public String getSelected(){  
         // TODO Auto-generated method stub  
        JFileChooser jfc=new JFileChooser();  
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);  
        jfc.showDialog(new JLabel(), "Ñ¡Ôñ");
        String res = "";
        try{
            File file=jfc.getSelectedFile();  
            if(file.isDirectory()){  
            	return "";
                // res += file.getAbsolutePath();  
            }else if(file.isFile()){  
                res += file.getAbsolutePath();  
            }  
            // res += jfc.getSelectedFile().getName();
            return res;
        }
        catch (Exception e) {
            //e.printStackTrace();
            return "";
        }
    } 
}  