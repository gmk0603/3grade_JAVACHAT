package chat;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
 
public class Imageload extends JFrame implements ActionListener {
	public static String imagename; // 이미지 파일명
	public static String imagepath = "C:/Users/USER/Downloads/"; // db에서 가져온 이미지 저장 경로
	public static String inputId; 	
    public static void main(String[] args) {
    	String url="jdbc:oracle:thin:@localhost:1521:orcl";
    	String id="c##scott";
    	String pw="tiger";
    	String driver="oracle.jdbc.driver.OracleDriver";
        try {
            Class.forName(driver);
            inputID();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null; //select 의 결과를 저장
        
        try {
            con = DriverManager.getConnection(url, id, pw);
            //test1 테이블의 모든 데이터 읽어오는 sql
            stmt = con.prepareStatement("select * from member where id=?"); // 자신의 id에 대한 blob 이미지 파일 및 이미지 이름 가져오기
            stmt.setString(1, inputId.trim()); // 입력받은 id를 이용해 select문 검색
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
        new Imageload();
    }
    
    Image img = null;
    Image resize_img = null;
    
    public Imageload() {
    	setTitle("blob 이미지 출력 테스트");
    	JLayeredPane layeredPane = new JLayeredPane();
    	layeredPane.setSize(400, 400); // 최하층 패널
    	layeredPane.setLayout(null);
    	
    	try {
    		img = ImageIO.read(new File(imagepath + imagename)); // 이미지 불러옴
    		resize_img = img.getScaledInstance(100,  100, Image.SCALE_SMOOTH); // 이미지의 사이즈를 조절함
    	} catch (IOException e) {
    		JOptionPane.showMessageDialog(null, "이미지 불러오기 실패");
    		System.exit(0);
    	}
    	//이미지 삽입
    	myPanel panel = new myPanel();
    	panel.setSize(400, 400); // 이미지 패널
    	layeredPane.add(panel);
    	
    	setLayout(null);
    	
    	add(layeredPane); // 이미지 패널 삽입
    	
    	setBounds(100, 100, 700, 700); // 출력 창 자체 위치 및 크기 설정
    	setVisible(true);
    	setResizable(false);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public static void inputID() {
    	inputId = JOptionPane.showInputDialog(null,"아이디를 입력하세요"); // 자신의 아이디를 입력해 자신의 이미지를 db에서 가져온 후 창에 출력함
    }
    
    class myPanel extends JPanel {
    	public void paint(Graphics g) {
    		g.drawImage(resize_img, 300, 300, null); // 이미지 패널 내 삽입 위치
    	}
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	
    }
}