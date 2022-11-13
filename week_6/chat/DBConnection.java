package chat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	PreparedStatement ps;
	Connection con;
	ResultSet rs;
	Statement st;
	
	public static Connection getConnection() {
		Connection con=null;
		String url="jdbc:oracle:thin:@localhost:1521:orcl";
		String id="c##scott";
		String pw="tiger";
		String driver="oracle.jdbc.driver.OracleDriver";
		try {
			Class.forName(driver);
			con=DriverManager.getConnection(url, id, pw);
			System.out.println("DB연결 완료");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("DB연결 실패");
		}
		return con;
	}
	public void dbClose() {
		try {
			if(rs !=null)rs.close();
			if(st !=null)st.close();
			if(ps !=null)ps.close();
		}catch(Exception e) {
			 System.out.println(e + "=> dbClose fail");
		}
	}
	public static void close(Connection c, PreparedStatement p) {
		try {
			if(p!=null)p.close();
			if(c!=null)c.close();
		}catch(Exception e) {}
	}
}