package Crypt;
/**
 * XXX类
 * 
 * 添加功能说明
 * 
 * Time: 2018-04-29
 * @author Frank
 */


import java.io.UnsupportedEncodingException;

public class DES {
	
	/**       初始置换                   **/
	private final static int[] IP = 
		{ 58, 50, 42, 34, 26, 18, 10, 2,
		  60, 52, 44, 36, 28, 20, 12, 4,
		  62, 54, 46, 38, 30, 22, 14, 6,
		  64, 56, 48, 40, 32, 24, 16, 8,
		  57, 49, 41, 33, 25, 17,  9, 1,
		  59, 51, 43, 35, 27, 19, 11, 3,
		  61, 53, 45, 37, 29, 21, 13, 5,
		  63, 55, 47, 39, 31, 23, 15, 7 };
	
	/**       逆初始置换                   **/
	private final static int[] IP_1 =
		{ 40, 8, 48, 16, 56, 24, 64, 32,
		  39, 7, 47, 15, 55, 23, 63, 31,
		  38, 6, 46, 14, 54, 22, 62, 30,
		  37, 5, 45, 13, 53, 21, 61, 29,
		  36, 4, 44, 12, 52, 20, 60, 28,
		  35, 3, 43, 11, 51, 19, 59, 27,
		  34, 2, 42, 10, 50, 18, 58, 26,
		  33, 1, 41,  9, 49, 17, 57, 25 };
	
	/**       左移循环次数规定            **/
	private final static int[] BITS_ROTATED = 
		{ 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };
			
			
	/**    56bit置换选择PC_1      **/
	private final static int[] PC_1 = 
		 { 57, 49, 41, 33, 25, 17,  9,
		    1, 58, 50, 42, 34, 26, 18,
		   10,  2, 59, 51, 43, 35, 27, 
		   19, 11,  3, 60, 52, 44, 36, 
		   63, 55, 47, 39, 31, 23, 15, 
		    7, 62, 54, 46, 38, 30, 22, 
		   14,  6, 61, 53, 45, 37, 29, 
		   21, 13,  5, 28, 20, 12,  4 };
	
	/**    48bit置换选择PC_2      **/
	private final static int[] PC_2 = 
		{ 14, 17, 11, 24, 1,  5,  3,  28,
		  15,  6, 21, 10, 23, 19, 12,  4,
		  26,  8, 16,  7, 27, 20, 13,  2,
		  41, 52, 31, 37, 47, 55, 30, 40,
		  51, 45, 33, 48, 44, 49, 39, 56,
		  34, 53, 46, 42, 50, 36, 29, 32 }; 
	
	/**    轮函数扩充表E      **/
	private final static int[] E = 
		{ 32,  1,  2,  3,  4,  5,
		   4,  5,  6,  7,  8,  9,
		   8,  9, 10, 11, 12, 13,
		  12, 13, 14, 15, 16, 17,
		  16, 17, 18, 19, 20, 21,
		  20, 21, 22, 23, 24, 25,
		  24, 25, 26, 27, 28, 29,
		  28, 29, 30, 31, 32,  1 };
	
	/**    轮函数置换表P      **/
	private final static int[] P = 
		{ 16,  7, 20, 21, 29, 12, 28, 17,
		   1, 15, 23, 26,  5, 18, 31, 10,
		   2,  8, 24, 14, 32, 27,  3,  9,
		  19, 13, 30,  6, 22, 11,  4, 25 };
	
