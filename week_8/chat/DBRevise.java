package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import chat.management.UserDefaultJTableDAO;
import chat.management.UserJDailogGUI;

public class DBRevise extends JFrame {

	//DB내 정보 회원 정보 가져와서 임시로 저장하는 변수
	String userid;
	String pw, name, email, phone, zipcode, image, gender, birth;

	protected static final int String = 0;
	private JPanel contentPane;
	private JPanel birthPanel;
	public static JLabel lblJoin;
	private JButton userrevise_CompleteBtn;
	private JButton userrev_pwBtn;
	private JButton userrev_idCkBtn;
	private JButton userrev_backBtn;
	private JButton userrev_imageBtn;
	private JButton userrev_birthBtn;
	public JTextField userrev_tfID;
	public static JTextField userrev_tfZipCode;
	public static JPasswordField userrev_tfPassword;
	public JTextField userrev_tfName;
	public JRadioButton userrev_tfMan; //남, 여
	private JRadioButton userrev_tfWoman;
	public static JTextField userrev_tfBirth;
	private JTextField userrev_tfEmail1;
	private JTextField userrev_tfEmail2;
	private JTextField userrev_tfPhone1;
	private JTextField userrev_tfPhone2;
	private JTextField userrev_tfPhone3;			     
	public static JTextField userrev_tfImage;
	public static JLabel userrev_lblSecStrength;
	DBConnection db = new DBConnection();
	
	public static String userrev_titleName = "회원수정";
	public static String userrev_JoinName = "회원수정";
	public static String userrev_zipcode;
	
	public static String userrev_wholePhone;
	public static String userrev_wholeEmail;
	public static String userrev_combineEmail;
	String phoneNumber;	
	char genderSelect;
	
	public static File userrev_file;// 사진의 실제 연결되는 경로
	
	public static String userrev_img_path = ""; // 사진의 경로를 저장
	public static String userrev_img_name = ""; // 사진의 이름을 저장
		
	//이미지 아이콘 추가
	ImageIcon sign_bg1; //원본 이미지
	Image mod_bg1; // 이미지아이콘(Imageicon)을 이미지(Image)로 변환
	Image mod_bg2; // 이미지 크기 조정
	ImageIcon sign_bg2; // 조정 이미지(Image)를 이미지아이콘(Imageicon)으로 재변환
	public static String userrev_Joinbgpath = "C:/Temp/defaultbackground.jpg";

	JButton okBtn;

	MsgeBox msgbox = new MsgeBox();

	
	PreparedStatement ps = null;
	Statement stmt = null;
	ResultSet rs = null;
	String url = "jdbc:oracle:thin:@localhost:1521:orcl"; // 오라클 포트번호1521/@이후에는 IP주소
	String sql = null;
	String sql2 = null;
	Properties info = null;
	Connection cnn = null;

