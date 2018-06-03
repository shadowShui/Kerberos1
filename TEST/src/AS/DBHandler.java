package AS;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class DBHandler {
	
	String database;
	String url = "jdbc:mysql://localhost:3306/";
	String user = "root";
	String psd = "123456";
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	
	public DBHandler() {}
	public DBHandler(String database) {
		
		this.database = database;
		url += database;
	}
	// 与数据库取得连接方法
	public Connection getConn(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, psd);
		}catch(Exception ex){
			ex.printStackTrace();
		}		
		return conn;
	}
	
	// 执行查询操作方法
	public String query(String sql,String[] args){
		String result="";
		try{
			conn = getConn();
			System.out.println(sql);
			ps = conn.prepareStatement(sql);
			for(int i = 0; i < args.length; i ++){
				ps.setString(i+1, args[i]);
			}			
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();			
			int count = rsmd.getColumnCount();
			System.out.println(count);
			while (rs.next()) {
				for(int i = 1; i <= count; i ++){
					// 进行编码
					//result += URLEncoder.encode(rs.getString(i), "UTF-8") + " ";
					result += rs.getString(i) + " ";
				}				
			}
			System.out.println(result);
		}catch (Exception ex) {
			ex.printStackTrace();
		}		
		return result;		
	}
	
	// 插入操作方法
	public boolean insert(String sql,String[] args){
		boolean flag = false;
		try{
			conn = getConn();
			System.out.println(sql);
			ps = conn.prepareStatement(sql);
			for(int i = 0; i < args.length; i ++){
				ps.setString(i+1, args[i]);
			}			
			int i = ps.executeUpdate();
			System.out.println(i);
			if(i == 1){
				flag = true;
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}		
		return flag;
	}
	
	// 检查用户是否存在
	public boolean checkUser(String sql,String[] args){
		boolean flag = false;
		try{
			conn = getConn();
			ps = conn.prepareStatement(sql);
			for(int i = 0; i < args.length; i ++){
				ps.setString(i+1, args[i]);
			}			
			rs = ps.executeQuery();
			if(rs.next()){
				flag = true;
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}		
		return flag;
	}
	
	//生成MD5  
    public static String getMD5(String message) {  
        String md5 = "";  
        try {  
            MessageDigest md = MessageDigest.getInstance("MD5");  // 创建一个md5算法对象  
            byte[] messageByte = message.getBytes("UTF-8");  
            byte[] md5Byte = md.digest(messageByte);              // 获得MD5字节数组,16*8=128位  
            md5 = bytesToHex(md5Byte);                            // 转换为16进制字符串  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return md5;  
    }  
   
     // 二进制转十六进制  
    public static String bytesToHex(byte[] bytes) {  
        StringBuffer hexStr = new StringBuffer();  
        int num;  
        for (int i = 0; i < bytes.length; i++) {  
            num = bytes[i];  
             if(num < 0) {  
                 num += 256;  
            }  
            if(num < 16){  
                hexStr.append("0");  
            }  
            hexStr.append(Integer.toHexString(num));  
        }  
        return hexStr.toString();  
    }	
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		DBHandler dbh = new DBHandler("as_usr");
		/* boolean b = dbh.checkUser("select * from worker where ID=? and pwd=?", new String[]{ide, pwd});
		 * boolean b = dbh.insert("insert into Sign(ID, Time)values(?, ?)",
				new String[] { id, dateNowStr });
		 * String result = dbh.query("select ID, UpdateTime, NoteName from sharenote",new String[]{});
		String result=dbh.query(
				"insert into user(user_name,user_psd)values(?,?)",
				new String[] { "chinesenewton", "123456" });*/
		
		boolean b = dbh.checkUser("select * from regist where ID=?", new String[]{"888"});
	}
}
