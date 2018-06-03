/**
 * �������̵߳�����Ϣȷ�϶Ի���
 * 
 * ������߳��޷���������
 * 
 * @author fangyunniu
 */

package Client;

import java.math.BigInteger;

import javax.swing.JOptionPane;


import Client.gui.DownloadChooser;
import Client.gui.FileFrame;
import System.Log;

public class ConfirmThread extends Thread{
	
	private short clientID;
	private Client myClient = null;
	private Thread t = this;
	private String auth;
	
	public ConfirmThread (short clientID, String auth, Client myClient) {
		this.clientID = clientID;
		this.myClient = myClient;
		this.auth = auth;
	}
	
    public void run() {
//        Display.getDefault().syncExec(new Runnable() {
//            @Override
//            public void run() {
//      
    	String[] res = Auth.confirmUsr(clientID, auth);
    	myClient.getFileFrame().append(res[1]);
    	
    	while (true) {
    		
        	int n;
        	// �Է����ȷ��
        	if (!res[0].equals("false"))
        		n = JOptionPane.showConfirmDialog(null, "<html><body>�Ƿ��������" + clientID + "���ļ���<br> "
    				+ "����ȫ���Է��������֤</body></html>", "�ļ���������", JOptionPane.YES_NO_OPTION); 
        	else
        		n = JOptionPane.showConfirmDialog(null, "<html><body>�Ƿ��������" + clientID + "���ļ���<br> "
        			+ "��Σ�գ��Է�����޷���֤�����������</body></html>", "�ļ���������", JOptionPane.YES_NO_OPTION); 
        		
        		
        	if (n == JOptionPane.YES_OPTION) { 
        		
        		Log.println("�ѽ��ܣ�");
        		
        		String path = new DownloadChooser().getSelected();
            	if (path.equals("")) {
        			JOptionPane.showMessageDialog(null, "��ѡ��һ������·����");
        		} else {
        		
        			this.myClient.getFileFrame().append(Auth.getFileHashLog(auth)); 
        			myClient.receiveFileFromClient(path, clientID, Auth.getFileData(auth));
        			break;
        		}
        	} else {
        		Log.println("�Ѿܾ���");
        		myClient.denyFileFromClient(clientID);
        		break;
        	}
    	}
//            }
//        });
    }
 
}
