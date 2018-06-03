package APPS;

import File.FileTransferServer;

public class FileServerThread extends Thread {
	
	private short usr;
	private int port;
	
	public FileServerThread(short usr, int port) {
		this.usr = usr;
		this.port = port;
	}
	
	public void run() {
		
		try {
			FileTransferServer server = new FileTransferServer("D:\\��������ȫ�γ����\\filecache\\" + usr, port);
			server.load();  
            server.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
