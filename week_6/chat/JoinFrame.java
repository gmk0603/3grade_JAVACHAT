package chat;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;

import chat.MemberDAO;
import chat.zip.ZipSearch;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import chat.Member;

public class JoinFrame extends JFrame {

	protected static final int String = 0;
	private JPanel contentPane;
	private JPanel birthPanel;
	public static JLabel lblJoin;
	private JButton joinCompleteBtn;
	private JButton pwBtn;
	private JButton idCkBtn;
	private JButton zipBtn;
	private JButton backBtn;
	private JButton imageBtn;
	private JButton birthBtn;
	private JTextField tfID;
	public static JTextField tfZipCode;
	public static JPasswordField tfPassword;
	private JTextField tfName;
	private JRadioButton tfMan, tfWoman; //남, 여
	public static JTextField tfBirth;
	private JTextField tfEmail1;
	private JTextField tfEmail2;
	private JTextField tfPhone1;
	private JTextField tfPhone2;
	private JTextField tfPhone3;			     
	public static JTextField tfImage;
	public static JLabel lblSecStrength;
	DBConnection db = new DBConnection();
	
	public static String titleName = "회원가입";
	public static String JoinName = "회원가입";
	public static String zipcode;
	
	private JButton zipButton;
	
	public static File file;// 사진의 실제 연결되는 경로
	
	public static String img_path = ""; // 사진의 경로를 저장
	public static String img_name = ""; // 사진의 이름을 저장
		
	//이미지 아이콘 추가
	ImageIcon sign_bg1; //원본 이미지
	Image mod_bg1; // 이미지아이콘(Imageicon)을 이미지(Image)로 변환
	Image mod_bg2; // 이미지 크기 조정
	ImageIcon sign_bg2; // 조정 이미지(Image)를 이미지아이콘(Imageicon)으로 재변환
	public static String Joinbgpath = "C:/Users/USER/Desktop/testimage.jpg";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JoinFrame frame = new JoinFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JoinFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(490, 850);
		setLocationRelativeTo(null);
		Member JoinInterfer1 = new Member();
		if (JoinInterfer1.getBgpath() == null) {
			JoinInterfer1.setBgpath(Joinbgpath);
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
			JoinInterfer1.setJointitle1(titleName);
			System.out.println(JoinInterfer1.getJointitle1());
		}
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle(JoinInterfer1.getJointitle1()); // 타이틀 이름
		
		if (JoinInterfer1.getJointitle2() == null) {
			JoinInterfer1.setJointitle2(JoinName);
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
		
		lblSecStrength = new JLabel(""); //임시 문자열 '보안강도'를 적어서 어디에 표시되는지 확인함
		lblSecStrength.setBounds(29, 200, 370, 40);
		contentPane.add(lblSecStrength);
		
		lblSecStrength.setFont(new Font("DIALOG", Font.BOLD, 14));
		
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
		
		tfID = new JTextField();
		tfID.setColumns(15);
		tfID.setBounds(145, 107, 147, 35);
		contentPane.add(tfID);
		
		tfPassword = new JPasswordField();
		tfPassword.setEchoChar('*');
		tfPassword.setColumns(15);
		tfPassword.setBounds(145, 157, 147, 35);
		contentPane.add(tfPassword);
		
		tfZipCode = new JTextField();
		tfZipCode.setColumns(30);
		tfZipCode.setBounds(145, 407, 147, 35);
		contentPane.add(tfZipCode);
		
		tfName = new JTextField();
		tfName.setColumns(10);
		tfName.setBounds(145, 257, 186, 35);
		contentPane.add(tfName);
		
		tfMan = new JRadioButton("남");
		tfWoman = new JRadioButton("여");
		ButtonGroup group = new ButtonGroup();
		group.add(tfMan);
		group.add(tfWoman);
		tfMan.setBounds(145, 307, 70, 35);
		tfWoman.setBounds(215, 307, 70, 35);
		contentPane.add(tfMan);
		contentPane.add(tfWoman);
		
		tfBirth = new JTextField();
		tfBirth.setColumns(10);
		tfBirth.setBounds(145, 357, 147, 35);
		contentPane.add(tfBirth);
		
		tfEmail1 = new JTextField(); //이메일 앞단
		tfEmail1.setColumns(15);
		tfEmail1.setBounds(145, 457, 87, 35);
		contentPane.add(tfEmail1);
		
		tfEmail2 = new JTextField(); //이메일 뒷단
		tfEmail2.setColumns(15);
		tfEmail2.setBounds(244, 457, 87, 35);
		contentPane.add(tfEmail2);
				
		tfPhone1 = new JTextField();
		tfPhone1.setColumns(10);
		tfPhone1.setBounds(145, 507, 69, 35);
		contentPane.add(tfPhone1);

		tfPhone2 = new JTextField();
		tfPhone2.setColumns(10);
		tfPhone2.setBounds(244, 507, 69, 35);
		contentPane.add(tfPhone2);

		tfPhone3 = new JTextField();
		tfPhone3.setColumns(10);
		tfPhone3.setBounds(339, 507, 69, 35);
		contentPane.add(tfPhone3);
		
		tfImage = new JTextField();
		tfImage.setColumns(10);
		tfImage.setBounds(145, 557, 147, 35); 
		contentPane.add(tfImage);
		
		idCkBtn = new JButton("아이디 확인");
		idCkBtn.setBounds(308, 107, 118, 35);
		contentPane.add(idCkBtn);
		
		pwBtn = new JButton("비밀번호 확인");
		pwBtn.setBounds(308, 157, 118, 35);
		contentPane.add(pwBtn);
				
		birthBtn = new JButton("날짜 검색");
		birthBtn.setBounds(308, 357, 118, 35);
		contentPane.add(birthBtn);
		
		zipBtn = new JButton("우편번호 검색");
		zipBtn.setBounds(308, 407, 118, 35);
		contentPane.add(zipBtn);
		
		imageBtn = new JButton("이미지 검색");
		imageBtn.setBounds(308, 557, 118, 35);
		contentPane.add(imageBtn);
		
		joinCompleteBtn = new JButton("회원가입완료");
		joinCompleteBtn.setBounds(246, 772, 139, 29);
		contentPane.add(joinCompleteBtn);
		
		backBtn = new JButton("취소");
		backBtn.setBounds(95, 772, 139, 29);
		contentPane.add(backBtn);
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
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					dispose();
			}
		});
		
		// 생일 검색 버튼 등록
		birthBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("날짜 검색 버튼 클릭.");
				Birth birth = new Birth();
			}
		});
		
		// 우편번호 검색 버튼 등록
		// 웹사이트 연결 코드
