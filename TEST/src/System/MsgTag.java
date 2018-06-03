package System;

/**
 * 存放报文首部标识
 * @author fangyunniu
 *
 */
public class MsgTag {
	
	public static final byte CLIENT_TO_AS = 0b00000000;
	public static final byte AS_TO_CLIENT = 0b00000001;
	public static final byte CLIENT_TO_TGS = 0b00000010;
	public static final byte TGS_TO_CLIENT = 0b00000011;
	public static final byte CLIENT_TO_V = 0b00000100;
	public static final byte V_TO_CLIENT = 0b00000101;
	
	public static final byte CONNECT_FAILED = (byte) 0b10000000;	// 连接服务器失败
	public static final byte REPLY_FAILED = (byte) 0b10000001;		// 获取服务器请求失败
	public static final byte WRITE_FAILED = (byte) 0b10000010;		// 向服务器发送消息失败
	public static final byte READ_FAILED = (byte) 0b10000011;		// 获取服务器消息失败
	
	public static final byte REGIST_TO_AS = 0b00010001;				// 向AS服务器发送注册请求
	public static final byte REGIST_SUCCESS = 0b00010010;			// 注册成功
	public static final byte REGIST_FAILRD = 0b00010011;			// 注册失败
	
	public static final byte CLOSE_APPS_SOCKET = 0b00010001;		// 关闭应用服务器连接
	
	public static final byte UPDATE_FILE = 0b00000110;				// 上传文件请求
	public static final byte PERMIT_UPDATE = 0b00000111;			// APPS服务器允许上传文件
	public static final byte DENY_UPDATE = 0b00001000;				// APPS服务器禁止上传文件
	public static final byte DOWNLOAD_FILE = 0b00001001;			// 下载文件请求
	public static final byte PERMIT_DOWNLOAD = 0b00001010;			// APPS服务器允许下载文件
	public static final byte DENY_DOWNLOAD = 0b00001011;			// APPS服务器禁止下载文件
	
	public static final byte REQUEST_FILENAME = 0b00001100;			// 获取云端文件名
	public static final byte REPLY_FILENAME = 0b00001101;			// 返回云端文件名
	
	public static final byte REQUEST_RESEND_SOCKET = 0b00010100;	// 发送桥梁申请
	public static final byte RESEND_SOCKET_SUCCESS = 0b00010101;	// 发送桥梁申请成功
	public static final byte RESEND_SOCKET_FAILED = 0b00010110;		// 发送桥梁申请失败
	
	public static final byte SEND_TO_CLIENT = 0b00010111;			// 向客户端传输文件请求
	public static final byte TO_CLIENT_SUCCESS = 0b00011000;		// 向客户端传输文件请求成功
	public static final byte TO_CLIENT_FAILED = 0b00011001;			// 向客户端传输文件请求失败
	
	public static final byte GET_CNT_USR = 0b00011010;				// 获取在线用户请求
	public static final byte CNT_USR_SUCCESS = 0b00011011;			// 获取在线用户请求成功
	public static final byte CNT_USR_FAILED = 0b00011100;			// 获取在线用户请求失败
	
	public static final byte REQUEST_CERTIFICATE = 0b00011101;		// 获取证书请求
	public static final byte CERTIFICATE_SUCCESS = 0b00011110;		// 获取证书请求
	public static final byte CERTIFICATE_ERROR = 0b00011111;		// 获取证书请求
	
}
