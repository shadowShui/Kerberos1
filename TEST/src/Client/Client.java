package Client;

/**
 * �ͻ���
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import Client.FileServerThread;
import Client.gui.FileFrame;
import Client.gui.MainFrame;
import Client.gui.TransferPanel;
import Crypt.DES;
import Crypt.MD5;
import Crypt.RSA;
import File.FileHash;
import File.FileTransferClient;
import System.IP;
import System.Log;
import System.MsgTag;
import System.Time;
import System.FileHandle;

import java.util.Date;
import java.util.Random;

public class Client {
	
	private FileFrame fileFrame;
	
	/** AS������IP��ַ */
	private String AS_IP = "127.0.0.1";
	
	/** TGS������IP��ַ */
	private String TGS_IP = "127.0.0.1";
	
	/** Ӧ�÷�����IP��ַ */
	private String APPS_IP = "127.0.0.1";
	
	/** AS�������˿ں� */
	private final int AS_PORT = 8888;
	
	/** TGS�������˿ں�  */
	private final int TGS_PORT = 8889;
	
	/** Ӧ�÷������˿ں�  */
	private final int APPS_PORT = 8899;
	
	/** Ӧ�÷����������˿ں�  */
	private final int APPS_BRIDGE_PORT = 8999;
	
	/** �ͻ�ID  */
	private short id;
	
	/** �ͻ�����  */
	private String psw;
	
	private String waitTransferFile = "";
	
	
	
	private DES des = new DES();
	
	/**    ������ID��        */
	private final byte AS_ID = 1;
	private final byte TGS_ID = 1;
	private final short APPS_ID = 1;
	
	private RSA rsa = new RSA(512);
	
	public Client() {
		this.updateHost();
	}
	
	public void setFileFrame(FileFrame fileFrame) {
		// TODO Auto-generated method stub
		this.fileFrame = fileFrame;
	}
	
	public FileFrame getFileFrame() {
		// TODO Auto-generated method stub
		return this.fileFrame;
	}
	
	public void updateHost() {
		
		if (FileHandle.isExist("./host.txt")) {
			String[] host = FileHandle.readToString("./host.txt").split(" ");
			this.AS_IP = host[0];
			this.TGS_IP = host[1];
			this.APPS_IP = host[2];
		}
	}
	
	public String getAS() {
		return this.AS_IP;
	}
	
	public String getTGS() {
		return this.TGS_IP;
	}
	
	public String getAPPS() {
		return this.APPS_IP;
	}
	
	public void setAS(String ip) {
		this.AS_IP = ip;
	}
	
	public void setTGS(String ip) {
		this.TGS_IP = ip;
	}
	
	public void setAPP(String ip) {
		this.APPS_IP = ip;
	}
	
	public void setWaitTransferFile(String file) {
		this.waitTransferFile = file;
	}
	
	public String getWaitTransferFile() {
		return this.waitTransferFile;
	}
	
    private Random r = new Random();
    private Socket socket;
    private OutputStream os;
    private InputStream is;

	private long psw_h;

	private long key_c_tgs;

	private int TS_4;

	private int TS_3;

	private int TS_2;

	private long key_c_v;

	private int TS_5;
    
    /**
     * ����������������
     * @param ip IP��ַ
     * @param port �˿ں�
     * @throws IOException 
     * @throws UnknownHostException 
     */
    public boolean connectServer(String ip, int port) {
    	
    	try {
			socket = new Socket(ip, port);
			//2.��ȡ������������������������Ϣ
	        os = socket.getOutputStream();//�ֽ������
	        is = socket.getInputStream();
	        return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }
    
    /**
     * �ر��ж�����
     */
    public void closeSocket() {
    	
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
    	Log.println("read from server: ");
    	
    	if (size > 0)
    		Log.printBinaryln(res[0]);
    	if (size > 1) {
	    	Log.printBinaryln(res[1]);
	    	Log.printBinaryln(res[2]);
    	}
    	return res;
    }
    
    /**
     * ע�᷽������AS��������������
     * @param id
     * @param psw
     * @return
     */
    public String regist(short id, String psw) {
    	
    	byte tag = MsgTag.REGIST_TO_AS;
    	byte[] tag_msg = new byte[11];
    	tag_msg[0] = tag;
    	tag_msg[1] = (byte) (id & 0x00FF);
    	tag_msg[2] = (byte) (id >> 8);
    	Log.printBinaryln(tag_msg[1]);
    	Log.printBinaryln(tag_msg[2]);
    	Log.println(id + " " + psw);
    	
    	byte[] psw_b = MD5.MD5bytes_64(psw);
    	long[] psw_l = new DES().byteToLong(psw_b);
    	Log.println(psw_l[0]);
    	System.arraycopy(psw_b, 0, tag_msg, 3, psw_b.length);
    	
    			
    	byte[] replyAS = registToAS(tag_msg);
    	
    	if (replyAS[0] == MsgTag.CONNECT_FAILED)
    		return "connect AS failed";
    	else if (replyAS[0] == MsgTag.WRITE_FAILED)
    		return "send message to AS failed";
    	else if (replyAS[0] == MsgTag.READ_FAILED)
    		return "read message from AS failed";
    	else if (replyAS[0] == MsgTag.REPLY_FAILED)
    		return "pass AS failed";
    	else if (replyAS[0] == MsgTag.REGIST_FAILRD)
    		return "regist failed";
    	else if (replyAS[0] == MsgTag.REGIST_SUCCESS)
    		return this.getCertificate(id);

    	return "unknown error";
    }
    
    
    private byte[] registToAS(byte[] message) {
		// TODO Auto-generated method stub
	    	/**
	   	 * ��AS��������������
	   	 */
	   	
	   	// ����AS������
	   	boolean isConnect;
	   	isConnect = this.connectServer(AS_IP, AS_PORT);
	   	if (!isConnect) {
	   		byte[] res = new byte[1];
	   		this.closeSocket();
	   		res[0] = MsgTag.CONNECT_FAILED;
	   		return res;
	   	}
	   	
	   	
	   	// ��AS������д������
	   	try {
			this.writeBytes(message);
		} catch (IOException e) {
			byte[] res = new byte[1];
			res[0] = MsgTag.WRITE_FAILED;
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.closeSocket();
			return res;
		}
	   	
	   	// ��AS��������ȡ����
	   	byte[] reply;
		try {
			reply = this.readBytes(25);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			byte[] res = new byte[1];
			res[0] = MsgTag.READ_FAILED;
			e.printStackTrace();
			this.closeSocket();
			return res;
		}
	   	
	   	this.closeSocket();
	   	return reply;

	}
    /**
     * ��¼����
     * 
     * ���а�������AS TGS Ӧ�÷����������ӹ���
     * @param id �û���
     * @param psw ����
     * @return ���ӳɹ�����ʧ�ܵ�ԭ��
     */
    public String login(short id, String psw) {
    	
    	this.id = id;
    	this.psw = psw;
    	this.psw_h = des.byteToLong(MD5.MD5bytes_64(psw))[0];
    	Log.printBinaryln(psw_h);
    	
    	byte[] replyAS = requestAS();
    	
    	if (replyAS[0] == MsgTag.CONNECT_FAILED)
    		return "connect AS failed";
    	else if (replyAS[0] == MsgTag.WRITE_FAILED)
    		return "send message to AS failed";
    	else if (replyAS[0] == MsgTag.READ_FAILED)
    		return "read message from AS failed";
    	else if (replyAS[0] == MsgTag.REPLY_FAILED)
    		return "pass AS failed";
    	else if (replyAS[0] != MsgTag.AS_TO_CLIENT)
    		return "as failed, unknown error";
    	
    	byte[] replyTGS = requestTGS(replyAS);
    	
    	if (replyTGS[0] == MsgTag.CONNECT_FAILED)
    		return "connect TGS failed";
    	else if (replyTGS[0] == MsgTag.WRITE_FAILED)
    		return "send message to TGS failed";
    	else if (replyTGS[0] == MsgTag.READ_FAILED)
    		return "read message from TGS failed";
    	else if (replyTGS[0] == MsgTag.REPLY_FAILED)
    		return "pass TGS failed";
    	
    	byte[] replyAPPS = requestAPPS(replyTGS);
    	
    	if (replyAPPS[0] == MsgTag.CONNECT_FAILED)
    		return "connect APPS failed";
    	else if (replyAPPS[0] == MsgTag.WRITE_FAILED)
    		return "send message to APPS failed";
    	else if (replyAPPS[0] == MsgTag.READ_FAILED)
    		return "read message from APPS failed";
    	else if (replyAPPS[0] == MsgTag.REPLY_FAILED)
    		return "pass APPS failed";
    	
    	return checkAPPS(replyAPPS);
 
    }

    private String checkAPPS(byte[] replyAPPS) {
		// TODO Auto-generated method stub
    	byte[] chipertext = new byte[8];
    	System.arraycopy(replyAPPS, 1, chipertext, 0, chipertext.length);
    	byte[] plaintext = chipertext = des.decryptByte(chipertext, key_c_v);
    	
    	int TS_6 = (((int)plaintext[0]) & 0x000000FF)
	    		  + ((((int)plaintext[1]) << 8) & 0x0000FF00)
	    		  + ((((int)plaintext[2]) << 16) & 0x00FF0000)
	    		  + ((((int)plaintext[3]) << 24) & 0xFF000000);
    	
    	Log.println("TS_6: " + TS_6);
    	if (TS_6 == TS_5 + 1) {
    		return "login successful";
    	} else {
    		return "TS_6 time error";
    	}
	}

	private byte[] requestAS() {
		// TODO Auto-generated method stub
	    	/**
	   	 * ��AS��������������
	   	 */
	   	
	   	// ����AS������
	   	boolean isConnect;
	   	isConnect = this.connectServer(AS_IP, AS_PORT);
	   	if (!isConnect) {
	   		byte[] res = new byte[1];
	   		this.closeSocket();
	   		res[0] = MsgTag.CONNECT_FAILED;
	   		return res;
	   	}
	   	
	   	
	   	// ��AS�������������󣬷��ؽ��
	   	byte[] message = this.createMessageToAS();
	   	
	   	// ��AS������д������
	   	try {
			this.writeBytes(message);
		} catch (IOException e) {
			byte[] res = new byte[1];
			res[0] = MsgTag.WRITE_FAILED;
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.closeSocket();
			return res;
		}
	   	
	   	// ��AS��������ȡ����
	   	byte[] reply;
		try {
			reply = this.readBytes(25);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			byte[] res = new byte[1];
			res[0] = MsgTag.READ_FAILED;
			e.printStackTrace();
			this.closeSocket();
			return res;
		}
	   	
	   	this.closeSocket();
	   	return reply;

	}



	/**
     * ��TGS��������������
     * @param replyTGS ��������
     * @return ���ؽ��
     */
    private byte[] requestTGS(byte[] replyAS) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
    	/**
	   	 * ��AS��������������
	   	 */
	   	
	   	// ����TGS������
	   	boolean isConnect;
	   	isConnect = this.connectServer(TGS_IP, TGS_PORT);
	   	if (!isConnect) {
	   		byte[] res = new byte[1];
	   		this.closeSocket();
	   		res[0] = MsgTag.CONNECT_FAILED;
	   		return res;
	   	}
	   	
	   	
	   	// ��TGS�������������󣬷��ؽ��
	   	byte[] message = this.createMessageToTGS(replyAS);
	   	
	   	// ��TGS������д������
	   	try {
			this.writeBytes(message);
		} catch (IOException e) {
			byte[] res = new byte[1];
			res[0] = MsgTag.WRITE_FAILED;
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.closeSocket();
			return res;
		}
	   	
	   	// ��TGS��������ȡ����, ע���С
	   	byte[] reply;
		try {
			reply = this.readBytes(29);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			byte[] res = new byte[1];
			res[0] = MsgTag.READ_FAILED;
			e.printStackTrace();
			this.closeSocket();
			return res;
		}
	   	
	   	this.closeSocket();
	   	return reply;
	}
    
	/**
     * ��Ӧ�÷�������������
     * @param replyAPPS ��������
     * @return ���ؽ��
     */
    private byte[] requestAPPS(byte[] replyTGS) {
		// TODO Auto-generated method stub
    	// TODO Auto-generated method stub
    	/**
	   	 * ��APPS��������������
	   	 */
	   	
	   	// ����APPS������
	   	boolean isConnect;
	   	isConnect = this.connectServer(APPS_IP, APPS_PORT);
	   	if (!isConnect) {
	   		byte[] res = new byte[1];
	   		this.closeSocket();
	   		res[0] = MsgTag.CONNECT_FAILED;
	   		return res;
	   	}
	   	
	   	
	   	// ��APPS�������������󣬷��ؽ��
	   	byte[] message = this.createMessageToAPPS(replyTGS);
	   	
	   	// ��APPS������д������
	   	try {
			this.writeBytes(message);
		} catch (IOException e) {
			byte[] res = new byte[1];
			res[0] = MsgTag.WRITE_FAILED;
			// TODO Auto-generated catch block
			e.printStackTrace();
			return res;
		}
	   	
	   	// ��APPS��������ȡ����
	   	byte[] reply;
		try {
			reply = this.readBytes(9);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			byte[] res = new byte[1];
			res[0] = MsgTag.READ_FAILED;
			e.printStackTrace();
			return res;
		}
	   	
	   	return reply;

	}
    
    

    /**
     * ���ɷ���APPS������
     * @param replyTGS
     * @return
     */
    private byte[] createMessageToAPPS(byte[] replyTGS) {
		// TODO Auto-generated method stub
    	// ��AS�������������󣬷��ؽ��
    	byte[] res = new byte[33];
	   	res[0] = MsgTag.CLIENT_TO_V;
	    
	   	byte[] chiperText = new byte[24];
	   	System.arraycopy(replyTGS, 1, chiperText, 0, chiperText.length);
	   	byte[] plainText = des.decryptByte(chiperText, this.key_c_tgs);
	   	
	   	this.key_c_v = ((long) plainText[0] & 0xFF)+((((long)plainText[1]) << 8) & 0xFF00);
	   	Log.println("key_c_v: " + key_c_v);
	   	
	   	System.arraycopy(plainText, 8, res, 1, 16);
	   	
	   	byte[] authenticator_c = new byte[16];
	   	authenticator_c[0] = (byte) (this.id);
	   	authenticator_c[1] = (byte) (this.id >> 8);
	   	
	   	this.TS_5 = Time.getSecondTimestamp(new Date());
	   	
	   	authenticator_c[4] = (byte) (TS_5);
	   	authenticator_c[5] = (byte) (TS_5 >> 8);
	   	authenticator_c[6] = (byte) (TS_5 >> 16);
	   	authenticator_c[7] = (byte) (TS_5 >> 24);
	   	Log.println("TS_5: " + TS_5);
	   	
	    /***************** ADc��װ ****************/
		
	    byte[] ADc = socket.getLocalAddress().getAddress();
	    System.arraycopy(ADc, 0, authenticator_c, 8, ADc.length);
	   	
	   	authenticator_c = des.encryptByte(authenticator_c, key_c_v);
	   		   		   	
	   	System.arraycopy(authenticator_c, 0, res, 17, authenticator_c.length);
	   	
		return res;
	}

	/**
     * ���ɷ���TGS������
     * @param replyAS
     * @return
     */
	private byte[] createMessageToTGS(byte[] replyAS) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
    	// ��AS�������������󣬷��ؽ��
    	byte[] res = new byte[37];
	   	res[0] = MsgTag.CLIENT_TO_TGS;
	    res[1] = APPS_ID;
    	
	    // ��װticket_tgs
	    byte[] chiperText = new byte[24];
	    System.arraycopy(replyAS, 1, chiperText, 0, chiperText.length);
	    byte[] plainText = des.decryptByte(chiperText, this.psw_h);
	    
	    // ȷ��key_c_tgs
	    this.key_c_tgs = ((long) plainText[0] & 0xFF)+((((long)plainText[1]) << 8) & 0xFF00);
	    Log.println("get key_c_tgs: " + this.key_c_tgs);
	    // ȷ��TS_4
	    this.TS_2 = (((int)plainText[4]) & 0xFF)
	    		  + ((((int)plainText[5]) << 8) & 0xFF00)
	    		  + ((((int)plainText[6]) << 16) & 0xFF0000)
	    		  + ((((int)plainText[7]) << 24) & 0xFF000000);
	    
	    Log.println("ts2: " + this.TS_2);
	    byte[] ticket_tgs = new byte[16];
	    System.arraycopy(plainText, 8, ticket_tgs, 0, ticket_tgs.length);
	    System.arraycopy(ticket_tgs, 0, res, 5, ticket_tgs.length);
	    
	    // ������֤authenticator_c
	    byte[] authenticator_c = new byte[16];
	    authenticator_c[0] = (byte) this.id;
	    authenticator_c[1] = (byte) (this.id >> 8);
	    int cnt_time = Time.getSecondTimestamp(new Date());
	    
	    Log.println("TS_3: " + cnt_time);
	    authenticator_c[4] = (byte) (cnt_time);
	    authenticator_c[5] = (byte) (cnt_time >> 8);
	    authenticator_c[6] = (byte) (cnt_time >> 16);
	    authenticator_c[7] = (byte) (cnt_time >> 24);
	    
	    /***************** ADc��װ ****************/
	    		
	    byte[] ADc = socket.getLocalAddress().getAddress();
	    System.arraycopy(ADc, 0, authenticator_c, 8, ADc.length);
	    
	    /*************************************/
	    
	    authenticator_c = des.encryptByte(authenticator_c, key_c_tgs);
	    
	    System.arraycopy(authenticator_c, 0, res, 21, authenticator_c.length);
		return res;
	}

	/**
     * �����صı��Ľ��з���
     * ���ݲ�ͬ����������һ��Ҫ���͵ı���
     * @param replyAS
     * @return
     */
    private byte[] methodChoose(byte[] reply) {
		// TODO Auto-generated method stub
		return null;
	}



	/**
     * �����ײ������ݽ���ƴ�ӣ���������
     * @param Tag
     * @param Message
     * @return
     */
	private byte[] compageMessage(byte Tag, byte[] Message) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     * ���ɷ���AS�ı���
	 * @param id 
     * @return
     */
    private byte[] createMessageToAS() {
		// TODO Auto-generated method stub
    	// ��AS�������������󣬷��ؽ��
    	byte[] res = new byte[9];
	   	res[0] = MsgTag.CLIENT_TO_AS;
	    res[1] = (byte) this.id;
	    res[2] = (byte) ((id >> 8) & 0x00FF);
	    res[3] = AS_ID;
	    
	    int cnt_time = Time.getSecondTimestamp(new Date());
	    res[5] = (byte) cnt_time;
	    res[6] = (byte) (cnt_time >> 8);
	    res[7] = (byte) (cnt_time >> 16);
	    res[8] = (byte) (cnt_time >> 24);
	    
//	    Log.printBinaryln(cnt_time);
//	    
//	    for (int i = 5; i < 9; i ++) {
//	    	Log.printBinaryln(res[i]);
//	    }
    	
		return res;
	}
    
    public boolean closeAPPSSocket() {
    	
    	byte[] msg = new byte[1];
    	msg[0] = MsgTag.CLOSE_APPS_SOCKET;
    	try {
			this.writeBytes(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.println("write msg to APPS failed");
			return false;
		}
    	
    	byte[] reply;
    	try {
    		reply = this.readBytes(1);
			Log.printBinaryln(reply[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.println("read msg from APPS failed");
			return false;
			
		}
    	
    	if (msg[0] == MsgTag.CLOSE_APPS_SOCKET)
    		return true;
    	else
    		return false;
    	
    }
    
    public String updateFile(String path) {
    	
    	byte[] msg = new byte[1];
    	msg[0] = MsgTag.UPDATE_FILE;
    	
    	try {
			this.writeBytes(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "write to APPS error";
		}
    	
    	byte[] reply;
    	try {
			reply = this.readBytes(3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "read from APPS error";
		}
    	
    	int port = (((int)reply[1]) & 0x00FF) + ((((int) reply[2]) << 8) & 0xFF00);
    	Log.println("port: " + port);
    	
    	if (reply[0] == MsgTag.DENY_UPDATE) {
    		return "deny update";
    	} else if (reply[0] == MsgTag.PERMIT_UPDATE) {
    		
	    	Thread t = new Thread() {
				public void run() {
					sendFile(path, port);
				}
			};
			
			t.start();
    		return "send successfully: " + path;
 
    	} else {
    		return "unknown error";
    	}
    	
    }


	private boolean sendFile(String path, int port) {
		// TODO Auto-generated method stub
		
		String log = Time.getCurrentTime() + "\n";
    	log += "��ǰClient��Ӧ��Կ: " + psw_h + "\n";
    	log += "��ʼ���м��ܴ���...\n";
    	this.fileFrame.append(log);
    	
        try {
        	FileTransferClient client = new FileTransferClient(APPS_IP, port, psw_h); // �����ͻ�������  
			client.sendFile(path);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} // �����ļ�  
		
	}
	
	/**
	 * ��ȡ�ļ�����
	 * @return
	 */
	public String getFilename() {
		
		byte[] request = new byte[1];
		request[0] = MsgTag.REQUEST_FILENAME;
		try {
			this.writeBytes(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "write to APPS error";
		}
    	
    	byte[] reply;
    	try {
			reply = this.readBytes(5);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "read from APPS error";
		}
    	
    	
    	if (reply[0] != MsgTag.REPLY_FILENAME) {
    		return "unknown error";
    	} 
    	
    	int length = ((int) reply[1] & 0xFF)
    			   + (((int) reply[2]) << 8 & 0xFF00)
    			   + (((int) reply[3]) << 16 & 0xFF0000)
    			   + (((int) reply[4]) << 24 & 0xFF000000);
    	Log.println("filename_l: " + length);
    	byte[] res;
		try {
			res = this.readBytes(length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "read from APPS error";
		}
    	
    	try {
			return new String(res, "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "encode error";
		}
		
	}
	
	/**
	 * ��ȡ�����û�
	 * @return
	 */
	public String getCntusr() {
		
		byte[] request = new byte[1];
		request[0] = MsgTag.GET_CNT_USR;
		try {
			this.writeBytes(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "write to APPS error";
		}
    	
    	byte[] reply;
    	try {
			reply = this.readBytes(5);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "read from APPS error";
		}
    	
    	
    	if (reply[0] != MsgTag.CNT_USR_SUCCESS) {
    		return "unknown error";
    	} 
    	
    	int length = ((int) reply[1] & 0xFF)
    			   + (((int) reply[2]) << 8 & 0xFF00)
    			   + (((int) reply[3]) << 16 & 0xFF0000)
    			   + (((int) reply[4]) << 24 & 0xFF000000);
    	Log.println("username_l: " + length);
    	byte[] res;
		try {
			res = this.readBytes(length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "read from APPS error";
		}
    	
    	try {
			return new String(res, "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "encode error";
		}
		
	}
	
	
	
	
	public String downloadFile(String path, int index) {
		
		String log = Time.getCurrentTime() + "\n";
    	log += "��ǰClient��Ӧ��Կ: " + psw_h + "\n";
    	log += "��ʼ���н��ܲ�����...\n";
    	this.fileFrame.append(log);
	    	
    	byte[] msg = new byte[9];
    	msg[0] = MsgTag.DOWNLOAD_FILE;
    	msg[1] = (byte) index;
    	msg[2] = (byte) (index >> 8);
    	msg[3] = (byte) (index >> 16);
		msg[4] = (byte) (index >> 24);
		
		Random random = new Random();
		int ran = 1025;
		while (true) {
			// ��ȡ1025-65535֮��������
			ran = random.nextInt(64510);
			ran += 1025;
			// ���˿�û�б�ռ��,������ѭ��
			if (isOpenPort(ran))
				break;
		}
		
		msg[5] = (byte) ran;
    	msg[6] = (byte) (ran >> 8);
    	msg[7] = (byte) (ran >> 16);
		msg[8] = (byte) (ran >> 24);
		
		Thread t = new FileServerThread(path, ran, psw_h);
		t.start();
		
    	try {
			this.writeBytes(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			t.stop();
			return "write to APPS error";
		}
    	
    	byte[] reply;
    	try {
			reply = this.readBytes(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			t.stop();
			return "read from APPS error";
		}
    	
    	if (reply[0] != MsgTag.PERMIT_DOWNLOAD) {
    		t.stop();
    		return "download denyed";
    	} else {
    		return "download now";
    	}
    	
	}
	 
	// �ж϶˿��Ƿ�ռ��
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
	 
	public String getResendSocket() {
		
		byte[] msg = new byte[1];
    	msg[0] = MsgTag.REQUEST_RESEND_SOCKET;
    	
    	try {
			this.writeBytes(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "write to APPS error";
		}
    	
    	Thread t = null;
    	try {
			t = new FileListeningThread(new Socket(APPS_IP, APPS_BRIDGE_PORT), this);
			t.start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "connect APPS_BRIDGE_PORT error";
		}
    	
    	byte[] reply;
    	try {
			reply = this.readBytes(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			t.stop();
			return "read from APPS error";
		}
    	
    	if (reply[0] == MsgTag.RESEND_SOCKET_SUCCESS) {
    		return "bridge success";
    	} else if (reply[0] == MsgTag.RESEND_SOCKET_FAILED) {
    		t.stop();
    		return "bridge failed";
    	} else {
    		t.stop();
    		return "unknown error";
    	}
	}

	/**
	 * �������˷����ļ�����
	 * @return
	 */
	public String sendFileToClient(short id) {
		
		String log = "";
		
		log += Time.getCurrentTime() + "\n";
		
		Log.println("�����ļ���: " + id);
		log += "�����ļ���: " + id + "\n\n";
		byte[] request = new byte[33];
		request[0] = MsgTag.SEND_TO_CLIENT;
		
		// Ŀ�ĵ�ַ
		request[1] = (byte) id;
		request[2] = (byte) (id >> 8);
		
		// Դ��ַ
		request[3] = (byte) this.id;
		request[4] = (byte) (this.id >> 8);
		
		String[] key = FileHandle.readToString("./security/key_" + this.id + ".txt").split(" ");
		BigInteger N = new BigInteger(key[1]);
		BigInteger pri = new BigInteger(key[2]);
		BigInteger pub = new BigInteger(key[3]);
		
		log += "����˽Կ: " + pri + "\n";
		log += "���ع�Կ: " + pub + "\n\n";
		
		int cnt_time = Time.getSecondTimestamp(new Date());
		String auth = rsa.endecry(BigInteger.valueOf(cnt_time), pri, N).toString();
		
		log += "��ȡ��ǰʱ���: " + cnt_time + "\n";
		log += "˽Կ���ܺ�ʱ���: " + auth + "\n\n";
		
		String certificate = FileHandle.readToString("./security/certificate_" + this.id + ".txt");
		
		String getFileHash = null;
		try {
			getFileHash = FileHash.md5HashCode32(this.getWaitTransferFile());
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		BigInteger fileHash = new BigInteger(getFileHash, 16);
		log += "˽Կ����ǰ�ļ�Hashֵ: " + fileHash + "\n";
		Log.println("fileHash: " + getFileHash);
		getFileHash = rsa.endecry(fileHash, pri, N).toString();
		log += "˽Կ���ܺ��ļ�Hashֵ: " + getFileHash + "\n\n";
		Log.println("fileHash: " + getFileHash);
		// ��֤�����û�˽Կǩ��ʱ���+֤��
		String all = auth + " " + certificate + " " + getFileHash;
		
		log += "��ȡ֤��: " + certificate + "\n";
		log += "������֤����: " + all;
		
		Log.println("auth: " + all);
		byte[] all_b = null;
		try {
			all_b = all.getBytes("GBK");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		request[5] = (byte) all_b.length;
		request[6] = (byte) (all_b.length >> 8);
		Log.println("length: " + all_b.length);
		
		this.fileFrame.append(log);
		
		
		try {
			this.writeBytes(request);
			this.writeBytes(all_b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "write to APPS error";
		}
    	
    	byte[] reply;
    	try {
			reply = this.readBytes(7);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "read from APPS error";
		}
    	
    	
    	if (reply[0] != MsgTag.SEND_TO_CLIENT) {
    		return "unknown error";
    	}
    	
    	return "request success ";

	}
	
	public String denyFileFromClient(short clientId) {
		
		byte[] request = new byte[3];
		request[0] = MsgTag.TO_CLIENT_FAILED;
		request[1] = (byte) clientId;
		request[2] = (byte) (clientId >> 8);
		try {
			this.writeBytes(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "write to APPS error";
		}
    	
    	byte[] reply;
    	try {
			reply = this.readBytes(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "read from APPS error";
		}
    	
    	
    	if (reply[0] != MsgTag.SEND_TO_CLIENT) {
    		return "unknown error";
    	}
    	
    	return "request success ";
		
	}
	
	public String receiveFileFromClient(String path, short clientID, BigInteger fileData) {
		
		
		
		Log.println("filehash: " + fileData);
		byte[] request = new byte[11];
		request[0] = MsgTag.TO_CLIENT_SUCCESS;
		request[1] = (byte) clientID;
		request[2] = (byte) (clientID >> 8);
    	byte[] ipAddress = IP.getIPAddress();
    	System.arraycopy(ipAddress, 0, request, 3, ipAddress.length);

    	Random random = new Random();
		int ran = 1025;
		while (true) {
			// ��ȡ1025-65535֮��������
			ran = random.nextInt(64510);
			ran += 1025;
			// ���˿�û�б�ռ��,������ѭ��
			if (isOpenPort(ran))
				break;
		}
		
		short port = (short) (ran & 0x0000FFFF);
		Log.println(((int) port & 0x0000FFFF));
		
		request[7] = (byte) port;
		request[8] = (byte) (port >> 8);
		
		short key = (short) random.nextInt(10000);
		
		request[9] = (byte) key;
		request[10] = (byte) (key >> 8);
		Log.println("file transfer key: " + key);
		String log = Time.getCurrentTime() + "\n";
    	log += "������ʱ����Ự��Կ: " + key + "\n";
    	log += "׼�������ļ�������...\n";
    	
    	this.fileFrame.append(log);
		
		new FileServerThread(path, ran, fileData, key).start();
		Log.println("�Ѵ򿪶˿�");
		Log.println("clientID: " + clientID);
		try {
			this.writeBytes(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "write to APPS error";
		}
    	
    	byte[] reply;
    	try {
			reply = this.readBytes(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "read from APPS error";
		}
    	
    	
    	if (reply[0] != MsgTag.SEND_TO_CLIENT) {
    		return "unknown error";
    	}
    	
    	return "request success ";
	}
	
	/**
	 * ��ȡ֤��
	 * 
	 * ֤���ʽ����ԿN + ASǩ����֤�ģ�id+��Կ��
	 * @return
	 */
	public String getCertificate(short usr) {
		
		byte[] message = null;
			// TODO Auto-generated method stub
	    	/**
	   	 * ��AS��������������
	   	 */
	   	
	   	// ����AS������
	   	boolean isConnect;
	   	isConnect = this.connectServer(AS_IP, AS_PORT);
	   	if (!isConnect) {
	   		this.closeSocket();
	   		return "get certificate successful failed";
	   	}
	   	
	   	byte[] pub_key = this.createKey(usr);
	    short length = (short) pub_key.length;
	    Log.println("length: " + length);
	    message = new byte[ pub_key.length + 11 ];
	    message[0] = MsgTag.REQUEST_CERTIFICATE;
	    message[1] = (byte) length;
	    message[2] = (byte) (length >> 8);
	    message[3] = (byte) usr;
	    message[4] = (byte) (usr >> 8);
	    System.arraycopy(pub_key, 0, message, 11, pub_key.length);
	    
	   	
	   	// ��AS������д������
	   	try {
			this.writeBytes(message);
		} catch (IOException e) {
			byte[] res = new byte[1];
			res[0] = MsgTag.WRITE_FAILED;
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.closeSocket();
			return "get certificate successful failed";
		}
	   	
	   	// ��AS��������ȡ����
	   	byte[] reply;
	   	byte[] key_chiper;
		try {
			reply = this.readBytes(3);
			int len = ((int) reply[1] & 0xFF) + (((int) reply[2]) << 8 & 0xFF00);
			Log.println("len: " + len);
			key_chiper = this.readBytes(len);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.closeSocket();
			return "get certificate successful failed";
		}
	   	this.closeSocket();
	   	BigInteger key_b = new BigInteger(key_chiper);
	   	
//	   	byte[] id_pub = rsa.endecry(key_b, ASKey.AS_pub, ASKey.AS_n).toByteArray();
//	   	byte[] pub = new byte[ id_pub.length - 2 ];
//	   	System.arraycopy(id_pub, 2, pub, 0, pub.length);   	
//	   	Log.println("encrypt key: " + key_b);
//	   	Log.println("decrypt key: " + new BigInteger(pub));
	   	
	   	FileHandle.append("./security/certificate_" + usr + ".txt", " " + key_b.toString());
	   	
	   	return "get certificate successful";
		
	}
	
	private byte[] createKey(short usr) {
		// TODO Auto-generated method stub
	   	BigInteger[] key = rsa.createKey(0);
	   	BigInteger N = key[0];
	   	BigInteger pub_key = key[1];
	   	BigInteger pri_key = key[2];
	   	Log.println("pub_key: " + pub_key);
	   	
	   	try {
			FileHandle.creat("./security/key_" + usr + ".txt", usr + " " + N.toString()
									   + " " + pri_key.toString() 
									   + " " + pub_key.toString());
			FileHandle.creat("./security/certificate_" + usr + ".txt", N.toString());
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return pub_key.toByteArray();
	}

	public static void main(String[] args) {
    	
    	Client client = new Client();
    	//Log.println(client.login((short) 111, "111"));
    	client.getCertificate((short) 257);
//        try {
//        	
//        	
//        	Log.println("���ӳɹ���");
//            byte[] data = { 0x11, (byte) 0xAA, (byte) 0xFF};
//            client.writeBytes(data);
//            Log.println("д��ɹ���");
//            data = client.readBytes(2);
//            Log.println("��ȡ�ɹ���");
//            Log.printBinaryln(data[0]);
//            Log.printBinaryln(data[1]);
//   
//        } catch (IOException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

	


}