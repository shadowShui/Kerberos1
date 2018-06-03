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
		res[1] += "�Է���֤����: " + auth + "\n";
		// TODO Auto-generated method stub
		String[] auth_a = auth.split(" ");
		
		
		if (auth_a.length < 3) {
			res[0] = "false";
			return res;
		}
		
		BigInteger time_c = new BigInteger(auth_a[0]);
		BigInteger usr_n = new BigInteger(auth_a[1]);
		BigInteger usr_id_pub = new BigInteger(auth_a[2]);
		
		res[1] += "�Է�֤��: " + auth_a[1] + " " + auth_a[2] + "\n\n";
		
		byte[] usr_id_pub_b = new RSA(1024).endecry(usr_id_pub, ASKey.AS_pub, ASKey.AS_n).toByteArray();
		
		short clientID_au = (short) ((((int)usr_id_pub_b[0]) & 0x00FF)
				+ ((((int) usr_id_pub_b[1]) << 8) & 0xFF00));
		Log.println("clientID_au: " + clientID_au);
		
		res[1] += "�Է�����ID: " + clientID + "\n";
		res[1] += "����֤���Է�ID: " + clientID_au + "\n";
		
		// ֤����ݼ��
		if (clientID_au != clientID) {
			res[1] += "��ݼ��ʧ�ܣ�\n";
			res[0] = "false";
			return res;
		}
		
		res[1] += "��ݼ��ɹ���\n\n";
		
		byte[] usr_pub_b = new byte[ usr_id_pub_b.length - 2 ];
		System.arraycopy(usr_id_pub_b, 2, usr_pub_b, 0, usr_pub_b.length);
		
		BigInteger usr_pub = new BigInteger(usr_pub_b);
		Log.println("usr_pub: " + usr_pub);
		
		res[1] += "�Է���Կ: " + usr_pub + "\n";
		
		int time = new RSA(1024).endecry(time_c, usr_pub, usr_n).intValue();
		// int time = new RSA(1024).endecry(time_c, ASKey.AS_pub, ASKey.AS_n).intValue();
		Log.println("usr_time: " + time);
		
		res[1] += "����ǰʱ���: " + time_c + "\n";
		res[1] += "���ܺ��: " + time + "\n";
		
		// �����֤�Ƿ�Ϊ�طŹ�����ʱ����ܼ�⣩
		if(Time.isIntime(time, 30)) {
			res[1] += "ʱ����ɹ���\n";
			return res;
		}
		else {
			res[1] += "ʱ����ʧ�ܣ�\n";
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
		res += "����ǰ�ļ�Hashֵ: " + auth.split(" ")[4] + "\n";
		BigInteger fileHash = Auth.getFileData(auth);
		res += "���ܺ��ļ�Hashֵ: " + fileHash + "\n";
		
		return res;
		
	}

}
