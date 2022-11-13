package chat.management;

//UserJDailogGUI.java
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import chat.JoinFrame;
import chat.MemberDAO;

public class UserJDailogGUI extends JDialog implements ActionListener{
	
	JPanel pw=new JPanel(new GridLayout(10,1));
	JPanel pc=new JPanel(new GridLayout(10,1));
	JPanel ps=new JPanel();

	JLabel lable_Id = new JLabel("아이디");
	JLabel lable_Password=new JLabel("비밀번호");
	JLabel lable_Name =new JLabel("이름");
	JLabel lable_email =new JLabel("이메일");
	JLabel label_phone = new JLabel("전화번호");
	JLabel label_zipcode = new JLabel("우편번호");
	JLabel label_image = new JLabel("프로필사진");
	JLabel label_imagefloat = new JLabel("변경 사진");
	JLabel label_dum = new JLabel();
	JLabel label_gender = new JLabel("성별");
	JLabel label_birth = new JLabel("생일");


	JTextField id=new JTextField();
	JTextField password=new JTextField();
	JTextField name=new JTextField();
	JTextField email=new JTextField();
	JTextField phone=new JTextField();
	JTextField zipcode=new JTextField();
	JTextField image=new JTextField();
	JTextField birth=new JTextField();
	

	JButton confirm;
	JButton reset=new JButton("취소");

   MenuJTabaleExam me;

   JPanel Pwcheck = new JPanel(new BorderLayout());
   JButton PwBtn = new JButton("비밀번호확인");
   
   JPanel Gender = new JPanel(new BorderLayout());
   public static JRadioButton tfMan = new JRadioButton("남");
   public static JRadioButton tfWoman = new JRadioButton("여");
   ButtonGroup group = new ButtonGroup();
   { //괄호를 안씌워주면 정상적으로 타이핑했음에도 불구하고 오류가 계속 나온다.
	   group.add(tfMan);
	   group.add(tfWoman);
   }
   
   JPanel Images = new JPanel(new BorderLayout());
   {
	   Images.setPreferredSize(new Dimension(400, 100));
   }
   JButton ImageBtn = new JButton("프로필검색");
   
   UserDefaultJTableDAO dao =new UserDefaultJTableDAO();
   

