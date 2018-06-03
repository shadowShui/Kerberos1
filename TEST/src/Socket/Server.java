package Socket;

/**
 * ����TCPЭ���Socketͨ�ţ�ʵ���û���½
 * ��������
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import System.Log;


public class Server {
	
    private static ServerSocket serverSocket;
    private static Socket socket = null;
	
    public static void main(String[] args) {
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
                ServerThread serverThread = new ServerThread(socket);
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
}