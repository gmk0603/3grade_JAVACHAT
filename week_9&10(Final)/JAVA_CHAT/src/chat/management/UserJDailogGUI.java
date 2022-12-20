package chat.management;

//UserJDailogGUI.java
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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

import chat.Birth;
import chat.ChatSave;
import chat.DBConnection;
import chat.JoinFrame;
import chat.Member;
import chat.MemberDAO;

public class UserJDailogGUI extends JDialog implements ActionListener{
	protected static final int String = 0;
	private JPanel contentPane;
	private JPanel birthPanel;
	public static JLabel lblJoin;
	private JButton reviseCompleteBtn;
	private JButton revpwBtn;
	private JButton revidCkBtn;
	private JButton revbackBtn;
	private JButton revimageBtn;
	private JButton revbirthBtn;
	public JTextField revtfID;
	public static JTextField revtfZipCode;
	public static JPasswordField revtfPassword;
	public JTextField revtfName;
	public JRadioButton revtfMan; //남, 여
	private JRadioButton revtfWoman;
	public static JTextField revtfBirth;
	private JTextField revtfEmail1;
	private JTextField revtfEmail2;
	private JTextField revtfPhone1;
	private JTextField revtfPhone2;
	private JTextField revtfPhone3;			     
	public static JTextField revtfImage;
	public static JLabel revlblSecStrength;
	DBConnection db = new DBConnection();
	
	public static String revtitleName = "회원수정";
	public static String revJoinName = "회원수정";
	public static String revzipcode;
		
	String combineEmail;
	String phoneNumber;	
	
	public static File revfile;// 사진의 실제 연결되는 경로
	
	public static String revimg_path = ""; // 사진의 경로를 저장
	public static String revimg_name = ""; // 사진의 이름을 저장
		
	//이미지 아이콘 추가
	ImageIcon sign_bg1; //원본 이미지
	Image mod_bg1; // 이미지아이콘(Imageicon)을 이미지(Image)로 변환
	Image mod_bg2; // 이미지 크기 조정
	ImageIcon sign_bg2; // 조정 이미지(Image)를 이미지아이콘(Imageicon)으로 재변환
	public static String revJoinbgpath = "C:/Temp/defaultbackground.jpg";
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


   MenuJTabaleExam me;  
   UserDefaultJTableDAO dao =new UserDefaultJTableDAO();
   
