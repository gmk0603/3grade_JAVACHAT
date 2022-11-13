package chat;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.regex.Pattern;

import chat.Member;
import chat.Time;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chat.JoinFrame;
import chat.ChatClientObject;

public class MemberDAO {
	private static final int FFBF00 = 0;
	DBConnection db = new DBConnection();
	static DBConnection db2 = new DBConnection();//아이디 체크를 위한 객체 생성
	
	private static MemberDAO instance=new MemberDAO();
	public static MemberDAO getInstance() {
		return instance;
	}
	
	private Connection conn; //DB 연결 객체
	private PreparedStatement pstmt; //Query 작성 객체
	private ResultSet rs; //Query 결과 커서
	private Statement st;
	
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
		 * @return 
	  * */
	 public String nameCheck(String id) {
		 String result = "guest";
		 try {
			 conn = DBConnection.getConnection();
			 pstmt = conn.prepareStatement("SELECT name FROM member WHERE id=?");
			 pstmt.setString(1, id.trim());
			 rs = pstmt.executeQuery();
			 if (rs.next()) {
				 result = rs.getString("name");
			 }
		 }  catch (SQLException e) {
			 System.out.println(e + "=> 이름 찾기 실패");
		 } finally {
			 db.dbClose();
		 }
		 return result;
	 }
	
	 public boolean getIdByCheck(String id) {//아이디 중복 체크
	     boolean result = true;
	
	     try {
	    	 conn = DBConnection.getConnection();
	         pstmt = conn.prepareStatement("SELECT * FROM member WHERE id=?");
	         pstmt.setString(1, id.trim()); // 입력한 id를 가지고 select문에 대입
	         rs = pstmt.executeQuery(); //실행
	         if (rs.next())
	             result = false; //레코드가 존재하면 false
	
	     } catch (SQLException e) {
	         System.out.println(e + "=>  getIdByCheck fail"); //실패하면
	     } finally {
	    	 db.dbClose(); //db 종료
	     }
	
	     return result; // true 반환: 사용 가능 false 반환: 사용 불가능	
	 }//getIdByCheck()
	 
	 public boolean getPwByCheck(String pw) { //비밀번호 중복 확인
		 boolean result = true;
		 
		 try {
			 conn = DBConnection.getConnection();
			 pstmt = conn.prepareStatement("SELECT * FROM member WHERE password=?");
			 pstmt.setString(1, pw.trim()); // 입력한 pw를 가지고 select문에 대입
			 rs = pstmt.executeQuery();
			 if(rs.next())
				 result = false; //레코드가 존재한 false
			 
		 } catch (SQLException e) {
			 System.out.println(e + "=> getPwByCheck fail"); //실패하면
		 } finally {
			 db.dbClose(); //db 종료
		 }
		 
		 return result; // true 반환: 사용 가능 false 반환: 사용 불가능
	 }//getPwByCheck()
	 
