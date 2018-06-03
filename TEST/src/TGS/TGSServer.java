package TGS;

/**
 * ����TCPЭ���Socketͨ�ţ�ʵ���û���½
 * ��������
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


import System.Log;
import TGS.gui.TGSFrame;


public class TGSServer {
	
    private static ServerSocket serverSocket;
    private static Socket socket = null;
    
    private static ArrayList<TGSServerThread> threadHub = new ArrayList<TGSServerThread>();
    private static ArrayList<Socket> socketHub = new ArrayList<Socket>();
    
    private static Thread t;
    
    private static TGSFrame tgsFrame;
    
    public static void setFrame(TGSFrame TgsFrame) {
    	tgsFrame = TgsFrame;
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
    	            serverSocket = new ServerSocket(8889);
    	            //��¼�ͻ��˵�����
    	            int count = 0;
    	            Log.println("***�����������������ȴ��ͻ��˵�����***");
    	            //ѭ�������ȴ��ͻ��˵�����
    	            while(true) {
    	            	
    	                //����accept()������ʼ�������ȴ��ͻ��˵�����
    	                socket = serverSocket.accept();
    	                //����һ���µ��߳�
    	                TGSServerThread serverThread = new TGSServerThread(socket, tgsFrame);
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