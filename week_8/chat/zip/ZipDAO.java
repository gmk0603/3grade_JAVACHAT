package chat.zip;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

public class ZipDAO {
	Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;
   
   
    // 데이터베이스 연결
    public void connection() {
    	try {
                  Class.forName("oracle.jdbc.driver.OracleDriver");
                  conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "c##scott", "tiger");
         	} catch (ClassNotFoundException e) {
         		
            } catch (SQLException e) {
            	
            }
    }
   
    // 데이터베이스 연결종료
    public void disconnection() {
    	try {
    		if(pstmt != null) pstmt.close();
                      
    		if(rs != null) rs.close();
                      
    		if(conn != null) conn.close();
             
    	} catch (SQLException e) {
    		
    	}
    }

    // 시도데이터=============================================
    public ArrayList<ZipDTO> searchSido() {
             ArrayList<ZipDTO> sidoList = new ArrayList<ZipDTO>();
             try {
                      String query = "select distinct(sido) from zipcodes order by sido";
                      pstmt = conn.prepareStatement(query);
                      rs = pstmt.executeQuery();
                      while(rs.next()){
                              ZipDTO zipcode = new ZipDTO();
                              zipcode.setSido(rs.getString("SIDO"));
                              sidoList.add(zipcode);
                      }
             } catch (SQLException e) {
             }

             return sidoList;
    }
   
    // 구군데이터=============================================
    public ArrayList<ZipDTO> searchGugun(String sido) {
             ArrayList<ZipDTO> gugunList = new ArrayList<>();
            
             try {
                      String query = "select distinct(gugun) from zipcodes where sido = \'" + sido + "\' order by gugun";
                      pstmt = conn.prepareStatement(query);
                      rs = pstmt.executeQuery();
                      while(rs.next()){
                              ZipDTO zipcode = new ZipDTO();
                              zipcode.setGugun(rs.getString("GUGUN"));
                              gugunList.add(zipcode);
                      }
             } catch (SQLException e) {
             }
                             
             return gugunList;         
    }

    // 동데이터=============================================
    public ArrayList<ZipDTO> searchDong(String sido, String gugun) {
             ArrayList<ZipDTO> dongList = new ArrayList<>();

             try {
                      String query = "select distinct(dong) from zipcodes where sido = \'" + sido + "\' and gugun = \'" + gugun + "\' order by dong";
                      pstmt = conn.prepareStatement(query);
                      rs = pstmt.executeQuery();
                      while(rs.next()){
                              ZipDTO zipcode = new ZipDTO();
                              zipcode.setDong(rs.getString("DONG"));
                              dongList.add(zipcode);
                      }
             } catch (SQLException e) {
             }

             return dongList;          
    }

    // 전부주소 데이터 =============================================
    public ArrayList<ZipDTO> searchAddress(String sido, String gugun, String dong) {

    	ArrayList<ZipDTO> addressList = new ArrayList<>();

    	try {
    		String query = "select * from zipcodes where sido = \'" + sido + "\' and gugun = \'" + gugun + "\' and dong = \'" + dong +"\'";

    		pstmt = conn.prepareStatement(query);

    		rs = pstmt.executeQuery();

    		while(rs.next()){

    			ZipDTO zipcode = new ZipDTO();
    			
    			zipcode.setSeq(rs.getString("seq"));
                zipcode.setZipcode(rs.getString("zipcode"));
                zipcode.setSido(rs.getString("sido"));
	            zipcode.setGugun(rs.getString("gugun"));
	            zipcode.setDong(rs.getString("dong"));
	            zipcode.setRi(rs.getString("ri"));
	            zipcode.setBldg(rs.getString("bldg"));
	            zipcode.setBunji(rs.getString("bunji"));
	            
	            addressList.add(zipcode);
    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
             
    	return addressList;               
    }

	public ArrayList<ZipDTO> searchKeyDong(String dongName) { //동이름 검색 기능
		
		ArrayList<ZipDTO> addressList = new ArrayList<>();

    	try {
    		String query = "select * from zipcodes where dong like \'%" + dongName + "%\'";

    		pstmt = conn.prepareStatement(query);

    		rs = pstmt.executeQuery();

    		while(rs.next()){

    			ZipDTO zipcode = new ZipDTO();
    			
    			zipcode.setSeq(rs.getString("seq"));
                zipcode.setZipcode(rs.getString("zipcode"));
                zipcode.setSido(rs.getString("sido"));
	            zipcode.setGugun(rs.getString("gugun"));
	            zipcode.setDong(rs.getString("dong"));
	            zipcode.setRi(rs.getString("ri"));
	            zipcode.setBldg(rs.getString("bldg"));
	            zipcode.setBunji(rs.getString("bunji"));
	            
	            addressList.add(zipcode);
    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
             
    	return addressList;  
	}	
}
