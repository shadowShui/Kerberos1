package System;

/**
 * ��ű����ײ���ʶ
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
	
	public static final byte CONNECT_FAILED = (byte) 0b10000000;	// ���ӷ�����ʧ��
	public static final byte REPLY_FAILED = (byte) 0b10000001;		// ��ȡ����������ʧ��
	public static final byte WRITE_FAILED = (byte) 0b10000010;		// �������������Ϣʧ��
	public static final byte READ_FAILED = (byte) 0b10000011;		// ��ȡ��������Ϣʧ��
	
	public static final byte REGIST_TO_AS = 0b00010001;				// ��AS����������ע������
	public static final byte REGIST_SUCCESS = 0b00010010;			// ע��ɹ�
	public static final byte REGIST_FAILRD = 0b00010011;			// ע��ʧ��
	
	public static final byte CLOSE_APPS_SOCKET = 0b00010001;		// �ر�Ӧ�÷���������
	
	public static final byte UPDATE_FILE = 0b00000110;				// �ϴ��ļ�����
	public static final byte PERMIT_UPDATE = 0b00000111;			// APPS�����������ϴ��ļ�
	public static final byte DENY_UPDATE = 0b00001000;				// APPS��������ֹ�ϴ��ļ�
	public static final byte DOWNLOAD_FILE = 0b00001001;			// �����ļ�����
	public static final byte PERMIT_DOWNLOAD = 0b00001010;			// APPS���������������ļ�
	public static final byte DENY_DOWNLOAD = 0b00001011;			// APPS��������ֹ�����ļ�
	
	public static final byte REQUEST_FILENAME = 0b00001100;			// ��ȡ�ƶ��ļ���
	public static final byte REPLY_FILENAME = 0b00001101;			// �����ƶ��ļ���
	
	public static final byte REQUEST_RESEND_SOCKET = 0b00010100;	// ������������
	public static final byte RESEND_SOCKET_SUCCESS = 0b00010101;	// ������������ɹ�
	public static final byte RESEND_SOCKET_FAILED = 0b00010110;		// ������������ʧ��
	
	public static final byte SEND_TO_CLIENT = 0b00010111;			// ��ͻ��˴����ļ�����
	public static final byte TO_CLIENT_SUCCESS = 0b00011000;		// ��ͻ��˴����ļ�����ɹ�
	public static final byte TO_CLIENT_FAILED = 0b00011001;			// ��ͻ��˴����ļ�����ʧ��
	
	public static final byte GET_CNT_USR = 0b00011010;				// ��ȡ�����û�����
	public static final byte CNT_USR_SUCCESS = 0b00011011;			// ��ȡ�����û�����ɹ�
	public static final byte CNT_USR_FAILED = 0b00011100;			// ��ȡ�����û�����ʧ��
	
	public static final byte REQUEST_CERTIFICATE = 0b00011101;		// ��ȡ֤������
	public static final byte CERTIFICATE_SUCCESS = 0b00011110;		// ��ȡ֤������
	public static final byte CERTIFICATE_ERROR = 0b00011111;		// ��ȡ֤������
	
}
