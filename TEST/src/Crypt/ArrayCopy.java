package Crypt;
import java.io.File;
/**
 * 报文去掉首部
 */
import java.util.Arrays;

import System.Log;

public class ArrayCopy {
	
	public static void main(String[] args) {
//		String[] str = "a,b,c,d,e,f,g,h,j,k".split(",");
//		String[] ss = new String[str.length-1];
//		System.arraycopy(str, 1, ss, 0, ss.length);
//		System.out.println(Arrays.toString(ss));
		
		
		File filepath = new File("D:\\物联网安全课程设计\\filecache\\12345");
		
		String[] fileList = filepath.list();
		
		Log.println(fileList[0] + fileList[1]);
	}
}
