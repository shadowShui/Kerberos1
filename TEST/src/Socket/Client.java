package Socket;

/**
 * Created by SuPhoebe on 2015/12/27.
 * 客户端
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
     * 连接至服务器方法
     * @param ip IP地址
     * @param port 端口号
     * @throws IOException 
     * @throws UnknownHostException 
     */
    public void connectServer(String ip, int port) {
    	
    	try {
			socket = new Socket(ip, port);
			//2.获取输出流，用来向服务器发送信息
	        os = socket.getOutputStream();//字节输出流
	        is = socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 关闭中断连接
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
     * 传送byte数组
     * @param data 要传输的数组
     * @throws IOException
     */
    public void writeBytes(byte[] data) throws IOException {
    	
    	os.write(data);
    }
    
    /**
     * 读取Bytes数组，默认大小1024
     * @return 返回获取结果
     * @throws IOException
     */
    public byte[] readBytes() throws IOException {
    	
    	return this.readBytes(1024);
    }
    
    /**
     * 读取Bytes数组，自定义数组大小
     * @param size 要接收的大小
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
        	
        	
        	Log.println("连接成功！");
            byte[] data = { 0x11, (byte) 0xAA, (byte) 0xFF};
            client.writeBytes(data);
            Log.println("写入成功！");
            data = client.readBytes(2);
            Log.println("读取成功！");
            Log.printBinaryln(data[0]);
            Log.printBinaryln(data[1]);
   
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}