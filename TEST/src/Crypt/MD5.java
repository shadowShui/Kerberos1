package Crypt;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.logging.Logger;

/**
 * MD5�����㷨
 */
public class MD5 {

    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // ���MD5ժҪ�㷨�� MessageDigest ����
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // ʹ��ָ�����ֽڸ���ժҪ
            mdInst.update(btInput);
            // �������
            byte[] md = mdInst.digest();
            // ������ת����ʮ�����Ƶ��ַ�����ʽ
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static byte[] MD5bytes_64(String key) {
    	
    	byte[] res = new byte[8];
    	String MD5_String = MD5(key);
    	BigInteger a = new BigInteger(MD5_String, 16);
    	byte[] res_all = a.toByteArray();
    	res[7] = 0;
    	System.arraycopy(res_all, 0, res, 0, res.length - 1);
    	return res;
    }

    public static void main(String[] args){
    	
    	String str = "000AB";
        Integer in = Integer.valueOf(str,16);
        System.out.println(in);

        System.out.println(MD5bytes_64("111").length);
    }
}