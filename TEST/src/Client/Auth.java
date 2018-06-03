package Client;

import java.math.BigInteger;

import Crypt.RSA;
import System.Log;
import System.Time;

public class Auth {

	public static String[] confirmUsr(short clientID, String auth) {
		
		String[] res = new String[2];
		res[0] = "true";
		res[1] = "";
		res[1] += Time.getCurrentTime() + "\n";
		res[1] += "对方认证报文: " + auth + "\n";
		// TODO Auto-generated method stub
		String[] auth_a = auth.split(" ");
		
		
		if (auth_a.length < 3) {
			res[0] = "false";
			return res;
		}
		
		BigInteger time_c = new BigInteger(auth_a[0]);
		BigInteger usr_n = new BigInteger(auth_a[1]);
		BigInteger usr_id_pub = new BigInteger(auth_a[2]);
		
		res[1] += "对方证书: " + auth_a[1] + " " + auth_a[2] + "\n\n";
		
		byte[] usr_id_pub_b = new RSA(1024).endecry(usr_id_pub, ASKey.AS_pub, ASKey.AS_n).toByteArray();
		
		short clientID_au = (short) ((((int)usr_id_pub_b[0]) & 0x00FF)
				+ ((((int) usr_id_pub_b[1]) << 8) & 0xFF00));
		Log.println("clientID_au: " + clientID_au);
		
		res[1] += "对方发送ID: " + clientID + "\n";
		res[1] += "解密证书后对方ID: " + clientID_au + "\n";
		
		// 证书身份检测
		if (clientID_au != clientID) {
			res[1] += "身份检测失败！\n";
			res[0] = "false";
			return res;
		}
		
		res[1] += "身份检测成功！\n\n";
		
		byte[] usr_pub_b = new byte[ usr_id_pub_b.length - 2 ];
		System.arraycopy(usr_id_pub_b, 2, usr_pub_b, 0, usr_pub_b.length);
		
		BigInteger usr_pub = new BigInteger(usr_pub_b);
		Log.println("usr_pub: " + usr_pub);
		
		res[1] += "对方公钥: " + usr_pub + "\n";
		
		int time = new RSA(1024).endecry(time_c, usr_pub, usr_n).intValue();
		// int time = new RSA(1024).endecry(time_c, ASKey.AS_pub, ASKey.AS_n).intValue();
		Log.println("usr_time: " + time);
		
		res[1] += "解密前时间戳: " + time_c + "\n";
		res[1] += "解密后戳: " + time + "\n";
		
		// 检测认证是否为重放攻击（时间解密检测）
		if(Time.isIntime(time, 30)) {
			res[1] += "时间检测成功！\n";
			return res;
		}
		else {
			res[1] += "时间检测失败！\n";
			res[0] = "false";
			return res;
		}
		
		
	}
	
	public static BigInteger getFileData(String auth) {
		
		String[] auth_a = auth.split(" ");
		
		if (auth_a.length < 4)
			return null;
		
		BigInteger usr_n = new BigInteger(auth_a[1]);
		BigInteger usr_id_pub = new BigInteger(auth_a[2]);
		BigInteger fileHash = new BigInteger(auth_a[4]);
		Log.println("fileHash: " + fileHash);
		
		byte[] usr_id_pub_b = new RSA(1024).endecry(usr_id_pub, ASKey.AS_pub, ASKey.AS_n).toByteArray();
		byte[] usr_pub_b = new byte[ usr_id_pub_b.length - 2 ];
		System.arraycopy(usr_id_pub_b, 2, usr_pub_b, 0, usr_pub_b.length);
		
		BigInteger usr_pub = new BigInteger(usr_pub_b);
		Log.println("usr_pub: " + usr_pub);
		
		// int time = new RSA(1024).endecry(time_c, ASKey.AS_pub, ASKey.AS_n).intValue();
		
		return new RSA(1024).endecry(fileHash, usr_pub, usr_n);	
	}
	

	public static String getFileHashLog(String auth) {
		
		String res = Time.getCurrentTime() + "\n";
		res += "解密前文件Hash值: " + auth.split(" ")[4] + "\n";
		BigInteger fileHash = Auth.getFileData(auth);
		res += "解密后文件Hash值: " + fileHash + "\n";
		
		return res;
		
	}

}
