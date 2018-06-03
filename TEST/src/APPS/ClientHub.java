/**
 * ���ڽ����ͻ���֮������
 */
package APPS;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import System.Log;

public class ClientHub {
	
	private static ServerSocket serverSocket;
    
    public static HashMap<Short, Bridge> usermap = new HashMap<Short, Bridge>();
    
    public static void init () {
    	try {
			serverSocket = new ServerSocket(8999);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static boolean getRegist(short usr) {
    	
    	Socket socket = null;
		try {
			socket = serverSocket.accept();
			usermap.put(usr, new Bridge(socket));
			Log.println("�û� " + usr + " ���ųɹ���");
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.println("�û� " + usr + " ����ʧ�ܣ�");
			return false;
		}
    	
    }
    
    
}
