package TGS;

/**
 *
 * 服务器线程处理类
 */
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import System.Log;
import System.MsgTag;
import System.Time;
import Crypt.DES;


public class TGSServerThread_copy extends Thread {
    /** LifeTime*/	
    private static final int LifeTime = 8;//设置生命周期为8s
	private static final long Ekv = 123;
	private static final long key_tgs = 123;
	private final long Ekcv = 123;	// v至C会话秘钥
	
    /**  和本线程相关的Socket */
    private Socket socket = null;
    
    /**  输入输出流            */
    private InputStream is = null;
    private DataInputStream dis = null;

    private OutputStream os = null;
    private DataOutputStream dos = null;
    
    /** c->v 密码*/
    
    public TGSServerThread_copy(Socket socket) {
    
        this.socket = socket;
        this.init();
    }
    
    /**
     * 初始化输入输出流
     */
    private void init() {
    	
    	try {
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 关闭中断连接
     */
    private void close() {
    	
    	try {
    		if (is != null)
    			is.close();
    		if (dis != null)
    			dis.close();
    		if (os != null)
    			os.close();
    		if (dos != null)
    			dos.close();
    		if (socket != null)
    			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 传送byte数组
     * @param data 要传输的数组
     * @throws IOException
     */
    private void writeBytes(byte[] data) throws IOException {
    	
    	os.write(data);
    }
    
    /**
     * 读取Bytes数组，默认大小1024
     * @return 返回获取结果
     * @throws IOException
     */
	private byte[] readBytes() throws IOException {
    	
    	return this.readBytes(1024);
    }
    
    /**
     * 读取Bytes数组，自定义数组大小
     * @param size 要接收的大小
     * @return
     * @throws IOException
     */
    private byte[] readBytes(int size) throws IOException {
    	
    	byte[] res = new byte[size];
    	is.read(res);
    	return res;
    }

    //线程执行的操作，响应客户端的请求
    public void run(){
    	
        try {
            byte[] clientMsg = this.readBytes(37);
            Log.println("读取成功！");
          
            byte[] replyMsg = this.msgHandle(clientMsg);
            this.writeBytes(replyMsg);
        } catch (IOException e) {
        	Log.println("接收消息失败！");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            //关闭资源
            this.close();
        }
        
    }

    /**
     * 服务器对客户端消息进行响应
     * @param clientMsg
     * @return
     */
	private byte[] msgHandle(byte[] clientMsg) {
		
		byte[] res = new byte[1];
		byte tag = clientMsg[0];
		byte[] msg = new byte[ clientMsg.length - 1 ];
		System.arraycopy(clientMsg, 1, msg, 0, msg.length);
		Log.printBinaryln(tag);
		//arraycopy(Object src, int srcPos, Object dest, int destPos, int length) 
        //从指定源数组中复制一个数组，复制从指定的位置开始，到目标数组的指定位置结束。
		switch(tag) {
			case MsgTag.CLIENT_TO_TGS: return createMsgToClient(msg);
			default: {
				res[0] = MsgTag.REPLY_FAILED;
				return res;
			}
		}
	}

	
	private byte[] createMsgToClient(byte[] clientMsg) {
		// TODO Auto-generated method stub'
		byte[] res = new byte[25];
		byte[] res_t = new byte[24];
		byte[] ticket_v = new byte[16];
		byte[] ticket_tgs = new byte[16];
		byte[] authenticator_c = new byte[16];
		System.arraycopy(clientMsg, 4, ticket_tgs, 0, ticket_tgs.length);
		System.arraycopy(clientMsg, 20, authenticator_c, 0, authenticator_c.length);
		
		//解密ticket_tgs
		ticket_tgs = new DES().decryptByte(ticket_tgs, key_tgs);
		
		long key_c_tgs = ((long) ticket_tgs[0] & 0xFF)+((((long)ticket_tgs[1]) << 8) & 0xFF00);
		
		Log.println("key_c_tgs: " + key_c_tgs);
		
		//解密authenticator_c
		authenticator_c = new DES().decryptByte(authenticator_c, key_c_tgs);
		
		res[0] = MsgTag.TGS_TO_CLIENT;

		int TS_2 = (((int)ticket_tgs[8]) & 0x000000FF)
	    		  + ((((int)ticket_tgs[9]) << 8) & 0x0000FF00)
	    		  + ((((int)ticket_tgs[10]) << 16) & 0x00FF0000)
	    		  + ((((int)ticket_tgs[11]) << 24) & 0xFF000000);
		
		int LifeTime_2 = (int) ticket_tgs[3];
		
		Log.println("TS_2 : " + TS_2);
		Log.println("LifeTime_2 : " + LifeTime_2);
		
		if(!Time.isIntime(TS_2, LifeTime_2)){
			res[0] = MsgTag.REPLY_FAILED;
			return res;
		}
		
		//判断idc
		if(ticket_tgs[4] != authenticator_c[0] || ticket_tgs[5] != authenticator_c[1]){
			res[0] = MsgTag.REPLY_FAILED;
			return res;
		}
		
		//判断adc
		if(ticket_tgs[12] != authenticator_c[8] || ticket_tgs[13] != authenticator_c[9]
				|| ticket_tgs[14] != authenticator_c[10]|| ticket_tgs[15] != authenticator_c[11]){
			res[0] = MsgTag.REPLY_FAILED;
			return res;
		}
		
		/*kcv*/
		res_t[0] = (byte) (Ekcv & 0xff);
		res_t[1] = (byte) (Ekcv >> 8);
		
		
		/*idv*/
		res_t[2] = clientMsg[0];
		res_t[3] = clientMsg[1];
		
		
		//Ts4 使用当前的时间进行填充 [5]-[8]，低位在前，高位在后
		Date d = new Date();
		int ts = Time.getSecondTimestamp(d);
		Log.println("ts4: " + ts);
		byte[] ts4 = new byte[4];
        ts4[0] = (byte) (ts & 0xff);// 最低位  
        ts4[1] = (byte) ((ts >> 8) & 0xff);// 次低位
        ts4[2] = (byte) ((ts >> 16) & 0xff);// 次高位
        ts4[3] = (byte) (ts >>> 24);// 最高位,无符号右移。
        System.arraycopy(ts4, 0, res_t, 4, 4);
        /*ticket*/
        /*kcv*/
        ticket_v[0] = (byte) (Ekcv & 0xff);
        ticket_v[1] = (byte) (Ekcv >> 8);
        /*idv*/
        ticket_v[2] = clientMsg[0];
        ticket_v[3] = clientMsg[1];
//        short usr = (short) (((short) clientMsg[0] & 0xFF)
//				+((((short)clientMsg[1]) << 8) & 0xFF00));
//        Log.println("usr: " + usr);
        /*idc*/
        ticket_v[4] = authenticator_c[0];
        ticket_v[5] = authenticator_c[1];
        /*adc*/
        ticket_v[12] = authenticator_c[8];
        ticket_v[13] = authenticator_c[9];
        ticket_v[14] = authenticator_c[10];
        ticket_v[15] = authenticator_c[11];
        
        int TS_3 = (((int)authenticator_c[4]) & 0x000000FF)
	    		  + ((((int)authenticator_c[5]) << 8) & 0x0000FF00)
	    		  + ((((int)authenticator_c[6]) << 16) & 0x00FF0000)
	    		  + ((((int)authenticator_c[7]) << 24) & 0xFF000000);
        
        Log.println("TS_3: " + TS_3);
        
        /*ts4*/
        System.arraycopy(ts4, 0, ticket_v, 8, 4);
        /*lf4*/
        ticket_v[6] = LifeTime & 0xff;
        ticket_v = new DES().encryptByte(ticket_v, Ekv);
        
        System.arraycopy(ticket_v, 0, res_t, 8, 16);
        res_t = new DES().encryptByte(res_t, key_c_tgs);
        
        System.arraycopy(res_t, 0, res, 1, 24);
		return res;
	}
}