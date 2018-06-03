package File;

import java.io.DataOutputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.net.Socket;

import javax.swing.JOptionPane;

import Client.JProcessBar;
import Crypt.DES;
import System.Log;

  
/** 
 * �ļ�����Client��<br> 
 * ����˵���� 
 * 
 */  
public class FileTransferClient extends Socket {  
  
    private static String SERVER_IP = "127.0.0.1"; // �����IP  
    private static int SERVER_PORT = 8899; // ����˶˿�  
    private boolean isCrypt = false;
    private long key;
    private DES des;
    
    private Socket client;  
  
    private FileInputStream fis;  
  
    private DataOutputStream dos;
	private int cnt_process = 0;  
	
	public int getProcess() {
		return this.cnt_process;
	}
    
    /**
     * �Ǽ��ܹ��췽��
     * @param ip
     * @param port
     * @throws Exception
     */
    public FileTransferClient(String ip, int port) throws Exception {  
    	
    	super(ip, port);  
        
        SERVER_IP = ip;
    	SERVER_PORT = port;
        this.client = this;  
        isCrypt = false;
        Log.println("Cliect[port:" + client.getLocalPort() + "] �ɹ����ӷ����");
    }
  

	/** 
     * ���ܹ��췽��<br/> 
     * ��������������� 
     * @throws Exception 
     */  
    public FileTransferClient(String ip, int port, long key) throws Exception {  
    	
        super(ip, port);  
        
        SERVER_IP = ip;
    	SERVER_PORT = port;
        this.client = this;  
        isCrypt = true;
        this.key = key;
        this.des = new DES();
        Log.println("Cliect[port:" + client.getLocalPort() + "] �ɹ����ӷ����");  
    }  
    
  
    /** 
     * �����˴����ļ� 
     * @throws Exception 
     */  
    public void sendFile(String path) throws Exception {  
    	
//    	FileTransferClient ftfc = this;
//    	new Thread() {
//    		public void run() {
//    			new JProcessBar(ftfc).setVisible(true);;
//    		}
//    	}.start();
    	
        try {  
            File file = new File(path);  
            if(file.exists()) {  
                fis = new FileInputStream(file);  
                dos = new DataOutputStream(client.getOutputStream());  
                Log.println(file.getName());
                // �ļ����ͳ���  
                dos.writeUTF(file.getName());  
                dos.flush();  
                dos.writeLong(file.length());  
                dos.flush();  
  
                // ��ʼ�����ļ�  
                Log.println("======== ��ʼ�����ļ� ========");  
                byte[] bytes = new byte[1024];  
                int length = 0;  
                long progress = 0;  
                while((length = fis.read(bytes, 0, bytes.length)) != -1) {  
                	
                	while (length < bytes.length) {  
                    	  
                        int read = fis.read(bytes, length, bytes.length - length);  
                        //�ж��ǲ��Ƕ�������������ĩβ ����ֹ������ѭ����  
                        if (read == -1) {  
                            break;  
                        }  
                        length += read;  
                    }  
                	
                	// ����������Ҫ�Գ��Ƚ�������������DES���ܻ�������
                	if (length != 1024) {
                		byte[] bytes_t = new byte[length];  
                		System.arraycopy(bytes, 0, bytes_t, 0, length);
                		bytes = bytes_t;
                	}
                	
                	if (isCrypt) {
                		Log.println("������ɣ�");
                		bytes = des.encryptByte(bytes, key);
                	}
                	
                    dos.write(bytes, 0, length);  
                    dos.flush();  
                    progress += length;
                    this.cnt_process = (int) (100*progress/file.length());
                    Log.println(bytes.length + " " + length);
                    Log.print("| " + (100*progress/file.length()) + "% |");  
                }  
                dos.close();
                Log.println();
                Log.println(FileHash.md5HashCode32(file.getAbsolutePath()));  
                Log.println("======== �ļ�����ɹ� ========");  
                JOptionPane.showMessageDialog(null, "������ɣ�");
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if(fis != null)  
                fis.close();  
            if(dos != null)  
                dos.close();  
            client.close();  
        }  
    }  
  
    /** 
     * ��� 
     * @param args 
     */  
    public static void main(String[] args) {  
        try {  
        	long key = 0b1001100101100010101101010110101011010101111011110010000L;
            FileTransferClient client = new FileTransferClient("127.0.0.1", 5353); // �����ͻ�������  
            client.sendFile("D:\\�ܽ��׼�������ȫ��¼.pdf"); // �����ļ�  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
}  