package Client;

import java.awt.Color;  
import java.awt.FlowLayout;  
  
import javax.swing.JFrame;  
import javax.swing.JPanel;  
import javax.swing.JProgressBar;  
import javax.swing.border.EmptyBorder;

import File.FileTransferClient;  
  
  
  
public class JProcessBar extends JFrame{  
  
    private static final long serialVersionUID = 1L;  
    private JProgressBar processBar;
	private FileTransferClient fileTransferClient;  
  
    public JProcessBar (){  
        setTitle("正在传输...");      //设置窗体标题  
          
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗体退出的操作  
          
        setBounds(100, 100, 250, 100);// 设置窗体的位置和大小  
          
        JPanel contentPane = new JPanel();   // 创建内容面板  
          
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));// 设置内容面板边框  
          
        setContentPane(contentPane);// 应用(使用)内容面板  
          
        contentPane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));// 设置为流式布局  
          
        processBar = new JProgressBar();// 创建进度条  
          
        processBar.setStringPainted(true);// 设置进度条上的字符串显示，false则不能显示  
          
        processBar.setBackground(Color.GREEN);  
      
        // 创建线程显示进度  
        new Thread(){  
                          
            public void run(){  
            	int p = 0;
            	while (true) {
            		p = fileTransferClient.getProcess();
            		processBar.setValue(p); // 设置进度条数值  
            		if (p == 100) {
            			break;
            		}
            		try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
                
            	
                processBar.setString("传输完成");// 设置提示信息  
            }  
        }.start(); //  启动进度条线程  
          
        contentPane.add(processBar);// 向面板添加进度控件  
    }  
      
    public JProcessBar(FileTransferClient fileTransferClient) {
		// TODO Auto-generated constructor stub
    	this.fileTransferClient = fileTransferClient;
	}

	public static void main(String[] args){  
        JProcessBar JPBD = new JProcessBar();  
        JPBD.setVisible(true);    
    }  
}