   public UserJDailogGUI(MenuJTabaleExam me, String index){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(490, 850);
		setLocationRelativeTo(null);
		Member JoinInterfer1 = new Member();
		if (JoinInterfer1.getBgpath() == null) {
			JoinInterfer1.setBgpath(revJoinbgpath);
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
			JoinInterfer1.setJointitle1(revtitleName);
			System.out.println(JoinInterfer1.getJointitle1());
		}
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle(JoinInterfer1.getJointitle1()); // 타이틀 이름
		
		if (JoinInterfer1.getJointitle2() == null) {
			JoinInterfer1.setJointitle2(revJoinName);
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
		
		revlblSecStrength = new JLabel(""); //임시 문자열 '보안강도'를 적어서 어디에 표시되는지 확인함
		revlblSecStrength.setBounds(29, 200, 370, 40);
		contentPane.add(revlblSecStrength);
		
		revlblSecStrength.setFont(new Font("DIALOG", Font.BOLD, 14));
		
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
		
		revtfID = new JTextField();
		revtfID.setColumns(15);
		revtfID.setBounds(145, 107, 147, 35);
		contentPane.add(revtfID);
		
		revtfPassword = new JPasswordField();
		revtfPassword.setEchoChar('*');
		revtfPassword.setColumns(15);
		revtfPassword.setBounds(145, 157, 147, 35);
		contentPane.add(revtfPassword);
		
		revtfZipCode = new JTextField();
		revtfZipCode.setColumns(30);
		revtfZipCode.setBounds(145, 407, 186, 35);
		contentPane.add(revtfZipCode);
		
		revtfName = new JTextField();
		revtfName.setColumns(10);
		revtfName.setBounds(145, 257, 186, 35);
		contentPane.add(revtfName);
		
		revtfMan = new JRadioButton("남");
		revtfWoman = new JRadioButton("여");
		ButtonGroup group = new ButtonGroup();
		group.add(revtfMan);
		group.add(revtfWoman);
		revtfMan.setBounds(145, 307, 70, 35);
		revtfWoman.setBounds(215, 307, 70, 35);
		contentPane.add(revtfMan);
		contentPane.add(revtfWoman);
		
		revtfBirth = new JTextField();
		revtfBirth.setColumns(10);
		revtfBirth.setBounds(145, 357, 147, 35);
		contentPane.add(revtfBirth);
		
		revtfEmail1 = new JTextField(); //이메일 앞단
		revtfEmail1.setColumns(15);
		revtfEmail1.setBounds(145, 457, 87, 35);
		contentPane.add(revtfEmail1);
		
		revtfEmail2 = new JTextField(); //이메일 뒷단
		revtfEmail2.setColumns(15);
		revtfEmail2.setBounds(244, 457, 87, 35);
		contentPane.add(revtfEmail2);
				
		revtfPhone1 = new JTextField();
		revtfPhone1.setColumns(10);
		revtfPhone1.setBounds(145, 507, 69, 35);
		contentPane.add(revtfPhone1);

		revtfPhone2 = new JTextField();
		revtfPhone2.setColumns(10);
		revtfPhone2.setBounds(244, 507, 69, 35);
		contentPane.add(revtfPhone2);

		revtfPhone3 = new JTextField();
		revtfPhone3.setColumns(10);
		revtfPhone3.setBounds(339, 507, 69, 35);
		contentPane.add(revtfPhone3);
		
		revtfImage = new JTextField();
		revtfImage.setColumns(10);
		revtfImage.setBounds(145, 557, 147, 35); 
		contentPane.add(revtfImage);
		
		revidCkBtn = new JButton("아이디 확인");
		revidCkBtn.setBounds(308, 107, 118, 35);
		contentPane.add(revidCkBtn);
		
		revpwBtn = new JButton("비밀번호 확인");
		revpwBtn.setBounds(308, 157, 118, 35);
		contentPane.add(revpwBtn);
				
		revbirthBtn = new JButton("날짜 검색");
		revbirthBtn.setBounds(308, 357, 118, 35);
		contentPane.add(revbirthBtn);
		
		revimageBtn = new JButton("이미지 검색");
		revimageBtn.setBounds(308, 557, 118, 35);
		contentPane.add(revimageBtn);
		
		reviseCompleteBtn = new JButton("회원 수정");
		reviseCompleteBtn.setBounds(246, 772, 139, 29);
		contentPane.add(reviseCompleteBtn);
		
		revbackBtn = new JButton("취소");
		revbackBtn.setBounds(95, 772, 139, 29);
		contentPane.add(revbackBtn);
	
		String emailType[] = {"naver.com", "nate.com", "hanmail.net", "gmail.com", 
				"paran.com", "empal.com", "hotmail.com", "hanmir.com", "직접입력"};
		JComboBox comboBox = new JComboBox(emailType);
		comboBox.setBounds(339, 457, 87, 34);
		contentPane.add(comboBox);
					
		setVisible(true);
		
		// 회원가입 내 취소버튼 등록
		revbackBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					dispose();
			}
		});
		
		// 생일 검색 버튼 등록
		revbirthBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("날짜 검색 버튼 클릭.");
				Birth birth = new Birth();
			}
		});
		
		// 이메일 선택 콤보박스 등록
		comboBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!(comboBox.getSelectedItem().toString()).equals("직접입력")) {
					revtfEmail2.setText("@" + comboBox.getSelectedItem().toString());
					revtfEmail2.requestFocus();
				} else {
					revtfEmail2.setText("@");
					revtfEmail2.requestFocus();
				}			
				System.out.println(revtfEmail1.getText() + revtfEmail2.getText());
				combineEmail = revtfEmail1.getText() + revtfEmail2.getText();
			}
		});
		
		// 이미지 버튼 등록
		revimageBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("프로필 사진 검색 버튼 클릭."); //이미지 검색 버튼 클릭시 출력
				JFileChooser chooser = new JFileChooser(); //파일 탐색창 생성
				int returnVal = chooser.showOpenDialog(UserJDailogGUI.this); //파일 탐색창 출력
				if(returnVal == JFileChooser.APPROVE_OPTION) { //파일 탐색창을 열고 확인 버튼을 눌렀는지 확인
					revfile = chooser.getSelectedFile(); //실제 이미지 파일 경로(File)
					revimg_name = chooser.getSelectedFile().getName(); //이미지 명(String)
					revimg_path = chooser.getSelectedFile().getPath(); //이미지 경로(String)
					try {
						//선택한 이미지를 라벨의 아이콘으로 설정
						ImageIcon icon = new ImageIcon(ImageIO.read(revfile));
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
					revtfImage.setText(revimg_path); //이미지 경로를 프로필 검색창에 입력해줌
					System.out.println(revimg_name); //이미지명 콘솔창에 출력
					System.out.println(revimg_path); //파일경로 콘솔창에 출력					
				}
			}
		});
		
		//보안강도 메소드
		revtfPassword.getDocument().addDocumentListener(new DocumentListener() {
			String resultmsg;
        	UserDefaultJTableDAO mdao = new UserDefaultJTableDAO();
            @Override
            public void insertUpdate(DocumentEvent e) {
            	resultmsg = mdao.revgetPwSecStrength(revtfPassword.getText());
                updateLabel();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            	resultmsg = mdao.revgetPwSecStrength(revtfPassword.getText());
                updateLabel();
            }

            protected void updateLabel() {
            	revlblSecStrength.setText(resultmsg);
            }

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
		
		//비밀번호 중복, 규칙 설정 이벤트 등록
		revpwBtn.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				MemberDAO mdao = new MemberDAO();				
				if(revtfPassword.getText().trim().equals("")) { // 빈 입력창 상태로 버튼 클릭시
					JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요.");
					revtfPassword.requestFocus(); // 비밀번호 입력창으로 포커스 이동
				}else { // 비밀번호 입력 후 버튼 클릭시
					if(mdao.getPwPatternCheck(revtfPassword.getText())) { // 우선적으로 비밀번호 규칙 확인
						JOptionPane.showMessageDialog(null, "최소 9~20글자, 대문자 1개, 소문자 1개, 특수문자 1개로 구성해야됩니다.");
						revtfPassword.setText(""); // 비밀번호 입력창에 입력한 정보 지움
						revtfPassword.requestFocus(); // 비밀번호 입력창에 포커스를 옮김 
					}
					else if(mdao.getPwByCheck(revtfPassword.getText())) { // 규칙 확인 이후 중복 확인
						JOptionPane.showMessageDialog(null, "사용 가능한 비밀번호입니다.");
					}else {
						JOptionPane.showMessageDialog(null, "해당 비밀번호는 중복된 비밀번호입니다.");
						revtfPassword.setText(""); // 비밀번호 입력창에 입력한 정보 지움
						revtfPassword.requestFocus(); // 비밀번호 입력창에 포커스 옮김
					}
					
				}
			}
		});
		
		// ID중복체크 이벤트 등록
		revidCkBtn.addActionListener(new ActionListener() { 			
			@Override
			public void actionPerformed(ActionEvent e) {
				MemberDAO mdao = new MemberDAO();
				//id 텍스트박스에 값이 없을 경우 메시지 출력, 있으면 DB에 연동한다.
				if(revtfID.getText().trim().equals("")){
					JOptionPane.showMessageDialog(null,"아이디를 입력해주세요."); // 빈 입력창 상태로 중복 확인시
					revtfID.requestFocus(); // 아이디 입력창으로 포커스 이동
				} else if(revtfID.getText().length()<6 || revtfID.getText().length()>15) { //6글자 미만 15글자 초과시
					JOptionPane.showMessageDialog(null, "아이디는 6글자 이상 15글자 이하로 입력해야됩니다."); //아이디 생성 불가능 메시지
					revtfID.setText(""); //아이디 입력창 초기화
					revtfID.requestFocus(); //아이디 입력창 포커스 이동
				}
				else {
					if(mdao.getIdByCheck(revtfID.getText())) { //중복이 아닐 경우(사용 가능한 경우)
						JOptionPane.showMessageDialog(null, "사용 가능한 아이디입니다.");
					}else { //중복일 경우
						JOptionPane.showMessageDialog(null, "해당 아이디는 중복된 아이디입니다.");
							
						revtfID.setText(""); // 아이디 입력창에 입력한 정보 지움
						revtfID.requestFocus(); // 아이디 입력창에 포커스를 옮김
					}
				}
			}
		});
		// 회원 수정 이벤트 등록
		reviseCompleteBtn.addActionListener(new ActionListener() { 	
			@Override
			public void actionPerformed(ActionEvent e) {	
				 phoneNumber = revtfPhone1.getText() + revtfPhone2.getText() + revtfPhone3.getText();
				 int reviseresult = startrevise();
				    if( reviseresult > 0){
				    	JOptionPane.showMessageDialog(null, "수정완료되었습니다.");
				    	dispose();
				    	dao.userSelectAll(me.dt);
				    	if(me.dt.getRowCount() > 0 ) me.jt.setRowSelectionInterval(0, 0);
				    	
				    }else{
				    	JOptionPane.showMessageDialog(null, "수정되지 않았습니다.");
				    }   
			}			
		});

		//text박스에 선택된 레코드의 정보 넣기
		int row = me.jt.getSelectedRow();//선택된 행
		revtfID.setText( me.jt.getValueAt(row, 0).toString() );
		revtfPassword.setText( me.jt.getValueAt(row, 1).toString() );
		revtfName.setText( me.jt.getValueAt(row, 2).toString() );
			
		String wholeEmail = me.jt.getValueAt(row, 3).toString();
		String frontEmail = wholeEmail.split("@")[0];
		String backEmail = wholeEmail.split("@")[1];
		revtfEmail1.setText(frontEmail);
		revtfEmail2.setText("@" + backEmail);
		
		String wholePhone = me.jt.getValueAt(row, 4).toString();
		char[] digits1 = wholePhone.toCharArray();
		String combinePhone1 = new StringBuilder().append(digits1[0]).append(digits1[1]).append(digits1[2]).toString();
		String combinePhone2 = new StringBuilder().append(digits1[3]).append(digits1[4]).append(digits1[5]).append(digits1[6]).toString();
		String combinePhone3 = new StringBuilder().append(digits1[7]).append(digits1[8]).append(digits1[9]).append(digits1[10]).toString();
		revtfPhone1.setText(combinePhone1);
		revtfPhone2.setText(combinePhone2);
		revtfPhone3.setText(combinePhone3);
		revtfZipCode.setText( me.jt.getValueAt(row, 5).toString() );
		revtfImage.setText( me.jt.getValueAt(row, 6).toString() );
		if(me.jt.getValueAt(row, 7).toString().equals("M")) {
			revtfMan.setSelected(true);
		}else {
			revtfWoman.setSelected(true);
		}
		revtfBirth.setText( me.jt.getValueAt(row, 8).toString() );
			
		//id text박스 비활성
		revtfID.setEditable(false);
		
		//우측상단 x버튼 누르면 수정창만 나가짐
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
		
		//이벤트등록    
        setLocationRelativeTo(null);
    	
	}//생성자끝
	public int startrevise() {
		int result = dao.userUpdate(this);
		return result;
	}
	/**
	 * 메시지박스 띄워주는 메소드 작성
	 * */
	public static void messageBox(Object obj , String message){
		JOptionPane.showMessageDialog( (Component)obj , message);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}//클래스끝