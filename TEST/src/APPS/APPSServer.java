package APPS;

/**
 * ����TCPЭ���Socketͨ�ţ�ʵ���û���½
 * ��������
 */


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import APPS.gui.APPSFrame;
import AS.ASServerThread;
import AS.gui.ASFrame;
import System.Log;


public class APPSServer {
	
    private static ServerSocket serverSocket;
    private static Socket socket = null;
    
    private static ArrayList<APPSServerThread> threadHub = new ArrayList<APPSServerThread>();
    private static ArrayList<Socket> socketHub = new ArrayList<Socket>();
    
    private static Thread t;
    
    private static APPSFrame appsFrame;
    
    public static void setFrame(APPSFrame AppsFrame) {
    	appsFrame = AppsFrame;
    }
    
    public static void start() {
		// TODO Auto-generated method stub
    	t = new Thread(){ 
        	
        	public void run() {
        		 try {
        	        	ClientHub.init();
        	            //1.����һ����������Socket����ServerSocket��ָ���󶨵Ķ˿ڣ��������˶˿�
        	            serverSocket = new ServerSocket(8899);
        	            //��¼�ͻ��˵�����
        	            int count = 0;
        	            Log.println("***�����������������ȴ��ͻ��˵�����***");
        	            //ѭ�������ȴ��ͻ��˵�����
        	            while(true) {
        	            	
        	                //����accept()������ʼ�������ȴ��ͻ��˵�����
        	                socket = serverSocket.accept();
        	                //����һ���µ��߳�
        	                APPSServerThread serverThread = new APPSServerThread(socket, appsFrame);
        	                //�����߳�
        	                serverThread.start();

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
    
    public static void stop() {
    	
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
			
			for (int i = 0; i < threadHub.size(); i ++) {
				socketHub.get(i).close();
				threadHub.get(i).stop();
			}
			ClientHub.usermap.clear();
			
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