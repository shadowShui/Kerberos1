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
	
	 /**  ���������            */
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

    //�߳�ִ�еĲ�������Ӧ������������
    public void run(){
    	
    	while (true) {
	        try {
	            byte[] serverMsg = this.readBytes(33);
	            Log.println("��ȡ�ɹ���");
	          
	            this.msgHandle(serverMsg);
	            
	        } catch (IOException e) {
	        	
	        	Log.println("�ȴ�������������Ϣ...");
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
		Log.println("�Է��ܾ������ļ���");
	}

	private void startSend(byte[] msg) {
		// TODO Auto-generated method stub
		
		Log.println("��ʼ�����ļ�.....");
		
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
    	log += "��ȡ����ʱ�Ự��Կ: " + key + "\n";
    	log += "��ʼ���м��ܴ���...\n";
    	
    	this.client.getFileFrame().append(log);
    	
    	try {
        	FileTransferClient client = new FileTransferClient(ip, port, key); // �����ͻ�������  
			client.sendFile(this.client.getWaitTransferFile());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleFileRequest(byte[] msg) throws IOException {
		// TODO Auto-generated method stub
		Log.println("���յ������ļ����룡");
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
		
//		// �û�ȡ��
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
//			// ��ȡ1025-65535֮��������
//			ran = random.nextInt(64510);
//			ran += 1025;
//			// ���˿�û�б�ռ��,������ѭ��
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
}