	public DBRevise(String id){
		
		userid = id;
		myInfo();
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(490, 850);
		setLocationRelativeTo(null);
		Member JoinInterfer1 = new Member();
		if (JoinInterfer1.getBgpath() == null) {
			JoinInterfer1.setBgpath(userrev_Joinbgpath);
			System.out.println(JoinInterfer1.getBgpath());
		}
		//이미지 추가
		sign_bg1 = new ImageIcon(JoinInterfer1.getBgpath());
		mod_bg1 = sign_bg1.getImage();
		mod_bg2 = mod_bg1.getScaledInstance(545, 811, Image.SCALE_SMOOTH);
		sign_bg2 = new ImageIcon(mod_bg2);
		contentPane = new JPanel(){
			public void paintComponent(Graphics g) {
				g.drawImage(sign_bg2.getImage(), 0, 0, null);
	            setOpaque(false); //그림을 표시하게 설정,투명하게 조절
	            super.paintComponent(g);
	        }
	    };
	    
		if (JoinInterfer1.getJointitle1()  == null) {
			JoinInterfer1.setJointitle1(userrev_titleName);
			System.out.println(JoinInterfer1.getJointitle1());
		}
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle(JoinInterfer1.getJointitle1()); // 타이틀 이름
		
		if (JoinInterfer1.getJointitle2() == null) {
			JoinInterfer1.setJointitle2(userrev_JoinName);
			System.out.println(JoinInterfer1.getJointitle2());
		}
		lblJoin = new JLabel(JoinInterfer1.getJointitle2());
		Font f1 = new Font("돋움", Font.BOLD, 20); //궁서 바탕 돋움
		lblJoin.setFont(f1); 
		lblJoin.setBounds(200, 41, 101, 20);
		contentPane.add(lblJoin);
					
		JLabel lblpassword = new JLabel("비밀번호");
		lblpassword.setBounds(29, 164, 69, 20);
		contentPane.add(lblpassword);
				
		JLabel lblID = new JLabel("아이디");
		lblID.setBounds(29, 117, 69, 20);
		contentPane.add(lblID);
		
		userrev_lblSecStrength = new JLabel(""); //임시 문자열 '보안강도'를 적어서 어디에 표시되는지 확인함
		userrev_lblSecStrength.setBounds(29, 200, 370, 40);
		contentPane.add(userrev_lblSecStrength);
		
		userrev_lblSecStrength.setFont(new Font("DIALOG", Font.BOLD, 14));
		
		JLabel lblZipCode = new JLabel("우편번호");
		lblZipCode.setBounds(29, 415, 69, 20);
		contentPane.add(lblZipCode);
		
		JLabel lblName = new JLabel("이름");
		lblName.setBounds(29, 262, 69, 20);
		contentPane.add(lblName);
		
		JLabel lblGender = new JLabel("성별");
		lblGender.setBounds(29, 313, 69, 20);
		contentPane.add(lblGender);
		
		JLabel lblBirth = new JLabel("생일");
		lblBirth.setBounds(29, 364, 69, 20);
		contentPane.add(lblBirth);
		
		JLabel lblEmail = new JLabel("이메일");
		lblEmail.setBounds(29, 466, 69, 20);
		contentPane.add(lblEmail);
		
		JLabel lblPhone = new JLabel("전화번호");
		lblPhone.setBounds(29, 517, 69, 20);
		contentPane.add(lblPhone);
		
		JLabel lblImage = new JLabel("프로필사진");
		lblImage.setBounds(29, 568, 69, 20);
		contentPane.add(lblImage);
		
		JLabel imgLabel1 = new JLabel("사진위치");
		imgLabel1.setBounds(29, 621, 100, 100);
		contentPane.add(imgLabel1);
		
		JLabel hyphen1 = new JLabel("-");
		hyphen1.setBounds(226, 513, 17, 22);
		contentPane.add(hyphen1);
		
		JLabel hyphen2 = new JLabel("-");
		hyphen2.setBounds(322, 513, 17, 22);
		contentPane.add(hyphen2);
		
		userrev_tfID = new JTextField();
		userrev_tfID.setColumns(15);
		userrev_tfID.setBounds(145, 107, 147, 35);
		contentPane.add(userrev_tfID);
		
		userrev_tfPassword = new JPasswordField();
		userrev_tfPassword.setEchoChar('*');
		userrev_tfPassword.setColumns(15);
		userrev_tfPassword.setBounds(145, 157, 147, 35);
		contentPane.add(userrev_tfPassword);
		
		userrev_tfZipCode = new JTextField();
		userrev_tfZipCode.setColumns(30);
		userrev_tfZipCode.setBounds(145, 407, 186, 35);
		contentPane.add(userrev_tfZipCode);
		
		userrev_tfName = new JTextField();
		userrev_tfName.setColumns(10);
		userrev_tfName.setBounds(145, 257, 186, 35);
		contentPane.add(userrev_tfName);
		
		userrev_tfMan = new JRadioButton("남");
		userrev_tfWoman = new JRadioButton("여");
		ButtonGroup group = new ButtonGroup();
		group.add(userrev_tfMan);
		group.add(userrev_tfWoman);
		userrev_tfMan.setBounds(145, 307, 70, 35);
		userrev_tfWoman.setBounds(215, 307, 70, 35);
		contentPane.add(userrev_tfMan);
		contentPane.add(userrev_tfWoman);
		
		userrev_tfBirth = new JTextField();
		userrev_tfBirth.setColumns(10);
		userrev_tfBirth.setBounds(145, 357, 147, 35);
		contentPane.add(userrev_tfBirth);
		
		userrev_tfEmail1 = new JTextField(); //이메일 앞단
		userrev_tfEmail1.setColumns(15);
		userrev_tfEmail1.setBounds(145, 457, 87, 35);
		contentPane.add(userrev_tfEmail1);
		
		userrev_tfEmail2 = new JTextField(); //이메일 뒷단
		userrev_tfEmail2.setColumns(15);
		userrev_tfEmail2.setBounds(244, 457, 87, 35);
		contentPane.add(userrev_tfEmail2);
				
		userrev_tfPhone1 = new JTextField();
		userrev_tfPhone1.setColumns(10);
		userrev_tfPhone1.setBounds(145, 507, 69, 35);
		contentPane.add(userrev_tfPhone1);

		userrev_tfPhone2 = new JTextField();
		userrev_tfPhone2.setColumns(10);
		userrev_tfPhone2.setBounds(244, 507, 69, 35);
		contentPane.add(userrev_tfPhone2);

		userrev_tfPhone3 = new JTextField();
		userrev_tfPhone3.setColumns(10);
		userrev_tfPhone3.setBounds(339, 507, 69, 35);
		contentPane.add(userrev_tfPhone3);
		
		userrev_tfImage = new JTextField();
		userrev_tfImage.setColumns(10);
		userrev_tfImage.setBounds(145, 557, 147, 35); 
		contentPane.add(userrev_tfImage);
		
		userrev_idCkBtn = new JButton("아이디 확인");
		userrev_idCkBtn.setBounds(308, 107, 118, 35);
		contentPane.add(userrev_idCkBtn);
		
		userrev_pwBtn = new JButton("비밀번호 확인");
		userrev_pwBtn.setBounds(308, 157, 118, 35);
		contentPane.add(userrev_pwBtn);
				
		userrev_birthBtn = new JButton("날짜 검색");
		userrev_birthBtn.setBounds(308, 357, 118, 35);
		contentPane.add(userrev_birthBtn);
		
		userrev_imageBtn = new JButton("이미지 검색");
		userrev_imageBtn.setBounds(308, 557, 118, 35);
		contentPane.add(userrev_imageBtn);
		
		userrevise_CompleteBtn = new JButton("회원 수정");
		userrevise_CompleteBtn.setBounds(246, 772, 139, 29);
		contentPane.add(userrevise_CompleteBtn);
		
		userrev_backBtn = new JButton("취소");
		userrev_backBtn.setBounds(95, 772, 139, 29);
		contentPane.add(userrev_backBtn);
/*		
		zipButton = new JButton("우편입력");
		zipButton.setBounds(95, 662, 139, 29);
		contentPane.add(zipButton);
*/		
		String emailType[] = {"naver.com", "nate.com", "hanmail.net", "gmail.com", 
				"paran.com", "empal.com", "hotmail.com", "hanmir.com", "직접입력"};
		JComboBox comboBox = new JComboBox(emailType);
		comboBox.setBounds(339, 457, 87, 34);
		contentPane.add(comboBox);
					
		setVisible(true);
		
		// 회원가입 내 취소버튼 등록
		userrev_backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					dispose();
			}
		});
		
		// 생일 검색 버튼 등록
		userrev_birthBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("날짜 검색 버튼 클릭.");
				Birth birth = new Birth();
			}
		});
		
		
		// 우편번호 삽입해주는 버튼