	 //비밀번호 보안 강도 메소드
	 public String getPwSecStrength(String pw) {
		 Color color1 = new Color(0xFF8200); //보안강도 중 색상(주황색)
		 Color color2 = new Color(0x52E252); //보안강도 상 색상(초록색)
		 String result = null; //보안강도 텍스트를 보낼 필드
		 /* [A-Za-z\\d@$!%*?&]{9,15}: 대괄호 범위의 문자로 최소9~최대15문자로 구성 
		  * .*[a-z]: 영문 소문자 하나 또는 하나 이상 
		  * .*[A-Z]: 영문 대문자 하나 또는 하나 이상 
		  * .*[@$!%*?&]: 특수문자 하나 또는 하나 이상 */
		 if (pw.length() < 9) {
			 result = "<HTML>※최소 9~15자, 대문자 1, 소문자 1, 특수문자 1개<br>(@$!%*?&)로 구성해야됩니다.</HTML>";
			 JoinFrame.lblSecStrength.setForeground(Color.red); //보안강도 텍스트를 빨간색으로 변경
		 }
		 if (pw.length() >=9 && pw.length() <=12) {
			 Pattern passPattern1 = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{9,20}$");
			 Matcher matcher1 = passPattern1.matcher(pw); //패턴 해석 및 입력 문자열에 대한 일치 작업 수행 엔진
			 if(!matcher1.find()) { //규칙에 맞게 비밀번호 생성시 find()메소드가 true 출력함
				 //규칙 중 하나라도 부합하지 못한 경우 find()메소드가 false인 경우
				 result = "<HTML>※최소 9~15자, 대문자 1, 소문자 1, 특수문자 1개<br>(@$!%*?&)로 구성해야됩니다.</HTML>";  
			 } else if(!getPwByCheck(JoinFrame.tfPassword.getText())) {
				 result = "중복된 비밀번호입니다.";
			 } else {
				 result = "※보안강도 하! 현재 글자수: " + pw.length(); //규칙을 전부 통과하면 보안강도 표시
			 }
			 JoinFrame.lblSecStrength.setForeground(Color.red);
		 } else if (pw.length() >=13 && pw.length() <= 16) {
			 Pattern passPattern1 = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{9,20}$");
			 Matcher matcher1 = passPattern1.matcher(pw);
			 if(!matcher1.find()) { //규칙에 맞게 비밀번호 생성시 find()메소드가 true 출력함
				 //규칙 중 하나라도 부합하지 못한 경우 find()메소드가 false인 경우
				 result = "<HTML>※최소 9~15자, 대문자 1, 소문자 1, 특수문자 1개<br>(@$!%*?&)로 구성해야됩니다.</HTML>";  
			 } else if(!getPwByCheck(JoinFrame.tfPassword.getText())) {
				 result = "중복된 비밀번호입니다.";
			 } else {
				 result = "※보안강도 중! 현재 글자수: " + pw.length(); //규칙을 전부 통과하면 보안강도 표시
				 JoinFrame.lblSecStrength.setForeground(color1); //보안강도 텍스트를 주황색으로 변경
			 }		
		 } else if (pw.length() >=17 && pw.length() <=20) {
			 Pattern passPattern1 = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{9,20}$");
			 Matcher matcher1 = passPattern1.matcher(pw);
			 if(!matcher1.find()) { //규칙에 맞게 비밀번호 생성시 find()메소드가 true 출력함
				 //규칙 중 하나라도 부합하지 못한 경우 find()메소드가 false인 경우
				 result = "<HTML>※최소 9~15자, 대문자 1, 소문자 1, 특수문자 1개<br>(@$!%*?&)로 구성해야됩니다.</HTML>";  
			 } else if(!getPwByCheck(JoinFrame.tfPassword.getText())) {
				 result = "중복된 비밀번호입니다.";
			 } else {
				 result = "※보안강도 상! 현재 글자수: " + pw.length(); //규칙을 전부 통과하면 보안강도 표시
				 JoinFrame.lblSecStrength.setForeground(color2); //보안강도 텍스트를 초록색으로 변경
			 }			
		 } else if (pw.length() > 20) { //20자 이상 초과시
			result = "※20자 이상 초과했습니다!";
			JoinFrame.lblSecStrength.setForeground(Color.red);			 
		 }

		 return result;
	 }
	 
	 public boolean getPwPatternCheck(String pw) { //비밀번호 규칙 확인
		 boolean result = false;
		 /* [A-Za-z\\d@$!%*?&]{9,15}: 대괄호 범위의 문자로 최소9~최대15문자로 구성 
		  * .*[a-z]: 영문 소문자 하나 또는 하나 이상 
		  * .*[A-Z]: 영문 대문자 하나 또는 하나 이상 
		  * .*[@$!%*?&]: 특수문자 하나 또는 하나 이상 */
		 Pattern passPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{9,20}$");
		 Matcher matcher = passPattern.matcher(pw); //패턴 해석 및 입력 문자열에 대한 일치 작업 수행 엔진
		 
		 if (!matcher.find()) { // 규칙에 맞게 비밀번호 생성시 find()메소드가 true를 출력함
			 result = true; // 규칙 중 하나라도 부합하지 못하면 find()메소드가 false인 경우
		 }
		 return result; // true 반환: 사용 불가능 false 반환: 사용 가능
	 }

	//회원가입 정보 db에 삽입
	public int save(Member member) { 
		conn = DBConnection.getConnection();
		
		try {
			String sql = "insert into member(" +
                    "id,password,name,email,phone,zipcode,imagename,image,createdate,gender,birth)" +
                    "values(?,?,?,?,?,?,?,?, sysdate, ?,?)"; //member 테이블에 회원가입 정보 삽입
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getId()); //입력한 회원가입 정보를 가지고 insert문의 ?에 각각 대입
			pstmt.setString(2, member.getPassword()); //이하 동문
			pstmt.setString(3, member.getName());
			pstmt.setString(4, member.getEmail());
			pstmt.setString(5, member.getPhone());
			pstmt.setString(6, member.getZipcode());
			pstmt.setString(7, member.getImagename());
			File f = new File(member.getImagepath());
			FileInputStream fis = new FileInputStream(f); //파일을 byte단위로 읽어 오는 클래스
			pstmt.setBinaryStream(8, fis, (int)f.length()); //이미지를 blob 형태로 저장
			pstmt.setString(9, member.getGender());
			pstmt.setString(10, member.getBirth());
			pstmt.executeUpdate(); //return값은 처리된 레코드의 개수
			return 1; // 정상적으로 저장되면 1을 반환
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 저장 실패시 -1 반환 
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
				member.setZipcode(rs.getString("zipcode"));
				member.setImagename(rs.getString("imagename"));
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