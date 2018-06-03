package Client.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class DownloadChooser extends JFrame{  

    public String getSelected(){  
         // TODO Auto-generated method stub  
        JFileChooser jfc=new JFileChooser();  
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);  
        jfc.showDialog(new JLabel(), "обть");
        String res = "";
        try{
            File file=jfc.getSelectedFile();  
            if(file.isDirectory()){  
                res += file.getAbsolutePath();  
            }else if(file.isFile()){  
                return "";
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