package Crypt;
import java.io.File;
/**
 * ����ȥ���ײ�
 */
import java.util.Arrays;

import System.Log;

public class ArrayCopy {
	
	public static void main(String[] args) {
//		String[] str = "a,b,c,d,e,f,g,h,j,k".split(",");
//		String[] ss = new String[str.length-1];
//		System.arraycopy(str, 1, ss, 0, ss.length);
//		System.out.println(Arrays.toString(ss));
		
		
		File filepath = new File("D:\\��������ȫ�γ����\\filecache\\12345");
		
		String[] fileList = filepath.list();
		
		Log.println(fileList[0] + fileList[1]);
	}
}