/*		zipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {			
				tfZipCode.setText(JoinInterfer1.getZipcode());
			}
		});
*/		

		// 이메일 선택 콤보박스 등록
		comboBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!(comboBox.getSelectedItem().toString()).equals("직접입력")) {
					userrev_tfEmail2.setText("@" + comboBox.getSelectedItem().toString());
					userrev_tfEmail2.requestFocus();
				} else {
					userrev_tfEmail2.setText("@");
					userrev_tfEmail2.requestFocus();
				}			
				System.out.println(userrev_tfEmail1.getText() + userrev_tfEmail2.getText());
			}
		});
		
		// 이미지 버튼 등록
		userrev_imageBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("프로필 사진 검색 버튼 클릭."); //이미지 검색 버튼 클릭시 출력
				JFileChooser chooser = new JFileChooser(); //파일 탐색창 생성
				int returnVal = chooser.showOpenDialog(DBRevise.this); //파일 탐색창 출력
				if(returnVal == JFileChooser.APPROVE_OPTION) { //파일 탐색창을 열고 확인 버튼을 눌렀는지 확인
					userrev_file = chooser.getSelectedFile(); //실제 이미지 파일 경로(File)
					userrev_img_name = chooser.getSelectedFile().getName(); //이미지 명(String)
					userrev_img_path = chooser.getSelectedFile().getPath(); //이미지 경로(String)
					try {
						//선택한 이미지를 라벨의 아이콘으로 설정
						ImageIcon icon = new ImageIcon(ImageIO.read(userrev_file));
						//아이콘에서 이미지를 리턴받음
						Image imageSrc = icon.getImage();
						//이미지 resize(x:100 y:100으로 설정)
						Image imageNew = imageSrc.getScaledInstance(100,  100,  Image.SCALE_AREA_AVERAGING);
						//아이콘 변경
						icon = new ImageIcon(imageNew);
						imgLabel1.setIcon(icon); //사진 위치에 사진 삽입
					}catch(Exception e2) {
						e2.printStackTrace();
					}
					userrev_tfImage.setText(userrev_img_path); //이미지 경로를 프로필 검색창에 입력해줌
					System.out.println(userrev_img_name); //이미지명 콘솔창에 출력
					System.out.println(userrev_img_path); //파일경로 콘솔창에 출력					
				}
			}
		});
		
		//보안강도 메소드
		userrev_tfPassword.getDocument().addDocumentListener(new DocumentListener() {
			String resultmsg;
        	UserDefaultJTableDAO mdao = new UserDefaultJTableDAO();
            @Override
            public void insertUpdate(DocumentEvent e) {
            	resultmsg = userrev_getPwSecStrength(userrev_tfPassword.getText());
                updateLabel();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            	resultmsg = userrev_getPwSecStrength(userrev_tfPassword.getText());
                updateLabel();
            }

            protected void updateLabel() {
            	userrev_lblSecStrength.setText(resultmsg);
            }

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
		
		//비밀번호 중복, 규칙 설정 이벤트 등록
		userrev_pwBtn.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				MemberDAO mdao = new MemberDAO();				
				if(userrev_tfPassword.getText().trim().equals("")) { // 빈 입력창 상태로 버튼 클릭시
					JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요.");
					userrev_tfPassword.requestFocus(); // 비밀번호 입력창으로 포커스 이동
				}else { // 비밀번호 입력 후 버튼 클릭시
					if(mdao.getPwPatternCheck(userrev_tfPassword.getText())) { // 우선적으로 비밀번호 규칙 확인
						JOptionPane.showMessageDialog(null, "최소 9~20글자, 대문자 1개, 소문자 1개, 특수문자 1개로 구성해야됩니다.");
						userrev_tfPassword.setText(""); // 비밀번호 입력창에 입력한 정보 지움
						userrev_tfPassword.requestFocus(); // 비밀번호 입력창에 포커스를 옮김 
					}
					else if(mdao.getPwByCheck(userrev_tfPassword.getText())) { // 규칙 확인 이후 중복 확인
						JOptionPane.showMessageDialog(null, "사용 가능한 비밀번호입니다.");
					}else {
						JOptionPane.showMessageDialog(null, "해당 비밀번호는 중복된 비밀번호입니다.");
						userrev_tfPassword.setText(""); // 비밀번호 입력창에 입력한 정보 지움
						userrev_tfPassword.requestFocus(); // 비밀번호 입력창에 포커스 옮김
					}
					
				}
			}
		});
		
		// ID중복체크 이벤트 등록
		userrev_idCkBtn.addActionListener(new ActionListener() { 			
			@Override
			public void actionPerformed(ActionEvent e) {
				MemberDAO mdao = new MemberDAO();
				//id 텍스트박스에 값이 없을 경우 메시지 출력, 있으면 DB에 연동한다.
				if(userrev_tfID.getText().trim().equals("")){
					JOptionPane.showMessageDialog(null,"아이디를 입력해주세요."); // 빈 입력창 상태로 중복 확인시
					userrev_tfID.requestFocus(); // 아이디 입력창으로 포커스 이동
				} else if(userrev_tfID.getText().length()<6 || userrev_tfID.getText().length()>15) { //6글자 미만 15글자 초과시
					JOptionPane.showMessageDialog(null, "아이디는 6글자 이상 15글자 이하로 입력해야됩니다."); //아이디 생성 불가능 메시지
					userrev_tfID.setText(""); //아이디 입력창 초기화
					userrev_tfID.requestFocus(); //아이디 입력창 포커스 이동
				}
				else {
					if(mdao.getIdByCheck(userrev_tfID.getText())) { //중복이 아닐 경우(사용 가능한 경우)
						JOptionPane.showMessageDialog(null, "사용 가능한 아이디입니다.");
					}else { //중복일 경우
						JOptionPane.showMessageDialog(null, "해당 아이디는 중복된 아이디입니다.");
							
						userrev_tfID.setText(""); // 아이디 입력창에 입력한 정보 지움
						userrev_tfID.requestFocus(); // 아이디 입력창에 포커스를 옮김
					}
				}
			}
		});
		// 회원 수정 이벤트 등록
		userrevise_CompleteBtn.addActionListener(new ActionListener() { 	
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					phoneNumber = userrev_tfPhone1.getText() + userrev_tfPhone2.getText() + userrev_tfPhone3.getText();
					userrev_combineEmail = userrev_tfEmail1.getText() + userrev_tfEmail2.getText();
					
					String sql = "UPDATE member SET password=?, name=?, email=?, phone=?, zipcode=?, imagename=?, image=?, createdate=sysdate, gender=?, birth=? WHERE id=?";
					
					ps = cnn.prepareStatement(sql);
					// ?의 순서대로 입력된 값 넣기
					ps.setString(1, userrev_tfPassword.getText());
					ps.setString(2, userrev_tfName.getText());
					ps.setString(3, userrev_combineEmail);
					ps.setString(4, phoneNumber);
					ps.setString(5, userrev_tfZipCode.getText());
					ps.setString(6, userrev_img_name);
					File f = new File(userrev_tfImage.getText());
					FileInputStream fis = new FileInputStream(f); //파일을 byte단위로 읽어 오는 클래스
					ps.setBinaryStream(7, fis, (int)f.length()); //이미지를 blob 형태로 저장
					if (userrev_tfMan.isSelected()) { //남자 버튼이 선택됬다면
						ps.setString(8, "M"); //성별 컬럼을 M(남자)으로 업데이트
					} else {
						ps.setString(8, "W"); //성별 컬럼을 W(여자)로 업데이트
					}
					ps.setString(9, userrev_tfBirth.getText());
					ps.setString(10, userid);
					
					try {
						ps.executeUpdate(); //sql문 실행
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "회원수정완료되었습니다.");
					dispose(); // 창 닫기
					dbClose();
				} catch (Exception ee) {
					System.out.println("문제있음");
					ee.printStackTrace();
				}
			}
		});

		//text박스에 선택된 레코드의 정보 넣기
		userrev_tfID.setText(userid);
		userrev_tfPassword.setText(pw);
		userrev_tfName.setText(name);
			
		String frontEmail = userrev_wholeEmail.split("@")[0];
		String backEmail = userrev_wholeEmail.split("@")[1];
		userrev_tfEmail1.setText(frontEmail);
		userrev_tfEmail2.setText("@" + backEmail);
		
		char[] digits1 = userrev_wholePhone.toCharArray();
		String combinePhone1 = new StringBuilder().append(digits1[0]).append(digits1[1]).append(digits1[2]).toString();
		String combinePhone2 = new StringBuilder().append(digits1[3]).append(digits1[4]).append(digits1[5]).append(digits1[6]).toString();
		String combinePhone3 = new StringBuilder().append(digits1[7]).append(digits1[8]).append(digits1[9]).append(digits1[10]).toString();
		userrev_tfPhone1.setText(combinePhone1);
		userrev_tfPhone2.setText(combinePhone2);
		userrev_tfPhone3.setText(combinePhone3);
		userrev_tfZipCode.setText(zipcode);
		userrev_tfImage.setText(image);
		if(gender.toString().equals("M")) {
			userrev_tfMan.setSelected(true);
		}else {
			userrev_tfWoman.setSelected(true);
		}
		userrev_tfBirth.setText(birth);
			
		//id text박스 비활성
		userrev_tfID.setEditable(false);
		
		//우측상단 x버튼 누르면 수정창만 나가짐
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
		
		//이벤트등록    
        setLocationRelativeTo(null);
    	
	}
	// id를 받아와서 그것의 정보로 pw/name/barth 수정및 삭제
	void myInfo() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); // 알아서 conn으로 연결
			cnn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "c##scott", "tiger");
			stmt = cnn.createStatement();

			sql = "select * from member where id=\'" + userid + "\'";
			rs = stmt.executeQuery(sql);

			while (rs.next() == true) { // 다음값의
				pw = rs.getString(2);
				System.out.println(pw);
				name = rs.getString(3);
				System.out.println(name);
				userrev_wholeEmail = rs.getString(4);
				System.out.println(userrev_wholeEmail);
				userrev_wholePhone = rs.getString(5);
				zipcode = rs.getString(6);
				image = rs.getString(7);
				gender = rs.getString(10);
				birth = rs.getString(11);
			}
		} catch (Exception ee) {
			System.out.println("문제있음");
			ee.printStackTrace();
		}		
	}
	
	//비밀번호 보안 강도 메소드
	 public String userrev_getPwSecStrength(String pw) {
		 MemberDAO mdao = new MemberDAO();
		 Color color1 = new Color(0xFF8200); //보안강도 중 색상(주황색)
		 Color color2 = new Color(0x52E252); //보안강도 상 색상(초록색)
		 String result = null; //보안강도 텍스트를 보낼 필드
		 /* [A-Za-z\\d@$!%*?&]{9,15}: 대괄호 범위의 문자로 최소9~최대15문자로 구성 
		  * .*[a-z]: 영문 소문자 하나 또는 하나 이상 
		  * .*[A-Z]: 영문 대문자 하나 또는 하나 이상 
		  * .*[@$!%*?&]: 특수문자 하나 또는 하나 이상 */
		 if (pw.length() < 9) {
			 result = "<HTML>※최소 9~15자, 대문자 1, 소문자 1, 특수문자 1개<br>(@$!%*?&)로 구성해야됩니다.</HTML>";
			 userrev_lblSecStrength.setForeground(Color.red); //보안강도 텍스트를 빨간색으로 변경
		 }
		 if (pw.length() >=9 && pw.length() <=12) {
			 Pattern passPattern1 = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{9,20}$");
			 Matcher matcher1 = passPattern1.matcher(pw); //패턴 해석 및 입력 문자열에 대한 일치 작업 수행 엔진
			 if(!matcher1.find()) { //규칙에 맞게 비밀번호 생성시 find()메소드가 true 출력함
				 //규칙 중 하나라도 부합하지 못한 경우 find()메소드가 false인 경우
				 result = "<HTML>※최소 9~15자, 대문자 1, 소문자 1, 특수문자 1개<br>(@$!%*?&)로 구성해야됩니다.</HTML>";  
			 } else if(!mdao.getPwByCheck(userrev_tfPassword.getText())) {
				 result = "중복된 비밀번호입니다.";
			 } else {
				 result = "※보안강도 하! 현재 글자수: " + pw.length(); //규칙을 전부 통과하면 보안강도 표시
			 }
			 userrev_lblSecStrength.setForeground(Color.red);
		 } else if (pw.length() >=13 && pw.length() <= 16) {
			 Pattern passPattern1 = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{9,20}$");
			 Matcher matcher1 = passPattern1.matcher(pw);
			 if(!matcher1.find()) { //규칙에 맞게 비밀번호 생성시 find()메소드가 true 출력함
				 //규칙 중 하나라도 부합하지 못한 경우 find()메소드가 false인 경우
				 result = "<HTML>※최소 9~15자, 대문자 1, 소문자 1, 특수문자 1개<br>(@$!%*?&)로 구성해야됩니다.</HTML>";  
			 } else if(!mdao.getPwByCheck(userrev_tfPassword.getText())) {
				 result = "중복된 비밀번호입니다.";
			 } else {
				 result = "※보안강도 중! 현재 글자수: " + pw.length(); //규칙을 전부 통과하면 보안강도 표시
				 userrev_lblSecStrength.setForeground(color1); //보안강도 텍스트를 주황색으로 변경
			 }		
		 } else if (pw.length() >=17 && pw.length() <=20) {
			 Pattern passPattern1 = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{9,20}$");
			 Matcher matcher1 = passPattern1.matcher(pw);
			 if(!matcher1.find()) { //규칙에 맞게 비밀번호 생성시 find()메소드가 true 출력함
				 //규칙 중 하나라도 부합하지 못한 경우 find()메소드가 false인 경우
				 result = "<HTML>※최소 9~15자, 대문자 1, 소문자 1, 특수문자 1개<br>(@$!%*?&)로 구성해야됩니다.</HTML>";  
			 } else if(!mdao.getPwByCheck(userrev_tfPassword.getText())) {
				 result = "중복된 비밀번호입니다.";
			 } else {
				 result = "※보안강도 상! 현재 글자수: " + pw.length(); //규칙을 전부 통과하면 보안강도 표시
				 userrev_lblSecStrength.setForeground(color2); //보안강도 텍스트를 초록색으로 변경
			 }			
		 } else if (pw.length() > 20) { //20자 이상 초과시
			result = "※20자 이상 초과했습니다!";
			userrev_lblSecStrength.setForeground(Color.red);			 
		 }

		 return result;
	 }
	 
	public void dbClose() {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (cnn != null)
				cnn.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
