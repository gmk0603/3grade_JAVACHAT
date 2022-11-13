package chat.management;

import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
//UserDefaultJTableDAO.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import chat.management.UserJDailogGUI;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class UserDefaultJTableDAO {
	public static String imagename; // 이미지 파일명
	public static String imagepath = "C:/Temp/"; // db에서 가져온 이미지 저장 경로
	public static String id; 	
	
	/**
	 * 필요한 변수선언
	 * */
	Connection con;
	Statement st;
	PreparedStatement ps;
	ResultSet rs;

	/**
	 * 로드 연결을 위한 생성자
	 * */
	public UserDefaultJTableDAO() {
		try {
			// 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 연결
			con = DriverManager
					.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
							"c##scott", "tiger");

		} catch (ClassNotFoundException e) {
			System.out.println(e + "=> 로드 fail");
		} catch (SQLException e) {
			System.out.println(e + "=> 연결 fail");
		}
	}//생성자 

	/**
	 * DB닫기 기능 메소드
	 * */
	public void dbClose() {
		try {
			if (rs != null) rs.close();
			if (st != null)	st.close();
			if (ps != null)	ps.close();
		} catch (Exception e) {
			System.out.println(e + "=> dbClose fail");
		}
	}//dbClose() ---

	/**
	 * 인수로 들어온 ID에 해당하는 레코드 검색하여 중복여부 체크하기 리턴값이 true =사용가능 , false = 중복임
	 * */
	 //안씀
	public boolean getIdByCheck(String id) {
		boolean result = true;

		try {
			ps = con.prepareStatement("SELECT * FROM member WHERE id=?");
			ps.setString(1, id.trim());
			rs = ps.executeQuery(); //실행
			if (rs.next())
				result = false; //레코드가 존재하면 false

		} catch (SQLException e) {
			System.out.println(e + "=>  getIdByCheck fail");
		} finally {
			dbClose();
		}

		return result;

	}//getIdByCheck()

	/**
	 * userlist의 모든 레코드 조회
	 * */
	 //콤보박스 ALL 선택후 검색 버튼 클릭 및 회원관리 창 접속시 회원정보 플로팅 메소드
	public void userSelectAll(DefaultTableModel t_model) {
		try {
			st = con.createStatement();
			rs = st.executeQuery("select * from member order by id");

			// DefaultTableModel에 있는 기존 데이터 지우기
			for (int i = 0; i < t_model.getRowCount();) {
				t_model.removeRow(0);
			}

			while (rs.next()) { // 검색된 데이터를 getString을 이용해 컬럼별로 전부 가져온다.
				Object data[] = { rs.getString(1), rs.getString(2),
						rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(10), rs.getString(11) };

				t_model.addRow(data); //DefaultTableModel에 레코드 추가
			}

		} catch (SQLException e) {
			System.out.println(e + "=> userSelectAll fail");
		} finally {
			dbClose();
		}
	}//userSelectAll()

	/**
	 * ID에 해당하는 레코드 삭제하기
	 * */
	public int userDelete(String id) {
		int result = 0;
		try {
			ps = con.prepareStatement("delete member where id = ? "); //삭제 sql문
			ps.setString(1, id.trim()); //검색된 아이디로 삭제함
			result = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e + "=> userDelete fail");
		}finally {
			dbClose();
		}

		return result;
	}//userDelete()

	/**
	 * ID에 해당하는 레코드 수정하기
	 * */
	public int userUpdate(UserJDailogGUI user) {
		int result = 0;
		String sql = "UPDATE member SET password=?, name=?, email=?, phone=?, zipcode=?, gender=?, birth=? WHERE id=?";

		try {
			ps = con.prepareStatement(sql);
			// ?의 순서대로 입력된 값 넣기
			ps.setString(1, user.password.getText());
			ps.setString(2, user.name.getText());
			ps.setString(3, user.email.getText());
			ps.setString(4, user.phone.getText());
			ps.setString(5, user.zipcode.getText());
			if (UserJDailogGUI.tfMan.isSelected()) { //남자 버튼이 선택됬다면
				ps.setString(6, "M"); //성별 컬럼을 M(남자)으로 업데이트
			} else {
				ps.setString(6, "W"); //성별 컬럼을 W(여자)로 업데이트
			}
			ps.setString(7, user.birth.getText());
			ps.setString(8, user.id.getText().trim());

			// 실행하기
			result = ps.executeUpdate(); //sql문 실행

		} catch (SQLException e) {
			System.out.println(e + "=> userUpdate fail");
		} finally {
			dbClose();
		}

		return result;
	}//userUpdate()

	/**
	 * 검색단어에 해당하는 레코드 검색하기 (like연산자를 사용하여 _, %를 사용할때는 PreparedStatemnet안된다. 반드시
	 * Statement객체를 이용함)
	 * */
	public void getUserSearch(DefaultTableModel dt, String fieldName,
			String word) {
		//검색창에 검색어를 가지고 sql문 수행 fieldName = 콤보 데이터를 가져옴, word = 검색창에 입력된 검색어
		String sql = "SELECT * FROM member WHERE " + fieldName.trim() 
				+ " LIKE '%" + word.trim() + "%'";

		try {
			st = con.createStatement();
			rs = st.executeQuery(sql);

			// DefaultTableModel에 있는 기존 데이터 지우기
			for (int i = 0; i < dt.getRowCount();) {
				dt.removeRow(0);
			}

			while (rs.next()) { // 검색된 데이터를 getString을 이용해 컬럼별로 전부 가져온다.
				Object data[] = { rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(10), rs.getString(11) };

				dt.addRow(data);
			}

		} catch (SQLException e) {
			System.out.println(e + "=> getUserSearch fail");
		} finally {
			dbClose();
		}

	}//getUserSearch()
	
	//테이블을 선택할 때마다 해당 회원의 이미지를 출력
	public static void floatImage(String id) {	
	    String url="jdbc:oracle:thin:@localhost:1521:orcl";
	    String id1="c##scott";
	    String pw="tiger";
	    String driver="oracle.jdbc.driver.OracleDriver";
	    try {
	    	Class.forName(driver);
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }
	        
	    Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null; //select 의 결과를 저장
	    try {
	        con = DriverManager.getConnection(url, id1, pw);
	        //test1 테이블의 모든 데이터 읽어오는 sql
	        stmt = con.prepareStatement("select * from member where id=?"); // 자신의 id에 대한 blob 이미지 파일 및 이미지 이름 가져오기
	        stmt.setString(1, id.trim()); // 입력받은 id를 이용해 select문 검색
	        rs = stmt.executeQuery();
	        if(rs.next()){
	            do{
	                imagename = rs.getString("imagename"); // db에 저장된 이미지 파일명 가져오기
	                System.out.println(imagename); // 이미지 파일명 출력
	                System.out.println("blob 이미지 변환 후 저장 경로: " + imagepath + imagename); // 이미지 저장경로 출력
	                InputStream is = rs.getBinaryStream("image"); // blob 이미지 가져오기
	                FileOutputStream fos = new FileOutputStream(imagepath + imagename); // 파일 저장
	                byte[] buf = new byte[512]; // 해당 위치부터 blob 이미지 변환
	                int len;
	                while(true){
	                    //is에서 buf 만큼 읽어서 buf에 저장하고
	                    //읽은 갯수를 len에 저장
	                    //읽지 못하면 -1을 저장
	                    len = is.read(buf);
	                    //읽은 데이터가 없으면 읽기 종료
	                    
	                    if(len<=0)
	                       break;
	                    //읽은 데이터가 있으면 buf에서 0부터 len만큼 fos에 저장
	                        
	                    fos.write(buf,0,len);
	                }
	                    
	            }while(rs.next()); // 해당 위치까지 blob 이미지 변환
	        }
	        else{//없을때
	            System.out.println("데이터가 없습니다.");
	        }
	            
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }
	    finally {
	            //사용한 객체 close
	            try {
	                if(rs != null) rs.close();
	                if(con != null) con.close();
	                if(stmt != null) stmt.close();
	                    
	            } catch (Exception e) {
	                    
	            }
	            
	    }   
	    Image img = null;
	    Image resize_img = null;
	    try {
			img = ImageIO.read(new File(imagepath + imagename));
			resize_img = img.getScaledInstance(100,  100, Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(resize_img);
			MenuJTabaleExam.imageLabel.setIcon(icon); //사진 위치에 사진 삽입
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 이미지 불러옴

	}
}// 클래스끝


