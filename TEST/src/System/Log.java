package System;

import AS.ASServerThread;

public class Log {
	
	public static void print(Object data) {
		System.out.print(data);
	}
	
	public static void println() {
		println("");
	}
	
	public static void println(Object data) {
		System.out.println(data);
	}

	/**
	 * ×ª»¯Îª¶þ½øÖÆÊä³ö
	 * @param data
	 */
	public static void printBinaryln(Object data) {
		
		Class<?> c = data.getClass();
		
		try {
			if (c == Integer.class)
				println(Integer.toBinaryString((int) data));
			else if (c == Long.class)
				println(Long.toBinaryString((long) data));
			else if (c == Byte.class)
				println(Integer.toBinaryString(
						((byte) data & 0xFF) + 0x100).substring(1));
			else
				throw new Exception("can not cast from type" + c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static String logC2A(String path,byte[] clilentMsg,byte[] client_id_c){
		String end;
		FileHandle.append(path, "***************************************************************************************\r\n");
		FileHandle.append(path, Time.getCurrentTime() + "\r\n");
		//0
		FileHandle.append(path, "tag:");
		if(!ASServerThread.haveRegist(client_id_c)){
    		FileHandle.append(path, "ÓÃ»§Î´×¢²á\r\n");
    		end = "false";
			return end;
    	}
		else{
			FileHandle.append(path, "CLIENT_TO_AS\r\n");
		}
		//1-2 
		FileHandle.append(path, "id_c:");
		String id_c = String.valueOf(((int) clilentMsg[0] & 0xFF)+((((int)clilentMsg[1]) << 8) & 0xFF00));
		FileHandle.append(path,id_c + "\r\n");
		//3
		FileHandle.append(path,"id_tgs:");
		String id_tgs = String.valueOf((int)clilentMsg[2]);
		FileHandle.append(path,id_tgs + "\r\n");
		//4±£Áô×Ö¶Î
		//5-8
		FileHandle.append(path,"ts_1:");
		String ts_1 = String.valueOf((((int)clilentMsg[4]) & 0x000000FF)
	    		  + ((((int)clilentMsg[5]) << 8) & 0x0000FF00)
	    		  + ((((int)clilentMsg[6]) << 16) & 0x00FF0000)
	    		  + ((((int)clilentMsg[7]) << 24) & 0xFF000000));
		FileHandle.append(path,ts_1 + "\r\n");
		end = Time.getCurrentTime() +  "\n" + "tag:CLIENT_TO_AS\n" +"id_c:" 
				+ id_c + "\n" + "id_tgs:" + id_tgs + "\n" + "ts_1:"  + ts_1;
		return end;
	}
	
	
	public static String logA2C(String path,byte[] ticket_tgs,byte[] res_p){
		String end;
		FileHandle.append(path, "***************************************************************************************\r\n");
		FileHandle.append(path, Time.getCurrentTime() + "\r\n");
		//0
		FileHandle.append(path, "tag:");
		FileHandle.append(path, "AS_TO_CLIENT\r\n");
		//1-2 
		FileHandle.append(path, "Key_C_TGS:");
		String key_c_tgs = String.valueOf((int)((long) res_p[0] & 0xFF)+((((long)res_p[1]) << 8) & 0xFF00));
		FileHandle.append(path,key_c_tgs + "\r\n");
		//3
		FileHandle.append(path,"id_tgs:");
		String id_tgs = String.valueOf((int)res_p[2]);
		FileHandle.append(path,id_tgs + "\r\n");
		//4
		FileHandle.append(path,"lifetime_2:");
		String lifetime_2 = String.valueOf((int)res_p[3]);
		FileHandle.append(path,lifetime_2 + "\r\n");
		//5-8
		FileHandle.append(path,"ts_2:");
		String ts_2 = String.valueOf((((int)res_p[4]) & 0x000000FF)
	    		  + ((((int)res_p[5]) << 8) & 0x0000FF00)
	    		  + ((((int)res_p[6]) << 16) & 0x00FF0000)
	    		  + ((((int)res_p[7]) << 24) & 0xFF000000));
		FileHandle.append(path,ts_2 + "\r\n");
		//9-10
		FileHandle.append(path, "ticket_key_C_TGS:");
		String ticket_key_c_tgs = String.valueOf((int)((long) ticket_tgs[0] & 0xFF)+((((long)ticket_tgs[1]) << 8) & 0xFF00));
		FileHandle.append(path,ticket_key_c_tgs + "\r\n");
		//11
		FileHandle.append(path,"ticket_id_tgs:");
		String ticket_id_tgs = String.valueOf((int)ticket_tgs[2]);
		FileHandle.append(path,ticket_id_tgs + "\r\n");
		//12
		FileHandle.append(path,"ticket_lifetime_2:");
		String ticket_lifetime_2 = String.valueOf((int)ticket_tgs[3]);
		FileHandle.append(path,lifetime_2 + "\r\n");
		//13-14
		FileHandle.append(path,"ticket_id_c:");
		String ticket_id_c = String.valueOf((int)((long) ticket_tgs[4] & 0xFF)+((((long)ticket_tgs[5]) << 8) & 0xFF00));
		FileHandle.append(path,ticket_id_c + "\r\n");
		//15-16±£Áô×Ö¶Î
		//17-20
		FileHandle.append(path,"ticket_ts_2:");
		String ticket_ts_2 = String.valueOf((((int)ticket_tgs[8]) & 0x000000FF)
	    		  + ((((int)ticket_tgs[9]) << 8) & 0x0000FF00)
	    		  + ((((int)ticket_tgs[10]) << 16) & 0x00FF0000)
	    		  + ((((int)ticket_tgs[11]) << 24) & 0xFF000000));
		FileHandle.append(path,ticket_ts_2 + "\r\n");
		//21-24
		FileHandle.append(path,"ticket_ad_c:");
		String ticket_ad_c = String.valueOf((((int) ticket_tgs[12]) & 0x00FF) + "." + (((int) ticket_tgs[13]) & 0x00FF) + "." 
							+ (((int) ticket_tgs[14]) & 0x00FF) + "."  + (((int) ticket_tgs[15]) & 0x00FF)) ;
		FileHandle.append(path,ticket_ad_c + "\r\n");
		end =Time.getCurrentTime() +  "\n" + "tag:AS_TO_CLIENT\n" + "Key_C_TGS:" + key_c_tgs +  "\n" + "id_tgs:" + id_tgs  +  "\n"
				+ "lifetime_2:" + lifetime_2 +  "\n" +  "ts_2:" + ts_2 + "\n" + "ticket_key_C_TGS:" + ticket_key_c_tgs + "\n"
				+ "ticket_id_tgs:" + ticket_id_tgs +  "\n" +  "ticket_lifetime_2:" + ticket_lifetime_2 +  "\n"  + "ticket_id_c:"
				+ ticket_id_c +"\n" + "ticket_ts_2:" + ticket_ts_2 + "\n" + "ticket_ad_c:" + ticket_ad_c;
		return end;
	}
	
	
	public static String logC2T(String path,byte[] clientMsg,byte[] ticket_tgs,byte[]authenticator_c){
		String end;
		FileHandle.append(path, "***************************************************************************************\r\n");
		FileHandle.append(path, Time.getCurrentTime() + "\r\n");
		//0
		FileHandle.append(path, "tag:");
		FileHandle.append(path, "CLIENT_TO_TGS\r\n");
		//1-2 
		FileHandle.append(path, "id_v:");
		String id_v = String.valueOf((int)(((short) clientMsg[0] & 0xFF)
				+((((short)clientMsg[1]) << 8) & 0xFF00)));
		FileHandle.append(path,id_v + "\r\n");
		//3-4±£Áô×Ö¶Î
		//5-6
		FileHandle.append(path, "Key_C_TGS:");
		String key_c_tgs = String.valueOf((int)((long) ticket_tgs[0] & 0xFF)+((((long)ticket_tgs[1]) << 8) & 0xFF00));
		FileHandle.append(path,key_c_tgs + "\r\n");
		//7
		FileHandle.append(path,"id_tgs:");
		String id_tgs = String.valueOf((int)ticket_tgs[2]);
		FileHandle.append(path,id_tgs + "\r\n");
		//8
		FileHandle.append(path,"lifetime_2:");
		String lifetime_2 = String.valueOf((int)ticket_tgs[3]);
		FileHandle.append(path,lifetime_2 + "\r\n");
		//9-10
		FileHandle.append(path,"ticket_id_c:");
		String ticket_id_c = String.valueOf((int)((long) ticket_tgs[4] & 0xFF)+((((long)ticket_tgs[5]) << 8) & 0xFF00));
		FileHandle.append(path,ticket_id_c + "\r\n");
		//11-12±£Áô×Ö¶Î
		//13-16
		FileHandle.append(path,"ts_2:");
		String ts_2 = String.valueOf((((int)ticket_tgs[8]) & 0x000000FF)
	    		  + ((((int)ticket_tgs[9]) << 8) & 0x0000FF00)
	    		  + ((((int)ticket_tgs[10]) << 16) & 0x00FF0000)
	    		  + ((((int)ticket_tgs[11]) << 24) & 0xFF000000));
		FileHandle.append(path,ts_2 + "\r\n");
		//17-20 
		FileHandle.append(path,"ticket_ad_c:");
		String ticket_ad_c = String.valueOf((((int) ticket_tgs[12]) & 0x00FF) + "." + (((int) ticket_tgs[13]) & 0x00FF) + "." 
							+ (((int) ticket_tgs[14]) & 0x00FF) + "."  + (((int) ticket_tgs[15]) & 0x00FF)) ;
		FileHandle.append(path,ticket_ad_c + "\r\n");
		//21-22
		FileHandle.append(path,"authenticator_id_c:");
		String authenticator_id_c = String.valueOf((int)((long) authenticator_c[0] & 0xFF)+((((long)authenticator_c[1]) << 8) & 0xFF00));
		FileHandle.append(path,authenticator_id_c + "\r\n");
		//23-24±£Áô×Ö¶Î
		//25-28
		FileHandle.append(path,"ts_3:");
		String ts_3 = String.valueOf((((int)authenticator_c[4]) & 0x000000FF)
	    		  + ((((int)authenticator_c[5]) << 8) & 0x0000FF00)
	    		  + ((((int)authenticator_c[6]) << 16) & 0x00FF0000)
	    		  + ((((int)authenticator_c[7]) << 24) & 0xFF000000));
		
		FileHandle.append(path,ts_3 + "\r\n");
		//29-32
		FileHandle.append(path,"authenticator_ad_c:");
		String authenticator_ad_c = String.valueOf((((int) authenticator_c[8]) & 0x00FF) + "." + (((int) authenticator_c[9]) & 0x00FF) + "." 
						+ (((int) authenticator_c[10]) & 0x00FF) + "."  + (((int) authenticator_c[11]) & 0x00FF));
		FileHandle.append(path,authenticator_ad_c + "\r\n");
		//33-36±£Áô×Ö¶Î
		end = Time.getCurrentTime() +  "\n" + "tag:CLIENT_TO_TGS\n" + "id_v:" + id_v + "\n" + "Key_C_TGS:" + key_c_tgs +  "\n" +  "id_tgs:" 
				+ id_tgs  +  "\n" + "lifetime_2:" + lifetime_2 +  "\n" + "ticket_id_c:" + ticket_id_c + "\n" + "ts_2:" + ts_2 + "\n" 
				+ "ticket_ad_c:" + ticket_ad_c +"\n" + "authenticator_id_c:" + authenticator_id_c + "\n" + "ts_3:" + ts_3 + "\n" 
				+ "authenticator_ad_c:" + authenticator_ad_c;
		return end;
	}
	
	
	public static String logT2C(String path,byte[] ticket_v, byte[] res_t,byte[]res){
		String end;
		FileHandle.append(path, "***************************************************************************************\r\n");
		FileHandle.append(path, Time.getCurrentTime() + "\r\n");
		//0
		FileHandle.append(path, " tag:");
    	if (res[0] == MsgTag.REPLY_FAILED){
    		FileHandle.append(path, "REPLY_FAILED\r\n");
    		end = "false";
    		return end;
    	}
    	else if (res[0] == MsgTag.TGS_TO_CLIENT){
    		FileHandle.append(path, "TGS_TO_CLIENT\r\n");
    	}
		//1-2 
		FileHandle.append(path, "Key_C_TGS:");
		String key_c_tgs = String.valueOf((int)((long) res_t[0] & 0xFF)+((((long)res_t[1]) << 8) & 0xFF00));
		FileHandle.append(path,key_c_tgs + "\r\n");
    	//3-4
		FileHandle.append(path, "id_v:");
		String id_v = String.valueOf((int)(((short) res_t[2] & 0xFF)
				+((((short)res_t[3]) << 8) & 0xFF00)));
		FileHandle.append(path,id_v + "\r\n");
		//5-8
		FileHandle.append(path,"ts_4:");
		String ts_4 = String.valueOf((((int)res_t[4]) & 0x000000FF)
	    		  + ((((int)res_t[5]) << 8) & 0x0000FF00)
	    		  + ((((int)res_t[6]) << 16) & 0x00FF0000)
	    		  + ((((int)res_t[7]) << 24) & 0xFF000000));
		FileHandle.append(path,ts_4 + "\r\n");
		//9-10
		FileHandle.append(path, "Key_C_V:");
		String key_c_v = String.valueOf((int)((long) ticket_v[0] & 0xFF)+((((long)ticket_v[1]) << 8) & 0xFF00));
		FileHandle.append(path,key_c_v + "\r\n");
		//11-12
		FileHandle.append(path, "ticket_id_v:");
		String ticket_id_v = String.valueOf((int)(((short) res_t[2] & 0xFF)
				+((((short)res_t[3]) << 8) & 0xFF00)));
		FileHandle.append(path,ticket_id_v + "\r\n");
		//13-14
		FileHandle.append(path, "ticket_id_c:");
		String ticket_id_c = String.valueOf((int)(((short) ticket_v[4] & 0xFF)
				+((((short)ticket_v[5]) << 8) & 0xFF00)));
		FileHandle.append(path,ticket_id_c + "\r\n");
		//15
		FileHandle.append(path,"lifetime_4:");
		String lifetime_4 = String.valueOf((int)ticket_v[6]);
		FileHandle.append(path,lifetime_4 + "\r\n");
		//16±£Áô×Ö¶Î
		//17-20
		FileHandle.append(path,"ticket_ts_4:");
		String ticket_ts_4 = String.valueOf((((int)ticket_v[8]) & 0x000000FF)
	    		  + ((((int)ticket_v[9]) << 8) & 0x0000FF00)
	    		  + ((((int)ticket_v[10]) << 16) & 0x00FF0000)
	    		  + ((((int)ticket_v[11]) << 24) & 0xFF000000));
		FileHandle.append(path,ticket_ts_4 + "\r\n");
		//21-24
		FileHandle.append(path,"ticket_ad_c:");
		String ticket_ad_c = String.valueOf((((int) ticket_v[12]) & 0x00FF) + "." + (((int) ticket_v[13]) & 0x00FF) + "." 
						+ (((int) ticket_v[14]) & 0x00FF) + "."  + (((int) ticket_v[15]) & 0x00FF));
		FileHandle.append(path,ticket_ad_c + "\r\n");
		end =Time.getCurrentTime() +  "\n" + "tag:TGS_TO_CLIENT\n" + "Key_C_TGS:" + key_c_tgs +  "\n" + "id_v:" + id_v + "\n"
				+ "ts_4:" + ts_4 +  "\n" + "Key_C_V:" + key_c_v +  "\n" + "ticket_id_v:" + ticket_id_v + "\n" + "ticket_id_c:" 
				+ ticket_id_c + "\n" + "lifetime_4:" + lifetime_4 + "\n" + "ticket_ts_4:" + ticket_ts_4 + "\n" + "ticket_ad_c:"
				+ ticket_ad_c + "\n";
		return end;
	}
	
	
	public static String logC2V(String path, byte[] ticket_v, byte[] authenticator_c){
		String end;
		FileHandle.append(path, "***************************************************************************************\r\n");
		FileHandle.append(path, Time.getCurrentTime() + "\r\n");
		//0
		FileHandle.append(path, "tag:");
		FileHandle.append(path, "CLIENT_TO_V\r\n");
		
		FileHandle.append(path, "Key_C_V:");
		String key_c_v = String.valueOf((int)((long) ticket_v[0] & 0xFF)+((((long)ticket_v[1]) << 8) & 0xFF00));
		FileHandle.append(path, key_c_v + "\r\n");
		
		FileHandle.append(path, "id_v:");
		String id_v = String.valueOf((int)((long) ticket_v[2] & 0xFF)+((((long)ticket_v[3]) << 8) & 0xFF00));
		FileHandle.append(path, id_v + "\r\n");
		//7
		FileHandle.append(path,"id_c:");
		String id_c = String.valueOf((int)((long) ticket_v[4] & 0xFF)+((((long)ticket_v[5]) << 8) & 0xFF00));
		FileHandle.append(path, id_c + "\r\n");
		//8
		FileHandle.append(path,"lifetime_4:");
		String lifetime_4 = String.valueOf((int)ticket_v[6]);
		FileHandle.append(path,lifetime_4 + "\r\n");
	
		//11-12±£Áô×Ö¶Î
		//13-16
		FileHandle.append(path,"ts_4:");
		String ts_4 = String.valueOf((((int)ticket_v[8]) & 0x000000FF)
	    		  + ((((int)ticket_v[9]) << 8) & 0x0000FF00)
	    		  + ((((int)ticket_v[10]) << 16) & 0x00FF0000)
	    		  + ((((int)ticket_v[11]) << 24) & 0xFF000000));
		FileHandle.append(path,ts_4 + "\r\n");
		
		
		//17-20 
		FileHandle.append(path,"ticket_ad_c:");
		String ticket_ad_c = String.valueOf((((int) ticket_v[12]) & 0x00FF) + "." + (((int) ticket_v[13]) & 0x00FF) + "." 
							+ (((int) ticket_v[14]) & 0x00FF) + "."  + (((int) ticket_v[15]) & 0x00FF)) ;
		FileHandle.append(path,ticket_ad_c + "\r\n");
		
		//21-22
		FileHandle.append(path,"authenticator_id_c:");
		String authenticator_id_c = String.valueOf((int)((long) authenticator_c[0] & 0xFF)+((((long)authenticator_c[1]) << 8) & 0xFF00));
		FileHandle.append(path,authenticator_id_c + "\r\n");
		//23-24±£Áô×Ö¶Î
		//25-28
		FileHandle.append(path,"ts_5:");
		String ts_5 = String.valueOf((((int)authenticator_c[4]) & 0x000000FF)
	    		  + ((((int)authenticator_c[5]) << 8) & 0x0000FF00)
	    		  + ((((int)authenticator_c[6]) << 16) & 0x00FF0000)
	    		  + ((((int)authenticator_c[7]) << 24) & 0xFF000000));
		
		FileHandle.append(path,ts_5 + "\r\n");
		//29-32
		FileHandle.append(path,"authenticator_ad_c:");
		String authenticator_ad_c = String.valueOf((((int) authenticator_c[8]) & 0x00FF) + "." + (((int) authenticator_c[9]) & 0x00FF) + "." 
						+ (((int) authenticator_c[10]) & 0x00FF) + "."  + (((int) authenticator_c[11]) & 0x00FF));
		FileHandle.append(path,authenticator_ad_c + "\r\n");
		
		//33-36±£Áô×Ö¶Î
		end = Time.getCurrentTime() +  "\n" + "tag:CLIENT_TO_V\n" + "Key_C_V:" + key_c_v +  "\n" +  "id_V:" 
				+ id_v +  "\n" + "id_c:" + id_c +  "\n" + "lifetime_4:" + lifetime_4 +  "\n" + "ts_4:" + ts_4 + "\n" 
				+ "ticket_ad_c:" + ticket_ad_c + "\n" + "authenticator_id_c:" + authenticator_id_c + "\n" + "ts_5:" + ts_5 + "\n" 
				+ "authenticator_ad_c:" + authenticator_ad_c;
		return end;
		
	}
	
	
	public static String logV2C(String path, byte[]res){
		
		String end;
		FileHandle.append(path, "***************************************************************************************\r\n");
		FileHandle.append(path, Time.getCurrentTime() + "\r\n");
		//0
		FileHandle.append(path, "tag:");
		FileHandle.append(path, "V_TO_CLIENT\r\n");
		FileHandle.append(path,"ts_6:");
		String ts_6 = String.valueOf((((int)res[0]) & 0x000000FF)
	    		  + ((((int)res[1]) << 8) & 0x0000FF00)
	    		  + ((((int)res[2]) << 16) & 0x00FF0000)
	    		  + ((((int)res[3]) << 24) & 0xFF000000));
		
		FileHandle.append(path,ts_6 + "\r\n");
		end = Time.getCurrentTime() +  "\n" + "tag:V_TO_CLIENT\n" + "ts_6:" + ts_6;
		
		return end;
	}
}
