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
        setTitle("���ڴ���...");      //���ô������  
          
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���ô����˳��Ĳ���  
          
        setBounds(100, 100, 250, 100);// ���ô����λ�úʹ�С  
          
        JPanel contentPane = new JPanel();   // �����������  
          
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));// �����������߿�  
          
        setContentPane(contentPane);// Ӧ��(ʹ��)�������  
          
        contentPane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));// ����Ϊ��ʽ����  
          
        processBar = new JProgressBar();// ����������  
          
        processBar.setStringPainted(true);// ���ý������ϵ��ַ�����ʾ��false������ʾ  
          
        processBar.setBackground(Color.GREEN);  
      
        // �����߳���ʾ����  
        new Thread(){  
                          
            public void run(){  
            	int p = 0;
            	while (true) {
            		p = fileTransferClient.getProcess();
            		processBar.setValue(p); // ���ý�������ֵ  
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
                
            	
                processBar.setString("�������");// ������ʾ��Ϣ  
            }  
        }.start(); //  �����������߳�  
          
        contentPane.add(processBar);// �������ӽ��ȿؼ�  
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