package AS;

/**
 * ����TCPЭ���Socketͨ�ţ�ʵ���û���½
 * ��������
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import AS.gui.ASFrame;
import System.Log;


public class ASServer {
	
    private static ServerSocket serverSocket;
    private static Socket socket = null;
    
    private static ArrayList<ASServerThread> threadHub = new ArrayList<ASServerThread>();
    private static ArrayList<Socket> socketHub = new ArrayList<Socket>();
    
    private static Thread t;
    
    private static ASFrame asFrame;
    
    public static void setFrame(ASFrame AsFrame) {
    	asFrame = AsFrame;
    }
    
    public static void main(String[] args) {
    	
    	start();
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	stop();
    }
	
    public static void start() {
		// TODO Auto-generated method stub
    	t = new Thread(){ 
        	
        	public void run() {
    	        try {
    	            //1.����һ����������Socket����ServerSocket��ָ���󶨵Ķ˿ڣ��������˶˿�
    	            serverSocket = new ServerSocket(8888);
    	            //��¼�ͻ��˵�����
    	            int count = 0;
    	            Log.println("***�����������������ȴ��ͻ��˵�����***");
    	            //ѭ�������ȴ��ͻ��˵�����
    	            while(true) {
    	            	
    	                //����accept()������ʼ�������ȴ��ͻ��˵�����
    	                socket = serverSocket.accept();
    	                //����һ���µ��߳�
    	                ASServerThread serverThread = new ASServerThread(socket, asFrame);
    	                //�����߳�
    	                serverThread.start();
    	                threadHub.add(serverThread);
    	                socketHub.add(socket);
    	
    	                count++;//ͳ�ƿͻ��˵�����
    	                Log.println("�ͻ��˵�������"+count);
    	                InetAddress address = socket.getInetAddress();
    	                Log.println("��ǰ�ͻ��˵�IP��" + address.getHostAddress());
    	            }
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        }
        	}
        };
		t.start();
	}

	
  
    
    
    public static void stop() {
    	
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
			
			for (int i = 0; i < threadHub.size(); i ++) {
				socketHub.get(i).close();
				threadHub.get(i).stop();
			}
			
			
			if (t != null) {
				t.stop();
			}
			
			Log.println("***�������ѹر�***");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}