package File;

import java.io.DataInputStream;  
import java.io.File;  
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.math.RoundingMode;  
import java.net.ServerSocket;  
import java.net.Socket;  
import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import Crypt.DES;
import System.Log;  
  
/** 
 * 文件传输Server端<br> 
 * 功能说明： 
 * 
 */  
public class FileTransferServer_back extends ServerSocket {  
  
    private static int SERVER_PORT = 8899; // 服务端端口  
    private static String path;
    
    private boolean isCrypt = false;
    private long key;
    private DES des;
    private static DecimalFormat df = null;  
    
    BigInteger auth = null;
  
    static {  
        // 设置数字格式，保留一位有效小数  
        df = new DecimalFormat("#0.0");  
        df.setRoundingMode(RoundingMode.HALF_UP);  
        df.setMinimumFractionDigits(1);  
        df.setMaximumFractionDigits(1);  
    }  
  
    public FileTransferServer_back(String path, int port) throws Exception {  
        super(port);  
        SERVER_PORT = port; 
        this.path = path;
        this.isCrypt = false;
    }  
    
    public FileTransferServer_back(String path, int port, BigInteger auth) throws Exception {
    	
    	 super(port);  
         SERVER_PORT = port; 
         this.path = path;
         this.isCrypt = false;
         this.auth = auth;
    }
    
    public FileTransferServer_back(String path, int port, BigInteger auth, long key) throws Exception {  
        super(port);  
        SERVER_PORT = port; 
        this.path = path;
        this.isCrypt = true;
        this.auth = auth;
        this.key = key;
        this.des = new DES();
    }  
    
    public FileTransferServer_back(String path, int port, long key) throws Exception {  
        super(port);  
        SERVER_PORT = port; 
        this.path = path;
        this.key = key;
        this.isCrypt = true;
        this.des = new DES();
    }
  
    /** 
     * 使用线程处理每个客户端传输的文件 
     * @throws Exception 
     */  
    public void load() throws Exception {  
//        while (true) {  
//            // server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的  
        Socket socket = this.accept();  
        /** 
         * 我们的服务端处理客户端的连接请求是同步进行的， 每次接收到来自客户端的连接请求后， 
         * 都要先跟当前的客户端通信完之后才能再处理下一个连接请求。 这在并发比较多的情况下会严重影响程序的性能， 
         * 为此，我们可以把它改为如下这种异步处理与客户端通信的方式 
         */  
        // 每接收到一个Socket就建立一个新的线程来处理它  
        new Thread(new Task(socket)).start();  
//        }  
    }  
  
    /** 
     * 处理客户端传输过来的文件线程类 
     */  
    class Task implements Runnable {  
  
        private Socket socket;  
  
        private DataInputStream dis;  
  
        private FileOutputStream fos;  
        
        private FileOutputStream fos_crypt;  
  
        public Task(Socket socket) {  
            this.socket = socket;  
        }  
  
        @Override  
        public void run() {  
            try {  
                dis = new DataInputStream(socket.getInputStream());  
  
                // 文件名和长度  
                String fileName = dis.readUTF();  
                long fileLength = dis.readLong();  
                File directory = new File(path);  
                if(!directory.exists()) {  
                    directory.mkdir();  
                }  
                File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName + ".tmp");  
                fos = new FileOutputStream(file);  
                
                File file_crypt = null;
                if (isCrypt) {
                	
                	String name_crypt = fileName;
                	int pos = name_crypt.lastIndexOf(".");  	
                	String name_n = name_crypt.substring(0, pos);
                	String ext = name_crypt.substring(pos + 1, name_crypt.length());
                	
                	file_crypt = new File(directory.getAbsolutePath() + File.separatorChar 
                				+ name_n + "_crypt" + "."+ ext + ".tmp");
                
                		
                    fos_crypt = new FileOutputStream(file_crypt);  
                }
  
                // 开始接收文件  
                byte[] bytes = new byte[1024];  
                int length = 0;  
                while((length = dis.read(bytes, 0, bytes.length)) != -1) {  
                	
                	if (isCrypt) {
                		
                		fos_crypt.write(bytes, 0, length); 
                		fos_crypt.flush();
                		bytes = des.decryptByte(bytes, key);
                	}
                	
                    fos.write(bytes, 0, length);  
                    fos.flush();  
                }  
                fos.close();
               
                
                if (isCrypt) {
                	
                	fos_crypt.close();
                	String name_crypt = file_crypt.getAbsolutePath();	
                	int pos = name_crypt.lastIndexOf(".");  	
                	name_crypt = name_crypt.substring(0, pos);
                	
                	File newFile_crypt = new File(name_crypt);
                	file_crypt.renameTo(newFile_crypt);
            	}

                File newFile = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
                file.renameTo(newFile);
                
                String hash = FileHash.md5HashCode32(newFile.getAbsolutePath());
                BigInteger hash_b = new BigInteger(hash, 16);
                Log.println(hash_b);
                Log.println("======== 文件接收成功 [File Name：" + fileName + "] [Size：" + getFormatFileSize(fileLength) + "] ========");
                

                if (auth == null) 
                	JOptionPane.showMessageDialog(null, "下载完成！");
                else if (hash_b.equals(auth)) 
                	JOptionPane.showMessageDialog(null, "<html><body>下载完成！<br> "
                			+ "文件安全</body></html>");
                else
                	JOptionPane.showMessageDialog(null, "<html><body>下载完成！<br> "
                			+ "（危险）文件有风险！</body></html>");
                	
            } catch (Exception e) {  
                e.printStackTrace();  
            } finally {  
                try {  
                    if(fos != null)  
                        fos.close();  
                    if(dis != null)  
                        dis.close();  
                    socket.close();  
                } catch (Exception e) {}  
            }  
        }  
    }  
  
    /** 
     * 格式化文件大小 
     * @param length 
     * @return 
     */  
    private String getFormatFileSize(long length) {  
        double size = ((double) length) / (1 << 30);  
        if(size >= 1) {  
            return df.format(size) + "GB";  
        }  
        size = ((double) length) / (1 << 20);  
        if(size >= 1) {  
            return df.format(size) + "MB";  
        }  
        size = ((double) length) / (1 << 10);  
        if(size >= 1) {  
            return df.format(size) + "KB";  
        }  
        return length + "B";  
    }  
  
    /** 
     * 入口 
     * @param args 
     * 
     */  
    public static void main(String[] args) {  
        try {  
        	long key = 0b1001100101100010101101010110101011010101111011110010000L;
            FileTransferServer_back server = new FileTransferServer_back("D:\\物联网安全课程设计\\filecache", 5353); // 启动服务端  
            server.load();  
            server.close();

        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}  
