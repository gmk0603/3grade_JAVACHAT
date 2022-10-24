package chat;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import javax.swing.ImageIcon;
import java.awt.Graphics;

import chat.MemberDAO;
import chat.Member;

public class JoinFrame extends JFrame {

	private JPanel contentPane;
	private JLabel lblJoin;
	private JButton joinCompleteBtn;
	private JButton idCkBtn;
	private JButton backBtn;
	private JTextField tfID;
	private JTextField tfPassword;
	private JTextField tfName;
	private JTextField tfEmail;
	private JTextField tfPhone;
	DBConnection db = new DBConnection();
	
	//이미지 아이콘 추가
	ImageIcon icon, icon2;
			     
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(445, 530);
		setLocationRelativeTo(null);
		
		//이미지 추가
		icon = new ImageIcon("c:/img/testimagesignup.jpg");
		
		contentPane = new JPanel(){
			public void paintComponent(Graphics g) {
				g.drawImage(icon.getImage(), 0, 0, null);
	            setOpaque(false); //그림을 표시하게 설정,투명하게 조절
	            super.paintComponent(g);
	        }
	    };
	    
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle("회원가입"); // 타이틀 이름
							
		lblJoin = new JLabel("회원가입");
		Font f1 = new Font("돋움", Font.BOLD, 20); //궁서 바탕 돋움
		lblJoin.setFont(f1); 
		lblJoin.setBounds(159, 41, 101, 20);
		contentPane.add(lblJoin);
			
		JLabel lblpassword = new JLabel("password");
		lblpassword.setBounds(29, 164, 69, 20);
		contentPane.add(lblpassword);
		
		JLabel lblID = new JLabel("id");
		lblID.setBounds(29, 117, 69, 20);
		contentPane.add(lblID);
		
		JLabel lblName = new JLabel("name");
		lblName.setBounds(29, 211, 69, 20);
		contentPane.add(lblName);
		
		JLabel lblEmail = new JLabel("email");
		lblEmail.setBounds(29, 258, 69, 20);
		contentPane.add(lblEmail);
		
		JLabel lblPhone = new JLabel("phone");
		lblPhone.setBounds(29, 305, 69, 20);
		contentPane.add(lblPhone);
		
		tfID = new JTextField();
		tfID.setColumns(10);
		tfID.setBounds(119, 107, 147, 35);
		contentPane.add(tfID);
		
		tfPassword = new JTextField();
		tfPassword.setColumns(10);
		tfPassword.setBounds(119, 157, 186, 35);
		contentPane.add(tfPassword);
		
		tfName = new JTextField();
		tfName.setColumns(10);
		tfName.setBounds(119, 204, 186, 35);
		contentPane.add(tfName);
		
		tfEmail = new JTextField();
		tfEmail.setColumns(10);
		tfEmail.setBounds(119, 251, 186, 35);
		contentPane.add(tfEmail);
		
		tfPhone = new JTextField();
		tfPhone.setColumns(10);
		tfPhone.setBounds(119, 298, 186, 35);
		contentPane.add(tfPhone);
		
		idCkBtn = new JButton("IDCheck");
		idCkBtn.setBounds(278, 107, 118, 35);
		contentPane.add(idCkBtn);
		
		joinCompleteBtn = new JButton("회원가입완료");
		joinCompleteBtn.setBounds(218, 400, 139, 29);
		contentPane.add(joinCompleteBtn);
		
		backBtn = new JButton("취소");
		backBtn.setBounds(67, 400, 139, 29);
		contentPane.add(backBtn);
		
		setVisible(true);
		
		// 회원가입 내 취소버튼 등록
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String btnLabel = e.getActionCommand();
				
				if(btnLabel.equals("취소")) {
					dispose();
				}
			}
		});
		
		// ID중복체크 이벤트 등록
		idCkBtn.addActionListener(new ActionListener() { 
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MemberDAO mdao = new MemberDAO();
				String btnLabel = e.getActionCommand();

				if(btnLabel.equals("IDCheck")) {
					//id 텍스트박스에 값이 없을 경우 메시지 출력, 있으면 DB에 연동한다.
					if(tfID.getText().trim().equals("")){
						JOptionPane.showMessageDialog(null,"ID를 입력해주세요.");
						tfID.requestFocus();
					}else {
						if(mdao.getIdByCheck(tfID.getText())) { //중복이 아닐 경우(사용 가능한 경우)
							JOptionPane.showMessageDialog(null, tfID.getText()+"은(는) 사용 가능합니다.");
						}else { //중복일 경우
							JOptionPane.showMessageDialog(null, tfID.getText()+"은(는) 중복된 ID입니다.");
							
							tfID.setText("");
							tfID.requestFocus();
						}
					}
				}
			}
		});
		
		//회원가입완료 액션
		joinCompleteBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Member member = new Member();
				member.setId(tfID.getText());
				member.setPassword(tfPassword.getText());
				member.setName(tfName.getText());
				member.setEmail(tfEmail.getText());
				member.setPhone(tfPhone.getText());
				MemberDAO mdao = new MemberDAO();
				
				String btnLabel = e.getActionCommand();
				
				MemberDAO dao = MemberDAO.getInstance();
				if(btnLabel.equals("회원가입완료")){
					int result = dao.save(member);
					if(result == 1) {
						JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.");
						dispose();
					}else {
						JOptionPane.showMessageDialog(null,  "회원가입에 실패하였습니다.");
						dispose();
					}		
				}

			}
		});
	}
}