package Socket;

/**
 * Created by SuPhoebe on 2015/12/27.
 * �������̴߳�����
 */
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import System.Log;


public class ServerThread extends Thread {
	
    /**  �ͱ��߳���ص�Socket */
    private Socket socket = null;
    
    /**  ���������            */
    private InputStream is = null;
    private DataInputStream dis = null;

    private OutputStream os = null;
    private DataOutputStream dos = null;
    
    
    public ServerThread(Socket socket) {
    	
        this.socket = socket;
        this.init();
    }
    
    /**
     * ��ʼ�����������
     */
    private void init() {
    	
    	try {
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * �ر��ж�����
     */
    private void close() {
    	
    	try {
    		if (is != null)
    			is.close();
    		if (dis != null)
    			dis.close();
    		if (os != null)
    			os.close();
    		if (dos != null)
    			dos.close();
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
    private void writeBytes(byte[] data) throws IOException {
    	
    	os.write(data);
    }
    
    /**
     * ��ȡBytes���飬Ĭ�ϴ�С1024
     * @return ���ػ�ȡ���
     * @throws IOException
     */
	private byte[] readBytes() throws IOException {
    	
    	return this.readBytes(1024);
    }
    
    /**
     * ��ȡBytes���飬�Զ��������С
     * @param size Ҫ���յĴ�С
     * @return
     * @throws IOException
     */
    private byte[] readBytes(int size) throws IOException {
    	
    	byte[] res = new byte[size];
    	is.read(res);
    	return res;
    }

    //�߳�ִ�еĲ�������Ӧ�ͻ��˵�����
    public void run(){
 
        try {
            byte[] res = this.readBytes(3);
            Log.println("��ȡ�ɹ���");
            Log.printBinaryln(res[0]);
            Log.printBinaryln(res[1]);
            Log.printBinaryln(res[2]);
            byte[] data = {0x11, (byte) 0xAC};
            this.writeBytes(data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            //�ر���Դ
            this.close();
        }
    }
}