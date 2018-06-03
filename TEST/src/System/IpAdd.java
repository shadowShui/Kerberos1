package System;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpAdd  
{  
	/**
	 * 判断IP地址是否合法
	 * @param addr
	 * @return
	 */
    public static boolean isIP(String addr)  
    {  

		boolean flag = false;
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher m = pattern.matcher(addr);
		flag = m.matches();
		return flag;
		
    }  
}  