	public UserJDailogGUI(MenuJTabaleExam me, String index){
		super(me,"회원수정");
		this.me=me;
		if(index.equals("가입")){
			confirm=new JButton(index);
		}else{
			confirm=new JButton("수정");	
			
			//text박스에 선택된 레코드의 정보 넣기
			int row = me.jt.getSelectedRow();//선택된 행
			id.setText( me.jt.getValueAt(row, 0).toString() );
			password.setText( me.jt.getValueAt(row, 1).toString() );
			name.setText( me.jt.getValueAt(row, 2).toString() );
			email.setText( me.jt.getValueAt(row, 3).toString() );
			phone.setText( me.jt.getValueAt(row, 4).toString() );
			zipcode.setText( me.jt.getValueAt(row, 5).toString() );
			image.setText( me.jt.getValueAt(row, 6).toString() );
			if(me.jt.getValueAt(row, 7).toString().equals("M")) {
				tfMan.setSelected(true);
			}else {
				tfWoman.setSelected(true);
			}
			birth.setText( me.jt.getValueAt(row, 8).toString() );
			
			//id text박스 비활성
			id.setEditable(false);
		}
		
		
		//Label추가부분
		pw.add(lable_Id);//아이디
		pw.add(lable_Password);//비밀번호
		pw.add(lable_Name);//이름
		pw.add(lable_email);//이메일
		pw.add(label_phone);//전화번호
		pw.add(label_zipcode);//우편번호
		pw.add(label_image);//프로필사진
		pw.add(label_imagefloat);//프로필사진 입력 위치
		pw.add(label_gender);//성별
		pw.add(label_birth);//생일
	
		//비밀번호 패널에 입력창 및 버튼 삽입
		Pwcheck.add(password,"Center");
		Pwcheck.add(PwBtn,"East");

		//성별 라디오버튼 삽입
		Gender.add(tfMan, "Center");
		Gender.add(tfWoman, "East");
		
		//이미지 패널에 입력창 및 버튼 삽입
		Images.add(image, "Center");
		Images.add(ImageBtn, "East");
		
		//TextField 추가
		pc.add(id);
		pc.add(Pwcheck);
		pc.add(name);
		pc.add(email);
		pc.add(phone);
		pc.add(zipcode);
		pc.add(Images);
		pc.add(label_dum);
		pc.add(Gender);
		pc.add(birth);
		
		
		
		ps.add(confirm); 
		ps.add(reset);
	
		getContentPane().add(pw,"West"); 
		getContentPane().add(pc,"Center");
		getContentPane().add(ps,"South");
		
		setSize(435,800);
		setVisible(true);

		//우측상단 x버튼 누르면 수정창만 나가짐
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
		
		//이벤트등록
        confirm.addActionListener(this); //가입/수정 이벤트등록
        reset.addActionListener(this); //취소 이벤트등록
        PwBtn.addActionListener(this);// ID중복체크 이벤트 등록
        setLocationRelativeTo(null);
    	ImageBtn.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			System.out.println("프로필 사진 검색 버튼 클릭."); //이미지 검색 버튼 클릭시 출력
    			JFileChooser chooser = new JFileChooser(); //파일 탐색창 생성
    			int returnVal = chooser.showOpenDialog(UserJDailogGUI.this); //파일 탐색창 출력
    			if(returnVal == JFileChooser.APPROVE_OPTION) { //파일 탐색창을 열고 확인 버튼을 눌렀는지 확인
    				JoinFrame.file = chooser.getSelectedFile(); //실제 이미지 파일 경로(File)
    				JoinFrame.img_name = chooser.getSelectedFile().getName(); //이미지 명(String)
    				JoinFrame.img_path = chooser.getSelectedFile().getPath(); //이미지 경로(String)
    				try {
    					//선택한 이미지를 라벨의 아이콘으로 설정
    					ImageIcon icon = new ImageIcon(ImageIO.read(JoinFrame.file));
    					//아이콘에서 이미지를 리턴받음
    					Image imageSrc = icon.getImage();
    					//이미지 resize(x:100 y:100으로 설정)
    					Image imageNew = imageSrc.getScaledInstance(100,  100,  Image.SCALE_AREA_AVERAGING);
    					//아이콘 변경
    					icon = new ImageIcon(imageNew);
    					label_dum.setIcon(icon); //사진 위치에 사진 삽입
    				}catch(Exception e2) {
    					e2.printStackTrace();
    				}
    				image.setText(JoinFrame.img_path); //이미지 경로를 프로필 검색창에 입력해줌
    				System.out.println(JoinFrame.img_name); //이미지명 콘솔창에 출력
    				System.out.println(JoinFrame.img_path); //파일경로 콘솔창에 출력					
    			}   				
    		}  
       });
	}//생성자끝
    
	/**
	 * 수정 기능에 대한 부분
	 * */
	@Override
	public void actionPerformed(ActionEvent e) {
	   String btnLabel =e.getActionCommand();//이벤트주체 대한 Label 가져오기
	    
	   if(btnLabel.equals("수정")){
		   
		    if( dao.userUpdate(this) > 0){
		    	messageBox(this, "수정완료되었습니다.");
		    	dispose();
		    	dao.userSelectAll(me.dt);
		    	if(me.dt.getRowCount() > 0 ) me.jt.setRowSelectionInterval(0, 0);
		    	
		    }else{
		    	messageBox(this, "수정되지 않았습니다.");
		    }   
	   }else if(btnLabel.equals("취소")){
		   dispose();
		   
	   }else if(btnLabel.equals("비밀번호확인")){
			MemberDAO mdao = new MemberDAO();				
			if(password.getText().trim().equals("")) { // 빈 입력창 상태로 버튼 클릭시
				JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요.");
				password.requestFocus(); // 비밀번호 입력창으로 포커스 이동
			}else { // 비밀번호 입력 후 버튼 클릭시
				if(mdao.getPwPatternCheck(password.getText())) { // 우선적으로 비밀번호 규칙 확인
					JOptionPane.showMessageDialog(null, "최소 9~15글자, 대문자 1개, 소문자 1개, 특수문자 1개로 구성해야됩니다.");
					password.setText(""); // 비밀번호 입력창에 입력한 정보 지움
					password.requestFocus(); // 비밀번호 입력창에 포커스를 옮김 
				}
				else if(mdao.getPwByCheck(password.getText())) { // 규칙 확인 이후 중복 확인
					JOptionPane.showMessageDialog(null, "사용 가능한 비밀번호입니다.");
				}else {
					JOptionPane.showMessageDialog(null, "해당 비밀번호는 중복된 비밀번호입니다.");
					password.setText(""); // 비밀번호 입력창에 입력한 정보 지움
					password.requestFocus(); // 비밀번호 입력창에 포커스 옮김
				}
				
			}
	   }		
	}//actionPerformed끝

	
	/**
	 * 메시지박스 띄워주는 메소드 작성
	 * */
	public static void messageBox(Object obj , String message){
		JOptionPane.showMessageDialog( (Component)obj , message);
	}

}//클래스끝