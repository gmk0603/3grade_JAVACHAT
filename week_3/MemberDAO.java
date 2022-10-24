package chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import chat.Member;
import chat.Time;

public class MemberDAO {
	DBConnection db = new DBConnection();
	static DBConnection db2 = new DBConnection();//아이디 체크를 위한 객체 생성
	
	MemberDAO() {}
	private static MemberDAO instance=new MemberDAO();
	public static MemberDAO getInstance() {
		return instance;
	}
	
	private Connection conn; //DB 연결 객체
	private PreparedStatement pstmt; //Query 작성 객체
	private ResultSet rs; //Query 결과 커서
	
	//성공 1, 실패 -1, 없음 0
	public int findByUsernameAndPassword(String id, String password) {
		//1. DB 연결
		conn = DBConnection.getConnection();
		
		try {
			//2. Query 작성
			pstmt = conn.prepareStatement("select * from member where id = ? and password = ?");
			
			//3. Query ? 완성 (index 1번 부터 시작)
			//setString, setInt, setDouble, setTimeStamp 등이 있음.
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			
			//4. Query 실행
			//(1) executeQuery() = select = ResultSet 리턴
			//(2) executeUpdate() = insert, update, delete = 리턴 없음.
			rs = pstmt.executeQuery();
			
			//5. rs는 query한 결과의 첫번째 행(레코드) 직전에 대기중
			//결과가 count(*) 그룹함수이기 때문에 1개의 행이 리턴됨. while문이 필요 없음.
			if(rs.next()) { //next()함수는 커서를 한칸 내리면서 해당 행에 데이터가 있으면 true, 없으면 false 반환
				//결과가 있다는 것은 해당 아이디와 비번에 매칭되는 값이 있다는 뜻.
				return 1; //로그인 성공
			}		
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return -1; //로그인 실패
	}
		   /**
	  * 인수로 들어온 ID에 해당하는 레코드 검색하여 중복여부 체크하기 리턴값이 true =사용가능 , false = 중복임
	  * */
	 public boolean getIdByCheck(String id) {//아이디 체크
	     boolean result = true;
	
	     try {
	    	 conn = DBConnection.getConnection();
	         pstmt = conn.prepareStatement("SELECT * FROM member WHERE id=?");
	         pstmt.setString(1, id.trim());
	         rs = pstmt.executeQuery(); //실행
	         if (rs.next())
	             result = false; //레코드가 존재하면 false
	
	     } catch (SQLException e) {
	         System.out.println(e + "=>  getIdByCheck fail");
	     } finally {
	    	 db.dbClose();
	     }
	
	     return result;
	
	 }//getIdByCheck()
	public static void main(String[] args) {
		db2.getConnection();
	}
	//성공 1, 실패 -1, 
	public int save(Member member) {
		conn = DBConnection.getConnection();
		
		try {
			String sql = "insert into member(" +
                    "id,password,name,email,phone,createdate)" +
                    "values(?,?,?,?,?, sysdate)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getName());
			pstmt.setString(4, member.getEmail());
			pstmt.setString(5, member.getPhone());
			pstmt.executeUpdate(); //return값은 처리된 레코드의 개수
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	//성공 Vector<Member>, 실패 null
	public Vector<Member> findByAll(){
		conn = DBConnection.getConnection();
		Vector<Member> members = new Vector<>();
		try {
			pstmt = conn.prepareStatement("select * from member");
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Member member = new Member();
				member.setId(rs.getString("id"));
				member.setPassword(rs.getString("password"));
				member.setName(rs.getString("name"));
				member.setEmail(rs.getString("email"));
				member.setPhone(rs.getString("phone"));
				member.setCreateDate(rs.getTimestamp("createDate"));
				members.add(member);
			}
			return members;
	
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}