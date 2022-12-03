package chat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatSave {
	DBConnection db = new DBConnection();
	
	private Connection conn; //DB 연결 객체
	private PreparedStatement pstmt; //Query 작성 객체
	private ResultSet rs; //Query 결과 커서
	private Statement st;
	ChatClient client;

	String name = null;
	StringBuffer dbChatlog;
	
	public String find_name(String id) {
		String result = null;
		conn = DBConnection.getConnection();
		try {
			
			String sql = "select * from member where id= \'" + id.trim() + "\'";			
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) { //아이디(id)와 매칭되는 이름(name)을 찾은 경우
				result = rs.getString(3); //이름의 컬럼은 3번째에 존재하고 이를 result 변수에 저장한다.
			} while (rs.next()); //찾을 때까지 반복문을 수행하게 된다.
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return result;
	}
	public void dbChatExtrac(String id) {
		conn = DBConnection.getConnection();
		dbChatlog = new StringBuffer();
		String filePath = "C:/Temp/chatlog.txt";
		try {
			String sql = "select * from chatlog_table where id= \'" + id.trim() + "\' order by savedate ASC";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				dbChatlog.append(rs.getString(5) + "\n" + rs.getString(4) + "\n\n");
			}
			rs.close();
			st.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			FileWriter fileWriter = new FileWriter(filePath);
			fileWriter.write(dbChatlog.toString());
			fileWriter.close();
		} catch (IOException e){
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void create(User user) {
		conn = DBConnection.getConnection();
		name = find_name(RoomUI.getID);
		try {
			String sql = "insert into chatlog_table(" //insert문을 통해 채팅 기록 테이블에 저장하게 된다.
					+ "no, id, name, chatlog, savedate)"
					+ "values(chat_seq.NEXTVAL, ?, ?, ?, sysdate)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, RoomUI.getID.trim()); //아이디 삽입
			pstmt.setString(2, name); //위에 호출에서 받은 이름을 삽입
			pstmt.setString(3, user.getchatLog()); //채팅 기록을 가져와 삽입
			pstmt.executeUpdate(); //추가(Insert), 삭제(Delete), 수정(Update)하는 SQL 문을 실행하기 위한 메소드 호출
		} catch(Exception e) {
			e.printStackTrace();
		}
		dbChatExtrac(RoomUI.getID);
		/* 테이블 및 시퀀스 생성
		try {
			String sql = "Create table chatlog_table (" +
						"no INT NOT NULL," + 
						"chatlog clob)"; //각 텍스트 로그
			String sql2 = "CREATE SEQUENCE chat_seq "
					+ "START WITH 1 "
					+ "INCREMENT BY 1 "
					+ "MAXVALUE 9999 "
					+ "NOCYCLE NOCACHE";
			//sql문을 실행시키기위한 Statement객체 얻어오기
			st = conn.createStatement();
			System.out.println("테이블 생성 완료");
			
			st.execute(sql);
			st.execute(sql2);
			
			if(st!=null) st.close();
			if(conn!=null) conn.close();
		} catch(SQLException e) {
			System.out.println("SQL오류: chatlog_table이 생성되어 있는지 확인하세요.");
			e.printStackTrace();
		}
		*/
	}

	private static ChatSave instance = new ChatSave(); //객체 생성
	public static ChatSave getInstance() {
		return instance;
	}
}
