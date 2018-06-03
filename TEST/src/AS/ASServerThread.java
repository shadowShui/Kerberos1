package AS;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcherFactory;

import AS.gui.ASFrame;
import Crypt.DES;
import Crypt.RSA;
import System.Time;
import System.Log;
import System.MsgTag;


public class ASServerThread extends Thread {
	
	private static final int LifeTime = 8;			//设置生命周期为8s

	private static final long Ektgs = 456;			// AS和TGS公有秘钥
	private final long Ekctgs = 888;				// TGS至C会话秘钥
	private DES des = new DES();
	private RSA rsa = new RSA(1024);
	
	private ASFrame asFrame;

	
	
    /**  和本线程相关的Socket */
    private Socket socket = null;
    
    /**  输入输出流            */
    private InputStream is = null;
    private DataInputStream dis = null;

    private OutputStream os = null;
    private DataOutputStream dos = null;    
    
    public ASServerThread(Socket socket, ASFrame asFrame) {
    	
    	this.asFrame = asFrame;
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
    	is.read(res);//从输入流中读取数据的res长度个字节
    	return res;
    }

    //线程执行的操作，响应客户端的请求
    public void run(){
    	
        try {
            byte[] clientMsg = this.readBytes(11);
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
     * @throws IOException 
     */
	private byte[] msgHandle(byte[] clientMsg) throws IOException {
		
		byte[] res = new byte[1];
		byte tag = clientMsg[0];
		byte[] msg = new byte[ clientMsg.length - 1 ];
		System.arraycopy(clientMsg, 1, msg, 0, msg.length);
		//arraycopy(Object src, int srcPos, Object dest, int destPos, int length) 
        //从指定源数组中复制一个数组，复制从指定的位置开始，到目标数组的指定位置结束。
		Log.printBinaryln(tag);
		
		switch(tag) {
			case MsgTag.CLIENT_TO_AS: return createMsgToClient(msg);
			case MsgTag.REGIST_TO_AS: return regist(msg);
			
			case MsgTag.REQUEST_CERTIFICATE: return sendCertificate(msg);
			default: {
				res[0] = MsgTag.REPLY_FAILED;
				return res;
			}
		}
	}

	
	private byte[] sendCertificate(byte[] msg) throws IOException {
		// TODO Auto-generated method stub
		
		short length =  (short) (((int) msg[0] & 0xFF) + (((int) msg[1]) << 8 & 0xFF00));
		short id =  (short) (((int) msg[2] & 0xFF) + (((int) msg[3]) << 8 & 0xFF00));
		
		Log.println("id: " + id);
		Log.println("length: " + length);
		byte[] user_pub = this.readBytes(length);
		Log.println("get pub: " + new BigInteger(user_pub));
		
		byte[] user_id_pub = new byte[ length + 2 ];
		user_id_pub[0] = msg[2];
		user_id_pub[1] = msg[3];
		System.arraycopy(user_pub, 0, user_id_pub, 2, user_pub.length);
		
		
		BigInteger pub_chiper = rsa.endecry(new BigInteger(user_id_pub), ASKey.AS_pri, ASKey.AS_n);
		Log.println("encrypt key: " + pub_chiper);
		byte[] pub_c_b = pub_chiper.toByteArray(); 
		
		byte[] res = new byte[ pub_c_b.length + 3 ];
		res[0] = MsgTag.CERTIFICATE_SUCCESS;
		res[1] = (byte) pub_c_b.length;
		res[2] = (byte) (pub_c_b.length >> 8);
		Log.println("length: " + pub_c_b.length);
		
		System.arraycopy(pub_c_b, 0, res, 3, pub_c_b.length);
		
		return res;
	}

	private byte[] regist(byte[] msg) {
		
		byte []res = new byte[1];
		// TODO Auto-generated method stub
		Log.printBinaryln(msg[0]);
		Log.printBinaryln(msg[1]);
		short id = (short) ( msg[0] + (((short) msg[1]) << 8));
		byte[] psw_b = new byte[8];
		System.arraycopy(msg, 2, psw_b, 0, psw_b.length);
		long[] psw_l = new DES().byteToLong(psw_b);
		long psw = psw_l[0];
		Log.println(id + "\n" + psw);
		
		DBHandler dbh = new DBHandler("as_usr");
		boolean b = dbh.insert("insert into regist(ID, psw)values(?, ?)",
				new String[] { id + "", psw + ""});
		
		res[0] = (b == true) ? MsgTag.REGIST_SUCCESS : MsgTag.REGIST_FAILRD;
		return res;
	}

	//获取ADc：Client的网络地址
	private byte[] getIP()
	{
		byte [] IP = new byte[4];
		try {
			InetAddress address = InetAddress.getLocalHost();//获取的是本地的IP地址 //PC-20140317PXKX/192.168.0.121
			String hostAddress = address.getHostAddress();//得到IP地址为string类型
			System.out.println(hostAddress);
			String[]ip_string = hostAddress.split("\\.");
			int [] ip_int = new int[ip_string.length];
			for(int i=0;i<ip_string.length;i++)
			{
				//得到int型Ip,再转换为byte类型
				ip_int[i]=Integer.parseInt(ip_string[i]);
				//System.out.println(ip_int[i]);
				IP[i]=(byte)(ip_int[i] & 0xff);
				//Log.printBinaryln(IP[i]);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return IP;
	}
	
	//根据IDc判断该用户是否已经注册2018/5/21
	/** 
	 * @param idC 根据用户id查询是否注册
	 * @return 
	 */
	public static boolean haveRegist(byte []idC)
	{
		short id = (short) (idC[0] + (((short) idC[1]) << 8));
		DBHandler db = new DBHandler("as_usr");
		Log.println("ID="+Short.toString(id));
		//return db.checkUser("select * from regist where ID= ?", new String[] {"410"});
		return db.checkUser("select * from regist where ID=?", new String[] {Short.toString(id)});
		
	}
	
	private long getPsw(byte []idC)
	{
		short id = (short) (idC[0] + (((short) idC[1]) << 8));
		DBHandler db = new DBHandler("as_usr");
		Log.println("ID="+Short.toString(id));
		//return db.checkUser("select * from regist where ID= ?", new String[] {"410"});
		String res = db.query("select psw from regist where ID=?", new String[] {Short.toString(id)});
		return Long.parseLong(res.trim());
	}
	
	private byte[] createMsgToClient(byte[] clientMsg) {
		// TODO Auto-generated method stub'
		byte[] res = new byte[25];
		res[0] = MsgTag.AS_TO_CLIENT;
		byte[] id_c = new byte[2];
		byte[] id_tgs = new byte[1];
		//临时保存ID(c)与ID(tgs)
		System.arraycopy(clientMsg, 0, id_c, 0, 2);
		System.arraycopy(clientMsg, 2, id_tgs, 0, 1);
		
	    String log = Log.logC2A(".\\logC2A.txt", clientMsg,id_c);
	    asFrame.append(log);
	    
		//判断是否已经注册
		if(haveRegist(id_c))
		{
			byte[] res_p = new byte[24];
			Log.println("AS连接成功！已经注册！");
			
			//获取Ekc
			long Ekc = getPsw(id_c);
			
			//根据ID(c)得到Ekc，对后续的报文进行加密
			//K(c,tgs)规定123，低位在前，高位在后
			res_p[0] = (byte) (Ekctgs & 0xff);
			res_p[1] = (byte) (Ekctgs >> 8);

			
			//AS需要从Client->AS消息中提取出ID（tgs）[3]
			res_p[2]=id_tgs[0];
			
			//Lifetime2多长时间  [4]=8s
			res_p[3] = LifeTime & 0xff;
			
			//Ts2 使用当前的时间进行填充 [5]-[8]，低位在前，高位在后
			Date dd = new Date();
			int ts = Time.getSecondTimestamp(dd);
			Log.println("ts2: " + ts);
			
			byte[] ts2 = new byte[4];
	        ts2[0] = (byte) (ts & 0xff);// 最低位  
	        ts2[1] = (byte) ((ts >> 8) & 0xff);// 次低位
	        ts2[2] = (byte) ((ts >> 16) & 0xff);// 次高位
	        ts2[3] = (byte) (ts >>> 24);// 最高位,无符号右移。
	        System.arraycopy(ts2, 0, res_p, 4, 4);
	        
	        byte[] ticket_tgs = new byte[16];
			//Ticket(tgs)Tickettgs  (E_(K_tgs )[ Kc,tgs || IDC || ADC || IDtgs || TS2 || Lifetime2 ])[9]-[24]	
			//[9]-[10]-K(c,tgs),低位在前，高位在后
	        ticket_tgs[0]=(byte) (Ekctgs & 0xff);
	        ticket_tgs[1]=(byte) (Ekctgs >> 8);
			
			//[11]ID-(tgs)
	        ticket_tgs[2]=id_tgs[0];
			
			//[12]-LifeTime2
	        ticket_tgs[3] = LifeTime & 0xff;
			
			//[13][14]-ID(c)
			System.arraycopy(id_c, 0, ticket_tgs, 4, 2);
			
			//[15]-[16]-保留字段		
			
			//[17]-[20]-TS2
			System.arraycopy(ts2, 0, ticket_tgs, 8, 4);
			
			//[21]-[24]-AD(c)
			byte[] ADc = socket.getInetAddress().getAddress();
			System.arraycopy(ADc, 0, ticket_tgs, 12, 4);
			//输出日志
			log = Log.logA2C(".//logA2C.txt", ticket_tgs, res_p);
			asFrame.append(log);
			//ticket进行加密
			ticket_tgs = des.encryptByte(ticket_tgs, Ektgs);
			
			System.arraycopy(ticket_tgs, 0, res_p, 8, ticket_tgs.length);
			
			//报文加密
			byte[] res_c = des.encryptByte(res_p, Ekc);
			//拼在后面
			System.arraycopy(res_c, 0, res, 1, res_c.length);
		}
		else
		{
			Log.println("未注册用户！");
			res[0] = MsgTag.REPLY_FAILED;
		}
		return res;
	}
}