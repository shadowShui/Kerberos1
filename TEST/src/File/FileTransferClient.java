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
 * 文件传输Client端<br> 
 * 功能说明： 
 * 
 */  
public class FileTransferClient extends Socket {  
  
    private static String SERVER_IP = "127.0.0.1"; // 服务端IP  
    private static int SERVER_PORT = 8899; // 服务端端口  
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
     * 非加密构造方法
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
        Log.println("Cliect[port:" + client.getLocalPort() + "] 成功连接服务端");
    }
  

	/** 
     * 加密构造方法<br/> 
     * 与服务器建立连接 
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
        Log.println("Cliect[port:" + client.getLocalPort() + "] 成功连接服务端");  
    }  
    
  
    /** 
     * 向服务端传输文件 
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
                // 文件名和长度  
                dos.writeUTF(file.getName());  
                dos.flush();  
                dos.writeLong(file.length());  
                dos.flush();  
  
                // 开始传输文件  
                Log.println("======== 开始传输文件 ========");  
                byte[] bytes = new byte[1024];  
                int length = 0;  
                long progress = 0;  
                while((length = fis.read(bytes, 0, bytes.length)) != -1) {  
                	
                	while (length < bytes.length) {  
                    	  
                        int read = fis.read(bytes, length, bytes.length - length);  
                        //判断是不是读到了数据流的末尾 ，防止出现死循环。  
                        if (read == -1) {  
                            break;  
                        }  
                        length += read;  
                    }  
                	
                	// 读不满的需要对长度进行修正，否则DES加密会有问题
                	if (length != 1024) {
                		byte[] bytes_t = new byte[length];  
                		System.arraycopy(bytes, 0, bytes_t, 0, length);
                		bytes = bytes_t;
                	}
                	
                	if (isCrypt) {
                		Log.println("加密完成！");
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
                Log.println("======== 文件传输成功 ========");  
                JOptionPane.showMessageDialog(null, "传输完成！");
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
     * 入口 
     * @param args 
     */  
    public static void main(String[] args) {  
        try {  
        	long key = 0b1001100101100010101101010110101011010101111011110010000L;
            FileTransferClient client = new FileTransferClient("127.0.0.1", 5353); // 启动客户端连接  
            client.sendFile("D:\\周杰伦吉他弹唱全记录.pdf"); // 传输文件  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
}  