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
 * �ļ�����Server��<br> 
 * ����˵���� 
 * 
 */  
public class FileTransferServer extends ServerSocket {  
  
    private static int SERVER_PORT = 8899; // ����˶˿�  
    private static String path;
    
    private boolean isCrypt = false;
    private long key;
    private DES des;
    private static DecimalFormat df = null;  
    
    private BigInteger auth = null;
  
    static {  
        // �������ָ�ʽ������һλ��ЧС��  
        df = new DecimalFormat("#0.0");  
        df.setRoundingMode(RoundingMode.HALF_UP);  
        df.setMinimumFractionDigits(1);  
        df.setMaximumFractionDigits(1);  
    }  
  
    public FileTransferServer(String path, int port) throws Exception {  
        super(port);  
        SERVER_PORT = port; 
        this.path = path;
        this.isCrypt = false;
    }  
    
    public FileTransferServer(String path, int port, BigInteger auth) throws Exception {
    	
    	 super(port);  
         SERVER_PORT = port; 
         this.path = path;
         this.isCrypt = false;
         this.auth = auth;
    }
    
    public FileTransferServer(String path, int port, BigInteger auth, long key) throws Exception {  
        super(port);  
        SERVER_PORT = port; 
        this.path = path;
        this.isCrypt = true;
        this.auth = auth;
        this.key = key;
        this.des = new DES();
    }  
    
    public FileTransferServer(String path, int port, long key) throws Exception {  
        super(port);  
        SERVER_PORT = port; 
        this.path = path;
        this.key = key;
        this.isCrypt = true;
        this.des = new DES();
    }
  
    /** 
     * ʹ���̴߳���ÿ���ͻ��˴�����ļ� 
     * @throws Exception 
     */  
    public void load() throws Exception {  
//        while (true) {  
//            // server���Խ�������Socket����������server��accept����������ʽ��  
        Socket socket = this.accept();  
        /** 
         * ���ǵķ���˴���ͻ��˵�����������ͬ�����еģ� ÿ�ν��յ����Կͻ��˵���������� 
         * ��Ҫ�ȸ���ǰ�Ŀͻ���ͨ����֮������ٴ�����һ���������� ���ڲ����Ƚ϶������»�����Ӱ���������ܣ� 
         * Ϊ�ˣ����ǿ��԰�����Ϊ���������첽������ͻ���ͨ�ŵķ�ʽ 
         */  
        // ÿ���յ�һ��Socket�ͽ���һ���µ��߳���������  
        new Thread(new Task(socket)).start();  
//        }  
    }  
  
    /** 
     * ����ͻ��˴���������ļ��߳��� 
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
  
                // �ļ����ͳ���  
                String fileName = dis.readUTF();  
                long fileLength = dis.readLong();  
                File directory = new File(path);  
                if(!directory.exists()) {  
                    directory.mkdir();  
                }  
                File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName + ".tmp");  
                fos = new FileOutputStream(file);  
                
                File file_crypt = null;
                if (auth != null) {
                	
                	String name_crypt = fileName;
                	int pos = name_crypt.lastIndexOf(".");  	
                	String name_n = name_crypt.substring(0, pos);
                	String ext = name_crypt.substring(pos + 1, name_crypt.length());
                	
                	file_crypt = new File(directory.getAbsolutePath() + File.separatorChar 
                				+ name_n + "_crypt" + "."+ ext + ".tmp");
                
                		
                    fos_crypt = new FileOutputStream(file_crypt);  
                }
  
                // ��ʼ�����ļ�  
                byte[] bytes = new byte[1024];  
                int length = 0;  
                while((length = dis.read(bytes, 0, bytes.length)) != -1) {  
                	
                	while (length < bytes.length) {  
                    	  
                        int read = dis.read(bytes, length, bytes.length - length);  
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
                		if (auth != null) {
                		
	                		fos_crypt.write(bytes, 0, length); 
	                		fos_crypt.flush();
                		}
                		bytes = des.decryptByte(bytes, key);
                	}
                	
                    fos.write(bytes, 0, length);  
                    fos.flush();  
                }  
                fos.close();
               
                
                if (auth != null) {
                	
                	fos_crypt.close();
                	String name_crypt = file_crypt.getAbsolutePath();	
                	int pos = name_crypt.lastIndexOf(".");  	
                	name_crypt = name_crypt.substring(0, pos);
                	
                	File newFile_crypt = new File(name_crypt);
                	if (newFile_crypt.exists())
                		newFile_crypt.delete();
                	file_crypt.renameTo(newFile_crypt);
            	}

                File newFile = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
                if (newFile.exists())
            		newFile.delete();
                
                file.renameTo(newFile);
                
                String hash = FileHash.md5HashCode32(newFile.getAbsolutePath());
                BigInteger hash_b = new BigInteger(hash, 16);
                Log.println("hash: " + hash);
                Log.println("hash_b: " + hash_b);
                Log.println("auth: " + auth);
                Log.println("======== �ļ����ճɹ� [File Name��" + fileName + "] [Size��" + getFormatFileSize(fileLength) + "] ========");
                

                if (auth == null) 
                	JOptionPane.showMessageDialog(null, "������ɣ�");
                else if (hash_b.equals(auth)) 
                	JOptionPane.showMessageDialog(null, "<html><body>������ɣ�<br> "
                			+ "�ļ���ȫ</body></html>");
                else
                	JOptionPane.showMessageDialog(null, "<html><body>������ɣ�<br> "
                			+ "��Σ�գ��ļ��з��գ�</body></html>");
                	
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
     * ��ʽ���ļ���С 
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
     * ��� 
     * @param args 
     * 
     */  
    public static void main(String[] args) {  
        try {  
        	long key = 0b1001100101100010101101010110101011010101111011110010000L;
            FileTransferServer server = new FileTransferServer("D:\\��������ȫ�γ����\\filecache", 5353); // ���������  
            server.load();  
            server.close();

        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}  
