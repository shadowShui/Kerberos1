package System;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Time {
	
	public static void main(String[] args) {
		
		Date dd = new Date();
		int tmp = getSecondTimestamp(dd);
		Log.println(tmp);
		Log.printBinaryln(tmp);

		String name = "01001";
		Log.println(name.getBytes().length);
	}

	/** 
	 * ��ȡ��ȷ�����ʱ��� 
	 * @return 
	 */  
	public static int getSecondTimestamp(Date date) {  
		
	    if (null == date) {  
	        return 0;  
	    }  
	    String timestamp = String.valueOf(date.getTime());  
	    int length = timestamp.length();  
	    if (length > 3) {  
	        return Integer.valueOf(timestamp.substring(0,length-3));  
	    } else {  
	        return 0;  
	    }  
	}  
	
	/**
	 * �ж�Ʊ���Ƿ����
	 */
	public static boolean isIntime(int time, int lifetime) {
		
		int MAX = 100;
		int cnt_time = Time.getSecondTimestamp(new Date());
		if (Math.abs(time - cnt_time) > MAX)
			return false;
		else if (time+lifetime <= cnt_time)
			return false;
		else
			return true;
	}
	/**
	 * ��ȡ��ǰʱ��
	 */
	public static String getCurrentTime(){
		Date day=new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String time = String.valueOf(day);
		return time;
	}
}
