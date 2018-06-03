package System;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IP {
	//获取ADc：Client的网络地址
	public static byte[] getIPAddress ()
	{
		byte [] IP = new byte[4];
		try {
			InetAddress address = InetAddress.getLocalHost();//获取的是本地的IP地址 //PC-20140317PXKX/192.168.0.121
			Log.println(address);
			String hostAddress = address.getHostAddress();//得到IP地址为string类型
			Log.println(hostAddress);
			String[] ip_string = hostAddress.split("\\.");
			int [] ip_int = new int[ip_string.length];
			for(int i=0;i<ip_string.length;i++)
			{
				//得到int型Ip,再转换为byte类型
				ip_int[i]=Integer.parseInt(ip_string[i]);
				//System.out.println(ip_int[i]);
				IP[i]=(byte)(ip_int[i] & 0xff);
				//Log.printBinaryln(IP[i]);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return IP;
	}
	
	public static void main(String[] args) {
		
		byte[] res = getIPAddress();
		Log.printBinaryln(res[0]);
		Log.printBinaryln(res[1]);
		Log.printBinaryln(res[2]);
		Log.printBinaryln(res[3]);
		
	}
}
