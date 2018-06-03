package TGS;

/**
 * 基于TCP协议的Socket通信，实现用户登陆
 * 服务器端
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
    	            //1.创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口
    	            serverSocket = new ServerSocket(8889);
    	            //记录客户端的数量
    	            int count = 0;
    	            Log.println("***服务器即将启动，等待客户端的连接***");
    	            //循环监听等待客户端的连接
    	            while(true) {
    	            	
    	                //调用accept()方法开始监听，等待客户端的连接
    	                socket = serverSocket.accept();
    	                //创建一个新的线程
    	                TGSServerThread serverThread = new TGSServerThread(socket, tgsFrame);
    	                //启动线程
    	                serverThread.start();
    	                threadHub.add(serverThread);
    	                socketHub.add(socket);
    	
    	                count++;//统计客户端的数量
    	                Log.println("客户端的数量："+count);
    	                InetAddress address = socket.getInetAddress();
    	                Log.println("当前客户端的IP：" + address.getHostAddress());
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
			
			Log.println("***服务器已关闭***");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}