package Socket;

/**
 * Created by SuPhoebe on 2015/12/27.
 * 服务器线程处理类
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
	
    /**  和本线程相关的Socket */
    private Socket socket = null;
    
    /**  输入输出流            */
    private InputStream is = null;
    private DataInputStream dis = null;

    private OutputStream os = null;
    private DataOutputStream dos = null;
    
    
    public ServerThread(Socket socket) {
    	
        this.socket = socket;
        this.init();
    }
    
    /**
     * 初始化输入输出流
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
     * 关闭中断连接
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
     * 传送byte数组
     * @param data 要传输的数组
     * @throws IOException
     */
    private void writeBytes(byte[] data) throws IOException {
    	
    	os.write(data);
    }
    
    /**
     * 读取Bytes数组，默认大小1024
     * @return 返回获取结果
     * @throws IOException
     */
	private byte[] readBytes() throws IOException {
    	
    	return this.readBytes(1024);
    }
    
    /**
     * 读取Bytes数组，自定义数组大小
     * @param size 要接收的大小
     * @return
     * @throws IOException
     */
    private byte[] readBytes(int size) throws IOException {
    	
    	byte[] res = new byte[size];
    	is.read(res);
    	return res;
    }

    //线程执行的操作，响应客户端的请求
    public void run(){
 
        try {
            byte[] res = this.readBytes(3);
            Log.println("读取成功！");
            Log.printBinaryln(res[0]);
            Log.printBinaryln(res[1]);
            Log.printBinaryln(res[2]);
            byte[] data = {0x11, (byte) 0xAC};
            this.writeBytes(data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            //关闭资源
            this.close();
        }
    }
}