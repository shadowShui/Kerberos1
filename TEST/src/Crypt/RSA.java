package Crypt;
/**
 * RSA�����㷨ʵ��
 * 
 * 
 * @author fangyunniu
 *
 */

import java.math.BigInteger;
import java.util.Random;

import File.FileHash;
import System.Log;

public class RSA {
	

	private final int LENGTH_OF_PQ;
	
	public RSA(int LENGTH_OF_PQ) {
		this.LENGTH_OF_PQ = LENGTH_OF_PQ;
	}
	
	/**
	 * ����Կ�׷���
	 * 
	 * @return ����Կ��
	 */
	public BigInteger[] createKey(int choose) {
		
		// �����������������
		BigInteger p = BigInteger.probablePrime(LENGTH_OF_PQ, new Random());
		BigInteger q = BigInteger.probablePrime(LENGTH_OF_PQ, new Random());
		
		return createKey(p, q, choose);
	}
	
	/**
	 * ���ܽ��ܷ���
	 * @param text ���������ַ���
	 * @param DE ��Կ
	 * @param N ����
	 * @return ���ܺ�������ַ���
	 */
	public String endecry(String text, int n, BigInteger DE, BigInteger N) {
		
		BigInteger textInt = new BigInteger(text, n);
		// ���ȳ������׳��쳣
		if (textInt.bitLength() > 2*LENGTH_OF_PQ )
			try {
				throw new Exception("the length of text exceeds the limit");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return endecry(textInt, DE, N).toString(n);
	}
	
	public BigInteger endecry(BigInteger text, BigInteger DE, BigInteger N) {
		
		return text.modPow(DE, N);
	}
	
	private BigInteger[] createKey(BigInteger p, BigInteger q, int choose) {
		
		BigInteger[] res = new BigInteger[3];
		// ���� n = p*q
		res[0] = p.multiply(q);
		// ���� p, q ����С������  L
		BigInteger L =  lcm(p.subtract(BigInteger.valueOf(1))
				, q.subtract(BigInteger.valueOf(1)));
		// �����û�ѡ���������
		res[1] = (choose == 0) ? createE(L)
				: BigInteger.valueOf(2).pow(16).add(BigInteger.valueOf(1));
		res[2] = createD(res[1], L);
		return res;
	}
	
	/**
	 * ���ɲ��� D
	 * @param L �������Ʒ�Χ L
	 * @return E����   ����2��������1 < D < L��E*D mod L �� 1 
	 */
	private BigInteger createD(BigInteger E, BigInteger L) {
		
		BigInteger d, x, y, res[];
		d = BigInteger.valueOf(-1);
		x = BigInteger.valueOf(0);
		y = BigInteger.valueOf(0);
		res = extGcd(E, L, d, x, y);
		d = res[0];
		x = res[1];
		y = res[2];

		return d.equals(BigInteger.valueOf(1)) ? x.add(L).mod(L) : null;  
	}
	
	
	/**
	 * ��չŷ����÷���
	 * 
	 * e*x + l*y = gcd(e, l) == 1
	 *
	 * @param e
	 * @param l
	 * @param d
	 * @param x
	 * @param y
	 * @return ������õ�d, x, y
	 */
	private BigInteger[] extGcd(BigInteger e, BigInteger l, BigInteger d, BigInteger x, BigInteger y) {
		
		BigInteger[] res = new BigInteger[3];	
		// TODO Auto-generated method stub
		if (l.equals(BigInteger.valueOf(0))) { 
			
			res[0] = e;
			res[1] = BigInteger.valueOf(1);
			res[2] = BigInteger.valueOf(0);
		}  
	    else { 
	    	res = extGcd(l, e.mod(l), d, y, x); 
	    	d = res[0]; y = res[1]; x = res[2];
	    	y = y.subtract(x.multiply(e.divide(l)));
	    	
	    	res[0] = d; res[1] = x; res[2] = y;
	    }  
		return res;
	}

	/**
	 * ���ɲ��� E
	 * @param L �������Ʒ�Χ L
	 * @return ��LС��һ�����ʵ���
	 */
	private BigInteger createE(BigInteger L) {
		
		BigInteger tmp;
		while (true) {
			
			tmp = new BigInteger(L.bitLength(), new Random());
			if (L.compareTo(tmp) >= 0 
					&& gcd(L, tmp).equals(BigInteger.valueOf(1)))
				return tmp;
		}
	}
	
	/**
	 * �������������
	 * @param m
	 * @param n
	 * @return ��ý��
	 */
	private BigInteger gcd(BigInteger m, BigInteger n) {  
		
//	    while (true) {    
//	        if(n.equals(BigInteger.valueOf(0)))    
//	            return m;    
//	        BigInteger temp = m.mod(n);    
//	        m = n;    
//	        n = temp;    
//	    }    
		return m.gcd(n);
	}    
	
	
	/**
	 * ����С����������
	 * @param a
	 * @param b
	 * @return ��ý��
	 */
	private BigInteger lcm(BigInteger a, BigInteger b) {
		
		return a.divide(gcd(a, b)).multiply(b);
	    // return a / gcd(a, b) * b;    
	}
	
	
	    
 

	public static void main(String[] args) {
		
//		int E = 5, N = 323, D = 29;
//		
		RSA rsa = new RSA(1024);
		BigInteger[] key = rsa.createKey(0);
		// NΪ���ȣ� EΪ��Կ�� DΪ˽Կ
		BigInteger N = key[0], E = key[1], D = key[2];
		
		Log.println(E.bitLength());
		Log.println(D.bitLength());
		
		System.out.println("********��Կ�������********");
		System.out.println("N: " + N);
		System.out.println("E: " + E);
		System.out.println("D: " + D);
		System.out.println("***************************");
//		BigInteger plain = new BigInteger("123451445645648797854556456456456");
//		BigInteger chiper = plain.modPow(E, N);
//		System.out.println(chiper);
//		BigInteger dechiper = chiper.modPow(D, N);
//		System.out.println(dechiper);
//		
		//String plain = "123451445645648797854556456456456";
		String plain = "ff4dca38faa13daebf57995b36776941";
		System.out.println("ԭʼ���ģ�" + plain);
		String chiper = rsa.endecry(plain, 16, E, N);
		System.out.print("���ܽ����");
		System.out.println(chiper);
		String dechiper = rsa.endecry(chiper, 16, D, N);
		System.out.print("���ܽ����");
		System.out.println(dechiper);
		
		//createKey();
		//createKey(new BigInteger("17"), new BigInteger("19"));
		
//		BigInteger a = new BigInteger("225");
//		BigInteger b = new BigInteger("29");
//		BigInteger c = new BigInteger("323");
		
//		BigInteger E = new BigInteger("5");
//		BigInteger L = new BigInteger("144");
//		
//		BigInteger D = createD(E, L);
//		
//		System.out.println(D);
		
//		System.out.println(a.modPow(b, c));
		
		
	}
}