/*		
		zipBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("우편번호 검색 버튼 클릭.");
				String urlLink = "http://localhost:8080/ZIP_CODEE/zipaccess.jsp";
				try {
					Desktop.getDesktop().browse(new URI(urlLink));
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
*/


		zipBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("우편번호 검색 버튼 클릭."); //우편번호 검색 버튼 클릭시 출력
				ZipSearch frame = new ZipSearch(); //우편번호 검색 창 생성
				frame.setVisible(true); //우편번호 검색 창을 화면에 출력함
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
					tfEmail2.setText("@" + comboBox.getSelectedItem().toString());
					tfEmail2.requestFocus();
				} else {
					tfEmail2.setText("@");
					tfEmail2.requestFocus();
				}			
				System.out.println(tfEmail1.getText() + tfEmail2.getText());
			}
		});
		
		// 이미지 버튼 등록
		imageBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("프로필 사진 검색 버튼 클릭."); //이미지 검색 버튼 클릭시 출력
				JFileChooser chooser = new JFileChooser(); //파일 탐색창 생성
				int returnVal = chooser.showOpenDialog(JoinFrame.this); //파일 탐색창 출력
				if(returnVal == JFileChooser.APPROVE_OPTION) { //파일 탐색창을 열고 확인 버튼을 눌렀는지 확인
					file = chooser.getSelectedFile(); //실제 이미지 파일 경로(File)
					img_name = chooser.getSelectedFile().getName(); //이미지 명(String)
					img_path = chooser.getSelectedFile().getPath(); //이미지 경로(String)
					try {
						//선택한 이미지를 라벨의 아이콘으로 설정
						ImageIcon icon = new ImageIcon(ImageIO.read(file));
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
					tfImage.setText(img_path); //이미지 경로를 프로필 검색창에 입력해줌
					System.out.println(img_name); //이미지명 콘솔창에 출력
					System.out.println(img_path); //파일경로 콘솔창에 출력					
				}
			}
		});
		
		//보안강도 메소드
		tfPassword.getDocument().addDocumentListener(new DocumentListener() {
			String resultmsg;
        	MemberDAO mdao = new MemberDAO();
            @Override
            public void insertUpdate(DocumentEvent e) {
            	resultmsg = mdao.getPwSecStrength(tfPassword.getText());
                updateLabel();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            	resultmsg = mdao.getPwSecStrength(tfPassword.getText());
                updateLabel();
            }

            protected void updateLabel() {
            	lblSecStrength.setText(resultmsg);
            }

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
		
		//비밀번호 중복, 규칙 설정 이벤트 등록
		pwBtn.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				MemberDAO mdao = new MemberDAO();				
				if(tfPassword.getText().trim().equals("")) { // 빈 입력창 상태로 버튼 클릭시
					JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요.");
					tfPassword.requestFocus(); // 비밀번호 입력창으로 포커스 이동
				}else { // 비밀번호 입력 후 버튼 클릭시
					if(mdao.getPwPatternCheck(tfPassword.getText())) { // 우선적으로 비밀번호 규칙 확인
						JOptionPane.showMessageDialog(null, "최소 9~20글자, 대문자 1개, 소문자 1개, 특수문자 1개로 구성해야됩니다.");
						tfPassword.setText(""); // 비밀번호 입력창에 입력한 정보 지움
						tfPassword.requestFocus(); // 비밀번호 입력창에 포커스를 옮김 
					}
					else if(mdao.getPwByCheck(tfPassword.getText())) { // 규칙 확인 이후 중복 확인
						JOptionPane.showMessageDialog(null, "사용 가능한 비밀번호입니다.");
					}else {
						JOptionPane.showMessageDialog(null, "해당 비밀번호는 중복된 비밀번호입니다.");
						tfPassword.setText(""); // 비밀번호 입력창에 입력한 정보 지움
						tfPassword.requestFocus(); // 비밀번호 입력창에 포커스 옮김
					}
					
				}
			}
		});
		
		// ID중복체크 이벤트 등록
		idCkBtn.addActionListener(new ActionListener() { 			
			@Override
			public void actionPerformed(ActionEvent e) {
				MemberDAO mdao = new MemberDAO();
				//id 텍스트박스에 값이 없을 경우 메시지 출력, 있으면 DB에 연동한다.
				if(tfID.getText().trim().equals("")){
					JOptionPane.showMessageDialog(null,"아이디를 입력해주세요."); // 빈 입력창 상태로 중복 확인시
					tfID.requestFocus(); // 아이디 입력창으로 포커스 이동
				} else if(tfID.getText().length()<6 || tfID.getText().length()>15) { //6글자 미만 15글자 초과시
					JOptionPane.showMessageDialog(null, "아이디는 6글자 이상 15글자 이하로 입력해야됩니다."); //아이디 생성 불가능 메시지
					tfID.setText(""); //아이디 입력창 초기화
					tfID.requestFocus(); //아이디 입력창 포커스 이동
				}
				else {
					if(mdao.getIdByCheck(tfID.getText())) { //중복이 아닐 경우(사용 가능한 경우)
						JOptionPane.showMessageDialog(null, "사용 가능한 아이디입니다.");
					}else { //중복일 경우
						JOptionPane.showMessageDialog(null, "해당 아이디는 중복된 아이디입니다.");
							
						tfID.setText(""); // 아이디 입력창에 입력한 정보 지움
						tfID.requestFocus(); // 아이디 입력창에 포커스를 옮김
					}
				}
			}
		});
		
		//회원가입완료 액션
		joinCompleteBtn.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) { //회원가입 버튼 클릭 시
				String combineEmail = tfEmail1.getText() + tfEmail2.getText();
				String phoneNumber = tfPhone1.getText() + tfPhone2.getText() + tfPhone3.getText();
				Member member = new Member(); //멤버 클래스의 필드에 각각 입력한 정보를 저장
				member.setId(tfID.getText()); //getText()는 입력창의 정보를 가져오는 메소드
				member.setPassword(tfPassword.getText());
				member.setName(tfName.getText());
				//성별 선택 버튼 이벤트 등록
				if (tfMan.isSelected()) { //남자 라디오 버튼을 선택한 경우
					member.setGender("M"); //멤버 클래스의 성별 필드에 M(남자) 적용
				}
				else if (tfWoman.isSelected()) { //여자 라디오 버튼을 선택한 경우
					member.setGender("W"); //멤버 클래스의 성별 필드에 W(여자) 적용
				}
				member.setBirth(tfBirth.getText());
				member.setEmail(combineEmail);
				member.setPhone(phoneNumber);
				member.setZipcode(tfZipCode.getText());
				member.setImagename(img_name);
				member.setImagepath(tfImage.getText());
				MemberDAO dao = MemberDAO.getInstance(); //db에 연결하는 MemberDAO 객체 생성
				int result = dao.save(member); //MemberDAO 클래스내 db에 입력 데이터를 저장(회원가입)하는 메소드 호출
				//데이터를 저장(회원가입)하는 메소드는 result값을 호출하게된다(1 아니면 -1)
				if(result == 1) {
					JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.");
					dispose(); //회원가입 완료시 회원가입 창을 나가 로그인창으로 이동함
				}else {
					JOptionPane.showMessageDialog(null, "회원가입이 실패하였습니다.");
				}	
			}
		});
	}
}