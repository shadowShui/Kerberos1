package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import APPS.FileServerThread;
import Client.gui.MainFrame;
import Client.gui.TransferPanel;
import File.FileTransferClient;
import System.MsgTag;
import System.IP;
import System.Log;
import System.Time;

public class FileListeningThread extends Thread {

	
	private Socket socket = null;
	
	 /**  输入输出流            */
    private InputStream is = null;
    private DataInputStream dis = null;

    private OutputStream os = null;
    private DataOutputStream dos = null;
    
    private Client client = null;
    
	public FileListeningThread(Socket socket, Client client) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.client = client;
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

    //线程执行的操作，响应服务器的请求
    public void run(){
    	
    	while (true) {
	        try {
	            byte[] serverMsg = this.readBytes(33);
	            Log.println("读取成功！");
	          
	            this.msgHandle(serverMsg);
	            
	        } catch (IOException e) {
	        	
	        	Log.println("等待服务器发送消息...");
	        	try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            // TODO Auto-generated catch block
	            //e.printStackTrace();
	        }
    	}
        
    }

	private void msgHandle(byte[] serverMsg) throws IOException {
		byte[] res = new byte[1];
		byte tag = serverMsg[0];
		byte[] msg = new byte[ serverMsg.length - 1 ];
		System.arraycopy(serverMsg, 1, msg, 0, msg.length);
		Log.printBinaryln(tag);
		
		switch(tag) {
		
			case MsgTag.SEND_TO_CLIENT: handleFileRequest(msg); break;
			case MsgTag.TO_CLIENT_SUCCESS: startSend(msg); break;
			case MsgTag.TO_CLIENT_FAILED: cancelSend(); break;
			
		}
	}

	private void cancelSend() {
		// TODO Auto-generated method stub
		Log.println("对方拒绝接受文件！");
	}

	private void startSend(byte[] msg) {
		// TODO Auto-generated method stub
		
		Log.println("开始传输文件.....");
		
		String ip = (((int) msg[2]) & 0x00FF) + "."
				  + (((int) msg[3]) & 0x00FF) + "."
				  + (((int) msg[4]) & 0x00FF) + "."
				  + (((int) msg[5]) & 0x00FF);
		Log.println("ip: " + ip);
		int port = (((int)msg[6]) & 0x00FF) + ((((int) msg[7]) << 8) & 0xFF00);
    	Log.println("port: " + port);
    	
    	short key = (short) ((((int)msg[8]) & 0x00FF) + ((((int) msg[9]) << 8) & 0xFF00));
    	Log.println("file transfer key: " + key);
    	
    	String log = Time.getCurrentTime() + "\n";
    	log += "获取到临时会话秘钥: " + key + "\n";
    	log += "开始进行加密传输...\n";
    	
    	this.client.getFileFrame().append(log);
    	
    	try {
        	FileTransferClient client = new FileTransferClient(ip, port, key); // 启动客户端连接  
			client.sendFile(this.client.getWaitTransferFile());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleFileRequest(byte[] msg) throws IOException {
		// TODO Auto-generated method stub
		Log.println("已收到传输文件申请！");
		byte[] res = new byte[7];
		res[0] = MsgTag.TO_CLIENT_SUCCESS;
		short clientID = (short) ((((int)msg[2]) & 0x00FF) + ((((int) msg[3]) << 8) & 0xFF00));
		short length = (short) ((((int)msg[4]) & 0x00FF) + ((((int) msg[5]) << 8) & 0xFF00));
		Log.println("au_length: " + length);
		byte[] auth = this.readBytes(length);
		String auth_s = new String(auth, "GBK");
		Log.println("auth: " + auth_s);
		
		ConfirmThread t = new ConfirmThread(clientID, auth_s, client);
		t.start();
		
//		int n = t.getN();
//		
//		while (t.getN() == -10) {
//			
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
//		// 用户取消
//    	if (n != JOptionPane.YES_OPTION) {   
//    		res[0] = MsgTag.TO_CLIENT_FAILED;
//    		return res;
//    	}
		
//    	byte[] ipAddress = IP.getIPAddress();
//    	System.arraycopy(ipAddress, 0, res, 1, ipAddress.length);
//
//    	Random random = new Random();
//		int ran = 1025;
//		while (true) {
//			// 获取1025-65535之间的随机数
//			ran = random.nextInt(64510);
//			ran += 1025;
//			// 若端口没有被占用,就跳出循环
//			if (isOpenPort(ran))
//				break;
//		}
//		
//		short port = (short) (ran & 0x0000FFFF);
//		Log.println(((int) port & 0x0000FFFF));
//		
//		res[5] = (byte) port;
//		res[6] = (byte) (port >> 8);
//		
//		// new FileServerThread(usr, ran).start();
//		return res;
	}
	
	// 判断端口是否被占用
	private boolean isOpenPort(int Port)
	{
		try {
			ServerSocket s = new ServerSocket(Port);
			s.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
}
