package File;
  

import java.io.FileInputStream;    
import java.io.FileNotFoundException;    
import java.io.InputStream;    
import java.math.BigInteger;    
import java.security.MessageDigest;    
    
public class FileHash {    
        
    public static void main(String[] args) {    
        try {    
            //�˴��Ҳ��Ե����ұ���jdkԴ���ļ���MD5ֵ   
            String filePath = "C:\\Program Files\\Java\\jdk1.7.0_45\\src.zip";  
              
            String md5Hashcode = md5HashCode(filePath);  
            String md5Hashcode32 = md5HashCode32(filePath);    
              
            System.out.println(md5Hashcode + "���ļ���md5ֵ");    
            System.out.println(md5Hashcode32+"���ļ�32λ��md5ֵ");   
              
            //System.out.println(-100 & 0xff);  
        } catch (FileNotFoundException e) {    
            e.printStackTrace();    
        }    
    }    
      
    /** 
     * ��ȡ�ļ���md5ֵ ���п��ܲ���32λ 
     * @param filePath  �ļ�·�� 
     * @return 
     * @throws FileNotFoundException 
     */  
    public static String md5HashCode(String filePath) throws FileNotFoundException{    
        FileInputStream fis = new FileInputStream(filePath);    
        return md5HashCode(fis);    
    }    
      
    /** 
     * ��֤�ļ���MD5ֵΪ32λ 
     * @param filePath  �ļ�·�� 
     * @return 
     * @throws FileNotFoundException 
     */  
    public static String md5HashCode32(String filePath) throws FileNotFoundException{    
        FileInputStream fis = new FileInputStream(filePath);    
        return md5HashCode32(fis);    
    }    
      
    /** 
     * java��ȡ�ļ���md5ֵ   
     * @param fis ������ 
     * @return 
     */  
    public static String md5HashCode(InputStream fis) {    
        try {    
            //�õ�һ��MD5ת����,�����ʹ��SHA-1��SHA-256������SHA-1,SHA-256    
            MessageDigest md = MessageDigest.getInstance("MD5");   
              
            //�ֶ�ν�һ���ļ����룬���ڴ����ļ����ԣ��Ƚ��Ƽ����ַ�ʽ��ռ���ڴ�Ƚ��١�  
            byte[] buffer = new byte[1024];    
            int length = -1;    
            while ((length = fis.read(buffer, 0, 1024)) != -1) {    
                md.update(buffer, 0, length);    
            }    
            fis.close();  
            //ת�������ذ���16��Ԫ���ֽ�����,������ֵ��ΧΪ-128��127  
            byte[] md5Bytes  = md.digest();  
            BigInteger bigInt = new BigInteger(1, md5Bytes);//1��������ֵ   
            return bigInt.toString(16);//ת��Ϊ16����  
        } catch (Exception e) {    
            e.printStackTrace();    
            return "";    
        }    
    }    
      
    /** 
     * java�����ļ�32λmd5ֵ 
     * @param fis ������ 
     * @return 
     */  
    public static String md5HashCode32(InputStream fis) {  
        try {  
            //�õ�һ��MD5ת����,�����ʹ��SHA-1��SHA-256������SHA-1,SHA-256    
            MessageDigest md = MessageDigest.getInstance("MD5");  
              
            //�ֶ�ν�һ���ļ����룬���ڴ����ļ����ԣ��Ƚ��Ƽ����ַ�ʽ��ռ���ڴ�Ƚ��١�  
            byte[] buffer = new byte[1024];  
            int length = -1;  
            while ((length = fis.read(buffer, 0, 1024)) != -1) {  
                md.update(buffer, 0, length);  
            }  
            fis.close();  
              
            //ת�������ذ���16��Ԫ���ֽ�����,������ֵ��ΧΪ-128��127  
            byte[] md5Bytes  = md.digest();  
            StringBuffer hexValue = new StringBuffer();  
            for (int i = 0; i < md5Bytes.length; i++) {  
                int val = ((int) md5Bytes[i]) & 0xff;//���Ͳμ����·�  
                if (val < 16) {  
                    /** 
                     * ���С��16����ôvalֵ��16������ʽ��ȻΪһλ�� 
                     * ��Ϊʮ����0,1...9,10,11,12,13,14,15 ��Ӧ�� 16����Ϊ 0,1...9,a,b,c,d,e,f; 
                     * �˴���λ��0�� 
                     */  
                    hexValue.append("0");  
                }  
                //���������Integer��ķ���ʵ��16���Ƶ�ת��   
                hexValue.append(Integer.toHexString(val));  
            }  
            return hexValue.toString();  
        } catch (Exception e) {  
            e.printStackTrace();  
            return "";  
        }  
    }  
      
    /** 
     * ����md5HashCode32 ��     ((int) md5Bytes[i]) & 0xff   �����Ľ��ͣ� 
     * ��Java�������漰���ֽ�byte�������ݵ�һЩ����ʱ���������� byte[i] & 0xff�����Ĳ���������ͼ�¼�ܽ�һ��������������壺  
     * 1��0xff��16���ƣ�ʮ������255������Ĭ��Ϊ���Σ�������λΪ32λ����Ͱ�λ�ǡ�1111 1111��������24λ����0��  
     * 2��&����: ���2��bit����1�����1�������0��  
     * 3��byte[i] & 0xff�����ȣ��������һ�㶼���ڽ�byte����ת��int���������������ݵĹ����У�ʹ����������������յ���������ֻ�е�8λ�����ݣ�����λ����Ϊ0��  
     * 4����������ó����������ݶ��Ǵ��ڵ���0����С�ڵ���255�� 
     */  
    
}   