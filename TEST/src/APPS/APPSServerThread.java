package APPS;

/**
 * Created by SuPhoebe on 2015/12/27.
 * 服务器线程处理类
 */
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import APPS.gui.APPSFrame;
import AS.gui.ASFrame;
import Crypt.DES;
import File.FileTransferClient;
import Socket.ServerThread;
import System.Log;
import System.MsgTag;
import System.Time;


public class APPSServerThread extends Thread {
	
    private static final long Ekv = 789;

	/**  和本线程相关的Socket */
    private Socket socket = null;
    
    /**  输入输出流            */
    private InputStream is = null;
    private DataInputStream dis = null;

    private OutputStream os = null;
    private DataOutputStream dos = null;
    
    private short usr = 12345;

	private long key_c_v;
	
	private APPSFrame appsFrame;
    
    
    public APPSServerThread(Socket socket, APPSFrame appsFrame) {
    	
        this.socket = socket;
        this.init();
        this.appsFrame = appsFrame;
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
    	
    	int count = 0;
    	while (true) {
	        try {
	            byte[] clientMsg = this.readBytes(33);
	            Log.println("读取成功！");
	          
	            byte[] replyMsg = this.msgHandle(clientMsg);
	            this.writeBytes(replyMsg);
	            
	            Log.println("写入成功！");
	            
	            Log.printBinaryln(replyMsg[0]);
	            if (replyMsg[0] == MsgTag.CLOSE_APPS_SOCKET) {
	            	this.close();
	            	break;
	            }
	            count = 0;
	        } catch (IOException e) {
	        	// 超过时间无连接自动关闭套接字
	        	if (count == 5) {
	        		this.closeSocket();
	            	this.close();
	            	break;
	        	}
	        	
	        	count ++;
	        	Log.println("等待客户端发送消息...");
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

    /**
     * 服务器对客户端消息进行响应
     * @param clientMsg
     * @return
     * @throws IOException 
     */
	private byte[] msgHandle(byte[] clientMsg) throws IOException {
		
		byte[] res = new byte[1];
		byte tag = clientMsg[0];
		byte[] msg = new byte[ clientMsg.length - 1 ];
		System.arraycopy(clientMsg, 1, msg, 0, msg.length);
		Log.printBinaryln(tag);
		
		switch(tag) {
		
			case MsgTag.CLIENT_TO_V: return createMsgToClient(msg);
			case MsgTag.CLOSE_APPS_SOCKET: return closeSocket();
			case MsgTag.UPDATE_FILE: return receiveFile();
			case MsgTag.REQUEST_FILENAME: return replyFilename();
			case MsgTag.DOWNLOAD_FILE: return sendFile(msg);
			case MsgTag.REQUEST_RESEND_SOCKET: return registToHub();
			case MsgTag.SEND_TO_CLIENT: return switchToClient(msg);
			
			case MsgTag.GET_CNT_USR: return replyCntusr();
			
			case MsgTag.TO_CLIENT_FAILED :
			case MsgTag.TO_CLIENT_SUCCESS: return reswitchToClient(tag, msg);
			
			default: {
				res[0] = MsgTag.REPLY_FAILED;
				return res;
			}
		}
	}

	private byte[] replyCntusr() {
		// TODO Auto-generated method stub
	// TODO Auto-generated method stub
				
		Set<Short> userList = ClientHub.usermap.keySet();
		StringBuffer sb = new StringBuffer();
		
		Iterator<Short> it = userList.iterator();
		
		while (it.hasNext()) {
			sb.append(it.next() + " ");
		}
	
		String user_s = sb.toString().trim();
		
		Log.println("当前在线用户: " + user_s);
		
		byte[] file_b = null;
		try {
			file_b = user_s.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int length = file_b.length;
		byte[] res = new byte[ file_b.length + 5 ];
		res[0] = MsgTag.CNT_USR_SUCCESS;
		res[1] = (byte) length;
		res[2] = (byte) (length >> 8);
		res[3] = (byte) (length >> 16);
		res[4] = (byte) (length >> 24);
		System.arraycopy(file_b, 0, res, 5, file_b.length);
		
		return res;
	}

	private byte[] reswitchToClient(byte tag, byte[] msg) {
		// TODO Auto-generated method stub

		byte[] first = new byte[1];
		byte[] msg_to_client = new byte[ msg.length + 1 ];
		
		first[0] = tag;
		
		short clientID = (short) ((((int)msg[0]) & 0x00FF) + ((((int) msg[1]) << 8) & 0xFF00));
		
		Log.println("收到发送至" + clientID + "请求");
		Bridge bridge = ClientHub.usermap.get(clientID);
		
		System.arraycopy(first, 0, msg_to_client, 0, first.length);
		System.arraycopy(msg, 0, msg_to_client, 1, msg.length);
		
		byte[] ip = socket.getInetAddress().getAddress();
		System.arraycopy(ip, 0, msg_to_client, 3, ip.length);
		Log.println("ip: " + socket.getInetAddress().getHostAddress());
		
		bridge.transfer(msg_to_client);
		
		Log.println("已进行转发");
		
		byte[] res = new byte[1];
		res[0] = MsgTag.SEND_TO_CLIENT;
		
		return res;
	}

	/**
	 * 客户端之间发送文件由服务器转发请求
	 * @param msg
	 * @return
	 * @throws IOException 
	 */
	private byte[] switchToClient(byte[] msg) throws IOException {
		// TODO Auto-generated method stub
		byte[] first = new byte[1];
		
		first[0] = MsgTag.SEND_TO_CLIENT;
		
		
		short clientID = (short) ((((int)msg[0]) & 0x00FF) + ((((int) msg[1]) << 8) & 0xFF00));
		
		Log.println("收到发送至" + clientID + "请求");
		
		Log.printBinaryln(msg[0]);
		Log.printBinaryln(msg[1]);
		
		short length = (short) ((((int)msg[4]) & 0x00FF) + ((((int) msg[5]) << 8) & 0xFF00));
		Log.println("length: " + length);
		byte[] auth = this.readBytes(length);
		
		Bridge bridge = ClientHub.usermap.get(clientID);
		
		byte[] msg_to_client = new byte[ length + 33 ];
		System.arraycopy(first, 0, msg_to_client, 0, first.length);
		System.arraycopy(msg, 0, msg_to_client, 1, msg.length);
		System.arraycopy(auth, 0, msg_to_client, 33, length);
		
		bridge.transfer(msg_to_client);
		
		Log.println("已进行转发");
		
		byte[] res = new byte[1];
		res[0] = MsgTag.SEND_TO_CLIENT;
		
		return res;
	}

	private byte[] registToHub() {
		// TODO Auto-generated method stub
		boolean res_r = ClientHub.getRegist(usr);
		byte[] res = new byte[1];
		if (res_r == true) {
			res[0] = MsgTag.RESEND_SOCKET_SUCCESS;
			return res;
		} else {
			res[0] = MsgTag.RESEND_SOCKET_FAILED;
			return res;
		}
	}

	private byte[] sendFile(byte[] msg) {
		// TODO Auto-generated method stub
		int index = ((int) msg[0] & 0xFF)
 			  	+ (((int) msg[1]) << 8 & 0xFF00)
 			  	+ (((int) msg[2]) << 16 & 0xFF0000)
 			  	+ (((int) msg[3]) << 24 & 0xFF000000);
		
		String name = getFilename(index);
		
		int port = ((int) msg[4] & 0xFF)
 			  	+ (((int) msg[5]) << 8 & 0xFF00)
 			  	+ (((int) msg[6]) << 16 & 0xFF0000)
 			  	+ (((int) msg[7]) << 24 & 0xFF000000);
		
		byte[] res = new byte[1];
		res[0] = MsgTag.PERMIT_DOWNLOAD;
		
		
		try {
			final FileTransferClient ftfc = new FileTransferClient(socket.getInetAddress().getHostAddress(), port);
			new Thread() {
				public void run() {
					try {
						ftfc.sendFile("D:\\物联网安全课程设计\\filecache\\" + usr + "\\" + name);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			res[0] = MsgTag.DENY_DOWNLOAD;
			e2.printStackTrace();
			return res;
		};
		
		return res;
	}

	private String getFilename(int index) {
		// TODO Auto-generated method stub
		File filepath = new File("D:\\物联网安全课程设计\\filecache\\" + usr);
		
		String[] fileList = filepath.list();
		Arrays.sort(fileList);
		
		return fileList[index];
	}

	/**
	 * 返回文件名
	 * @return
	 */
	private byte[] replyFilename() {
		// TODO Auto-generated method stub
		
		
		File filepath = new File("D:\\物联网安全课程设计\\filecache\\" + usr);
		if (!filepath.exists()) {
			filepath.mkdirs();
		}
		
		String[] fileList = filepath.list();
		Arrays.sort(fileList);
		
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < fileList.length; i++){
			if (i != fileList.length - 1)
				sb.append(fileList[i] + "{");
			else
				sb.append(fileList[i]);
		}
		String file_s = sb.toString();
		
		Log.println("用户 " + usr + "文件: " + file_s);
		
		byte[] file_b = null;
		try {
			file_b = file_s.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int length = file_b.length;
		byte[] res = new byte[ file_b.length + 5 ];
		res[0] = MsgTag.REPLY_FILENAME;
		res[1] = (byte) length;
		res[2] = (byte) (length >> 8);
		res[3] = (byte) (length >> 16);
		res[4] = (byte) (length >> 24);
		System.arraycopy(file_b, 0, res, 5, file_b.length);
		
		return res;
	}

	private byte[] receiveFile() {
		// TODO Auto-generated method stub
		Random random = new Random();
		int ran = 1025;
		while (true) {
			// 获取1025-65535之间的随机数
			ran = random.nextInt(64510);
			ran += 1025;
			// 若端口没有被占用,就跳出循环
			if (isOpenPort(ran))
				break;
		}
		
		new FileServerThread(usr, ran).start();
		
		Log.println("transfer port is open: " + ran);
		short port = (short) (ran & 0x0000FFFF);
		Log.println(port);
		byte[] res = new byte[3];
		res[0] = MsgTag.PERMIT_UPDATE;
		res[1] = (byte) (port & 0x00FF);
		res[2] = (byte) (port >> 8); 
		
		Log.printBinaryln(res[1]);
		Log.printBinaryln(res[2]);
		return res;
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

	/**
	 * 返回关闭报文
	 * @param msg
	 * @return
	 */
	private byte[] closeSocket() {
		// TODO Auto-generated method stub
		byte[] res = new byte[1];
		res[0] = MsgTag.CLOSE_APPS_SOCKET;
		ClientHub.usermap.remove(this.usr);
		Log.println("关闭桥接通道");
		return res;
	}

	/**
	 * 生成认证报文
	 * @param clientMsg
	 * @return
	 */
	private byte[] createMsgToClient(byte[] clientMsg) {
		// TODO Auto-generated method stub'
		byte[] res = new byte[9];
		res[0] = MsgTag.V_TO_CLIENT;
		byte[] ticket_v = new byte[16];
		byte[] authenticator_c = new byte[16];
		
		System.arraycopy(clientMsg, 0, ticket_v, 0, ticket_v.length);
		System.arraycopy(clientMsg, 16, authenticator_c, 0, authenticator_c.length);
		
		ticket_v = new DES().decryptByte(ticket_v, this.Ekv);
		
		this.key_c_v = ((long) ticket_v[0] & 0xFF)+((((long)ticket_v[1]) << 8) & 0xFF00);
		Log.println("key_c_v: " + this.key_c_v);
		authenticator_c = new DES().decryptByte(authenticator_c, this.key_c_v);
		
		String log = Log.logC2V(".\\logC2V.txt", ticket_v, authenticator_c);
		this.appsFrame.append(log);
		
		int TS_4 = (((int)ticket_v[8]) & 0x000000FF)
	    		  + ((((int)ticket_v[9]) << 8) & 0x0000FF00)
	    		  + ((((int)ticket_v[10]) << 16) & 0x00FF0000)
	    		  + ((((int)ticket_v[11]) << 24) & 0xFF000000);
		
		this.usr = (short) (((short) ticket_v[4] & 0xFF)
				+((((short)ticket_v[5]) << 8) & 0xFF00));
		Log.println("usr: " + this.usr);
		
		int LifeTime_4 = (int) ticket_v[6];
		Log.println("TS_4: " + TS_4);
		Log.println("LifeTime_4: " + LifeTime_4);
		
		if (!Time.isIntime(TS_4, LifeTime_4)) {
			res[0] = MsgTag.REPLY_FAILED;
			return res;
		}
		
		//判断adc
		if(ticket_v[12] != authenticator_c[8] || ticket_v[13] != authenticator_c[9]
				|| ticket_v[14] != authenticator_c[10]|| ticket_v[15] != authenticator_c[11]){
			res[0] = MsgTag.REPLY_FAILED;
			return res;
		}
		
		int TS_5 = (((int)authenticator_c[4]) & 0x000000FF)
	    		  + ((((int)authenticator_c[5]) << 8) & 0x0000FF00)
	    		  + ((((int)authenticator_c[6]) << 16) & 0x00FF0000)
	    		  + ((((int)authenticator_c[7]) << 24) & 0xFF000000);
		
		Log.println("TS_5: " + TS_5);
		
		byte[] res_t = new byte[8];
		int TS_6 = TS_5 + 1;
		Log.println("TS_6: " + TS_6);
		
		res_t[0] = (byte) TS_6;
		res_t[1] = (byte) (TS_6 >> 8);
		res_t[2] = (byte) (TS_6 >> 16);
		res_t[3] = (byte) (TS_6 >> 24);
		
		log = Log.logV2C(".\\logV2C.txt", res_t);
		this.appsFrame.append(log);
		
		res_t = new DES().encryptByte(res_t, key_c_v);
		
		System.arraycopy(res_t, 0, res, 1, res_t.length);
				
		return res;
	}
}