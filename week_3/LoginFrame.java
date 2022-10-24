package chat;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import chat.ChatClientObject;
import java.awt.Color;
import java.awt.Component;

public class LoginFrame extends JFrame {

	private JPanel contentPane;
	private JTextField tfID, tfPassword;
	private JButton loginBtn, joinBtn;
	
	//이미지 아이콘 추가
	ImageIcon icon, icon2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);
		setLocationRelativeTo(null);
		
		//배경 추가
		icon = new ImageIcon("c:/img/testimagelogin.jpg");
		
		contentPane = new JPanel(){
			public void paintComponent(Graphics g) {
				g.drawImage(icon.getImage(), 0, 0, null);
	            setOpaque(false); //그림을 표시하게 설정,투명하게 조절
	            super.paintComponent(g);
	        }
	    };
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle("로그인"); // 타이틀 이름
		
		JLabel lblLogin = new JLabel("ID");
		lblLogin.setBounds(41, 52, 69, 35);
		contentPane.add(lblLogin);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(41, 103, 69, 35);
		contentPane.add(lblPassword);
		
		tfID = new JTextField();
		tfID.setBounds(157, 52, 176, 35);
		contentPane.add(tfID);
		tfID.setColumns(10);
		
		joinBtn = new JButton("회원가입");
		joinBtn.setBounds(229, 154, 104, 29);
		contentPane.add(joinBtn);
		
		loginBtn = new JButton("로그인");
		loginBtn.setBounds(108, 154, 106, 29);
		contentPane.add(loginBtn);
		
		tfPassword = new JTextField();
		tfPassword.setColumns(10);
		tfPassword.setBounds(157, 103, 176, 35);
		contentPane.add(tfPassword);
		
		setVisible(true);
		//회원가입 액션
		joinBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JoinFrame frame = new JoinFrame();
			}
		});
		
		//로그인 액션
		loginBtn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						String ID = tfID.getText();
						String password = tfPassword.getText();
						MemberDAO dao = MemberDAO.getInstance();
						int result = dao.findByUsernameAndPassword(ID, password);
						if(result == 1) {
							//로그인 성공 메시지
							JOptionPane.showMessageDialog(null, "로그인 성공");
							//회원 정보 리스트 화면 이동과 동시에 username 세션값으로 넘김.
							//ChatClientObject 객체를 생성시켜 채팅방으로 연결시킨다.
							ChatClientObject chatclientobject = new ChatClientObject();
							chatclientobject.service();
							//MemberListFrame mlf = new MemberListFrame(username);
							//현재 화면 종료
							dispose();
						}else {
							JOptionPane.showMessageDialog(null, "로그인 실패");
						}
						
					}
		});
	}
}