	/**        8个s盒                     **/
	private static final int[][][] S_BOX = {//S-盒
	    {
	     { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
	     { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
	     { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
	     { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } },
	    {
	     { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
	     { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
	     { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
	     { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } },
	    {
	     { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
	     { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
	     { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
	     { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } },
	    {
	     { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
	     { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
	     { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
	     { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } },
	    {
	     { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
	     { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
	     { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
	     { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } },
	    {
	     { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
	     { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
	     { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
	     { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } },
	    {
	     { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
	     { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
	     { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
	     { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } },
	    {
	     { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
	     { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
	     { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
	     { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } } 
	 };
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		DES des = new DES();
		
		// String s = "哇哈哈哈哈，我是一个大好人！";
		// String s = "健康是福。有了健康的身体才有奋斗成功的本钱。要有健康身体，除了要注重饮食与运动外，还要正常的生活习惯。现在有很多朋友，日夜都颠倒过来了，这样哪会健康。另外，心理的健康也很重要。心理状况不好，也会影响健康，要及时调整。";
		String s = "关于String的getBytes()，getBytes(encoding)和new S"
				+ "tring(bytes, encoding)这三个方法，非常值得注意：A.get"
				+ "Bytes()：使用平台默认的编码方式(通过file.encoding属性获"
				+ "取)方式来将字符串转换成byte[]。得到的是字符串最原始的字节编码值。";
		long key = 0b1001100101100010101101010110101011010101111011110010000L;
//		System.out.println("原始明文为：");
//		System.out.println(s);
//		String res = des.encryptString(s, key);
//		System.out.println("加密后结果为：");
//		System.out.println(res);
//		String de = des.decryptString(res, key);
//		System.out.println("解密后结果为：");
//		System.out.println(de);
		
		byte[] res = des.encryptByte(s.getBytes(), key);
		byte[] rres = des.decryptByte(res, key);
		
		System.out.println(new String(res));
		System.out.println(new String(rres));
	}
	
	/**
	 * 将字符串处理为数字流，为64的倍数
	 * @param plaintext 密文字符串
	 * @return 数字流
	 */
	private long[] StingTo64bit(final String plaintext, String code) {
		long[] res;
		byte[] res_bytes = null;
		try {
			res_bytes = plaintext.getBytes(code);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int length_bytes = res_bytes.length;
		int last = length_bytes % 8;
		// int length_long = (last == 0) ? length_bytes/8 : length_bytes/8 + 1;
		
		// 对不足64位的进行填充
		if (last != 0) {
			byte[] tmp = new byte[ length_bytes - last + 8 ];
			for (int i = 0; i < length_bytes; i ++) {
				tmp[i] = res_bytes[i];
			}
			// 多余的填充为" "
			for (int i = length_bytes; i < tmp.length; i ++)
				tmp[i] = 0b100000;
			res_bytes = tmp;
		}
		
		res = byteToLong(res_bytes);
		return res;
	}
	
	/**
	 * byte数组转换为Long型数组
	 * @param data
	 * @return
	 */
	public long[] byteToLong(final byte[] data) {
		
		// 强制为8的倍数
		if (data.length%8 != 0) {
			try {
				throw new Exception("the length of data must be a multiple of 8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		int length_long = data.length/8;
		long[] res = new long[length_long];
		for (int i = 0; i < length_long; i ++) {
			res[i] = ((long)data[ i*8 + 7 ] << 56 & 0xFF00000000000000L)
				   + ((long)data[ i*8 + 6 ] << 48 & 0xFF000000000000L)
				   + ((long)data[ i*8 + 5 ] << 40 & 0xFF0000000000L)
				   + ((long)data[ i*8 + 4 ] << 32 & 0xFF00000000L)
				   + ((long)data[ i*8 + 3 ] << 24 & 0xFF000000L)
				   + ((long)data[ i*8 + 2 ] << 16 & 0xFF0000L)
				   + ((long)data[ i*8 + 1 ] << 8 & 0xFF00L)
				   + ((long)data[ i*8 ] & 0xFFL);
		}
		return res;
	}
	
	/**
	 * 将数字流转换为字符串
	 * @param b
	 * @return
	 */
	private String _64bitToString(long[] b, String code) {
		
		byte[] res;
		String res_s = null;
		res = longToByte(b);
		// TODO Auto-generated method stub
		try {
			res_s = new String(res, code);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res_s;
	}
	
	/**
	 * long型数组转换为Byte数组
	 * @param data
	 * @return
	 */
	public byte[] longToByte(final long[] data) {
		
		int res_length = data.length * 8;
		byte[] res = new byte[res_length];
		
		for (int i = 0; i < data.length; i ++) {
			res[ i*8 + 7 ] = (byte) ((data[i] >> 56) & 0xFF) ; 
			res[ i*8 + 6 ] = (byte) ((data[i] >> 48) & 0xFF);
			res[ i*8 + 5 ] = (byte) ((data[i] >> 40) & 0xFF);
			res[ i*8 + 4 ] = (byte) ((data[i] >> 32) & 0xFF);
			res[ i*8 + 3 ] = (byte) ((data[i] >> 24) & 0xFF);
			res[ i*8 + 2 ] = (byte) ((data[i] >> 16) & 0xFF);
			res[ i*8 + 1 ] = (byte) ((data[i] >> 8) & 0xFF);
			res[ i*8 ] = (byte) (data[i] & 0xFF);
		}
		
		return res;
	}
	
	/**
	 * 奇校验方法
	 * @param x 
	 * @return 校验结果
	 */
	private int parity_check(final long x) {
		
		long t = x;
		int a = 0;
		while (t > 0) {
			a ++;
			t &= (t-1);
		}
		return a % 2;
	}
	
	/**
	 * 循环左移方法
	 * @param tmp 数
	 * @param length 长度
	 * @param cnt 移位个数
	 * @return
	 */
	private long roundLeft(final long tmp, final long cnt, final long length){
		
		long res = (tmp << cnt | tmp >>> (length-cnt))
				& ((1<<length) - 1);
	    return res;
	}
	
	/**
	 * 置换方法
	 * @param tmp 
	 * @param length 长度
	 * @param table 置换表
	 * @return
	 */
	private long replace(final long tmp, final int length, final int[] table) {
		long res = 0;
		for (int i = 0; i < table.length; i ++) {
			res += ((tmp >> (length - table[i])) & 1) << (table.length - 1 - i); 
		}
		return res;		
	}
	
	
	/**
	 * 生成16个子钥匙
	 * @param key 原始秘钥
	 * @return 子钥匙数组
	 */
	private long[] createKey(final long key) {
		// TODO Auto-generated method stub
		long[] res;
		long key_64 = 0;
		long key_pc = 0;
		long c = 0, d = 0;
		long tmp;
		// 转换为64位
		for (int i = 0; i < 8; i ++) {
			tmp = (key >> 7*i) & 0b1111111;
			tmp = (tmp << 1) + parity_check(tmp);
			key_64 += tmp << (8*i);
		}
		
		// 进行PC-1置换选择
		key_pc = replace(key_64, 64, PC_1);
		
		c = key_pc >>> 28;
		d = key_pc & 0xFFFFFFF;
		
		res = new long[16];
		
		// 进行16轮生成
		for (int i = 0; i < 16; i ++) {
			c = roundLeft(c, BITS_ROTATED[i], 28);
			d = roundLeft(d, BITS_ROTATED[i], 28);
			tmp = (c << 28) + d;
			
			// 进行PC-2置换选择
			res[i] = replace(tmp, 56, PC_2);
		}
		
		return res;
	}
	
	/**
	 * 片段DES加密
	 * @param fragment 加密片段
	 * @return 加密后片段
	 */
	private long fragmentEncrypt(final long fragment, final long[] child_key) {
		
		long res = replace(fragment, 64, IP);
		// 初始置换
		for (int i = 0; i < 16; i ++) {
			res = singleRoundEncrypt(res, child_key[i]);
		}	
		// TODO Auto-generated method stub
		// 逆初始置换
		res = replace(res, 64, IP_1);
		return res;
	}
	
	/**
	 * 片段DES解密
	 * @param fragment
	 * @param child_key
	 * @return
	 */
	private long fragmentDecrypt(final long fragment, final long[] child_key) {
		// TODO Auto-generated method stub
		//System.out.println(Long.toBinaryString(fragment));
		long res = replace(fragment, 64, IP);
		//System.out.println(Long.toBinaryString(res));
		// 初始置换
		for (int i = 0; i < 16; i ++) {
			res = singleRoundDecrypt(res, child_key[ 15 - i ]);
		}
		// TODO Auto-generated method stub
		// 逆初始置换
		return replace(res, 64, IP_1);
	}
	

	/**
	 * 单轮DES解密
	 * @param left
	 * @param right
	 * @return
	 */
	private long singleRoundDecrypt(final long fragment, final long childKey) {
		
		// TODO Auto-generated method stub
		long res = 0;
		int left, right, tmp;
		left = (int) (fragment >> 32);
		right = (int) fragment;
		tmp = left;
		
		left = (right ^ func_F(left, childKey));
		right = tmp;
		
		res = ((long)left << 32) | ((long)right & 0xFFFFFFFFL);
//		System.out.println(Integer.toBinaryString(right));
		return res;
	}

	/**
	 * 单轮DES加密
	 * @param left
	 * @param right
	 * @return
	 */
	private long singleRoundEncrypt(final long fragment, final long childKey) {
		// TODO Auto-generated method stub
		long res = 0;
		int left, right, tmp;
		left = (int) (fragment >> 32);
		right = (int) fragment;
		tmp = right;
		
		right = func_F(right, childKey);	// 右半部分轮函数输出
		right ^= left;						// 与左半部分进行异或
		left = tmp;
		
		res = ((long)left << 32) | ((long)right & 0xFFFFFFFFL);
		return res;
	}


	/**
	 * 轮函数
	 * @param right
	 * @return
	 */
	private int func_F(final int right, final long key) {
		// TODO Auto-generated method stub
		int res = 0;
		long right_e = replace(right, 32, E);
		long right_k = key^right_e;
		int tmp;
		for (int i = 0; i < 8; i ++) {
			
			tmp = (int) (right_k >> ((7-i) * 6) & 0b111111);
			tmp = getS_BOX(tmp, i);
			res += tmp << ((7-i) * 4); 
		}
		res = (int) replace(res, 32, P);
		return res;
	}

	/**
	 * 从S盒中进行取值
	 * @param tmp
	 * @return
	 */
	private int getS_BOX(int tmp, int i) {
		
		int row_1 = (tmp >> 5) & 1;
		int row_2 = tmp & 1;
		int row = ((row_1) << 1) + row_2;
		int column = tmp >> 1 & 0b1111;
		// TODO Auto-generated method stub
		return S_BOX[i][row][column];

	}

	/**
	 * DES加密方法
	 * @param plaintext 输入明文对象
	 * @param key 输入秘钥，不超过56位
	 * @return 返回加密后的对象
	 */
	public String encryptString(final String plaintext, final long key) {
		
		String res = null;		
		long[] b = StingTo64bit(plaintext, "GBK");
		
		b = encryptLong(b, key);
		
		res = _64bitToString(b, "ISO-8859-1");
		return res;
	}
	


	
	/**
	 * 解密方法
	 * @param ciphertext 输入密文
	 * @param key 秘钥
	 * @return 解密结果
	 */
	public String decryptString(final String ciphertext, long key ) {
		
		String res = null;		
		long[] b = StingTo64bit(ciphertext, "ISO-8859-1");
		
		b = decryptLong(b, key);
		
		res = _64bitToString(b, "GBK");
		return res;
	}
	
	/**
	 * 加密long型数组
	 * @param data
	 * @param key
	 * @return
	 */
	public long[] encryptLong(final long[] data, long key) {

		long[] res = new long[data.length];

		// 判断秘钥是否超过56位
		if (key > ((long)1<<56) - 1) {
			try {
				throw new Exception("the length of key exceeds the limit");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long[] child_key = createKey(key);
		// 进行逐轮加密
		for (int i = 0; i < data.length; i ++) {
			res[i] = fragmentEncrypt(data[i], child_key);
		}
		
		return res;
	}
	
	/**
	 * 解密long型数组
	 * @param data
	 * @param key
	 * @return
	 */
	public long[] decryptLong(final long[] data, long key) {
		
		long[] res = new long[data.length];
		
		// 判断秘钥是否超过56位
		if (key > ((long)1<<56) - 1) {
			try {
				throw new Exception("the length of key exceeds the limit");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long[] child_key = createKey(key);
		// 进行逐轮解密
		for (int i = 0; i < data.length; i ++) {
			res[i] = fragmentDecrypt(data[i], child_key);
		}
		
		return res;
	}
	
	/**
	 * 加密Byte型数组
	 * @param data
	 * @param key
	 * @return
	 */
	public byte[] encryptByte(final byte[] data, long key) {
		
		if (data.length%8 != 0) {
			System.out.println("注意，数据大小不是8的倍数！");
		}
		
		byte[] data_8 = new byte[ data.length - data.length%8 ];
		System.arraycopy(data, 0, data_8, 0, data_8.length);
		
		long[] data_long = byteToLong(data_8);
		data_long = encryptLong(data_long, key);
		
		byte[] res_8 = longToByte(data_long);
		byte[] res = new byte[data.length];
		System.arraycopy(res_8, 0, res, 0, res_8.length);
		System.arraycopy(data, res_8.length, res, res_8.length, res.length - res_8.length);
		
		return res;
	}
	
	/**
	 * 解密Byte型数组
	 * @param data
	 * @param key
	 * @return
	 */
	public byte[] decryptByte(final byte[] data, long key) {
		
		if (data.length%8 != 0) {
			System.out.println("注意，数据大小不是8的倍数！");
		}
		
		byte[] data_8 = new byte[ data.length - data.length%8 ];
		System.arraycopy(data, 0, data_8, 0, data_8.length);
		
		long[] data_long = byteToLong(data_8);
		data_long = decryptLong(data_long, key);
		
		byte[] res_8 = longToByte(data_long);
		byte[] res = new byte[data.length];
		System.arraycopy(res_8, 0, res, 0, res_8.length);
		System.arraycopy(data, res_8.length, res, res_8.length, res.length - res_8.length);
		
		return res;
	}
}