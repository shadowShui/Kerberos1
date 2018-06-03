package Client;

import java.math.BigInteger;

import File.FileTransferServer;

public class FileServerThread extends Thread {
	
	FileTransferServer server = null;
	
	public FileServerThread(String path, int port) {
		
		try {
			server = new FileTransferServer(path, port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public FileServerThread(String path, int port, long key) {
		
		try {
			server = new FileTransferServer(path, port, key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public FileServerThread(String path, int port, BigInteger fileData) {
		
		
		try {
			server = new FileTransferServer(path, port, fileData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public FileServerThread(String path, int ran, BigInteger fileData, long key) {
		// TODO Auto-generated constructor stub
		try {
			server = new FileTransferServer(path, ran, fileData, key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		
		try {
			server.load();  
            server.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
