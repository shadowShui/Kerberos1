package Socket;

/**
 * Created by SuPhoebe on 2015/12/27.
 * �ͻ���
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import System.Log;

import java.util.Random;

public class Client {
	
    private Random r = new Random();
    private Socket socket;
    private OutputStream os;
    private InputStream is;
    
    /**
     * ����������������
     * @param ip IP��ַ
     * @param port �˿ں�
     * @throws IOException 
     * @throws UnknownHostException 
     */
    public void connectServer(String ip, int port) {
    	
    	try {
			socket = new Socket(ip, port);
			//2.��ȡ������������������������Ϣ
	        os = socket.getOutputStream();//�ֽ������
	        is = socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * �ر��ж�����
     */
    public void close() {
    	
    	try {
    		if (is != null)
    			is.close();
    		if (os != null)
    			os.close();
    		if (socket != null)
    			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * ����byte����
     * @param data Ҫ���������
     * @throws IOException
     */
    public void writeBytes(byte[] data) throws IOException {
    	
    	os.write(data);
    }
    
    /**
     * ��ȡBytes���飬Ĭ�ϴ�С1024
     * @return ���ػ�ȡ���
     * @throws IOException
     */
    public byte[] readBytes() throws IOException {
    	
    	return this.readBytes(1024);
    }
    
    /**
     * ��ȡBytes���飬�Զ��������С
     * @param size Ҫ���յĴ�С
     * @return
     * @throws IOException
     */
    public byte[] readBytes(int size) throws IOException {
    	
    	byte[] res = new byte[size];
    	is.read(res);
    	return res;
    }

    public static void main(String[] args) {
    	
    	Client client = new Client();
    	client.connectServer("127.0.0.1", 8888);
        try {
        	
        	
        	Log.println("���ӳɹ���");
            byte[] data = { 0x11, (byte) 0xAA, (byte) 0xFF};
            client.writeBytes(data);
            Log.println("д��ɹ���");
            data = client.readBytes(2);
            Log.println("��ȡ�ɹ���");
            Log.printBinaryln(data[0]);
            Log.printBinaryln(data[1]);
   
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}