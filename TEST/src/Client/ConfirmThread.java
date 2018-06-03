/**
 * 用于子线程弹出消息确认对话框
 * 
 * 解决子线程无法弹出问题
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
        	// 对方身份确认
        	if (!res[0].equals("false"))
        		n = JOptionPane.showConfirmDialog(null, "<html><body>是否接收来自" + clientID + "的文件？<br> "
    				+ "（安全）对方身份已认证</body></html>", "文件发送请求", JOptionPane.YES_NO_OPTION); 
        	else
        		n = JOptionPane.showConfirmDialog(null, "<html><body>是否接收来自" + clientID + "的文件？<br> "
        			+ "（危险）对方身份无法认证，不建议接收</body></html>", "文件发送请求", JOptionPane.YES_NO_OPTION); 
        		
        		
        	if (n == JOptionPane.YES_OPTION) { 
        		
        		Log.println("已接受！");
        		
        		String path = new DownloadChooser().getSelected();
            	if (path.equals("")) {
        			JOptionPane.showMessageDialog(null, "请选择一个下载路径！");
        		} else {
        		
        			this.myClient.getFileFrame().append(Auth.getFileHashLog(auth)); 
        			myClient.receiveFileFromClient(path, clientID, Auth.getFileData(auth));
        			break;
        		}
        	} else {
        		Log.println("已拒绝！");
        		myClient.denyFileFromClient(clientID);
        		break;
        	}
    	}
//            }
//        });
    }
 
}
