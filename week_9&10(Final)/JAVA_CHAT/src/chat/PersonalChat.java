package chat;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dialog;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import chat.management.MenuJTabaleExam;
import chat.management.UserJDailogGUI;

public class PersonalChat extends JFrame implements ActionListener {

	MsgeBox msgbox = new MsgeBox();
	String temp;
	public static String id;
	
	int lastRoomNum = 100;
	JButton indivSendBtn;
	JTree userTree;
	JTextField indivChatField;
	JTextArea waitRoomArea;
	JLabel lbid, lbnick;
	JTextField lbip;
    String imagename; // 이미지 파일명
	String imagepath = "C:/Temp/"; // db에서 가져온 이미지 저장 경로
	File file;// 사진의 실제 연결되는 경로
	
	Connection con;
	Statement st;
	PreparedStatement stmt;
	ResultSet rs;
	
	ChatClient client;
	ArrayList<User> userArray; // 사용자 목록 배열
	String currentSelectedTreeNode;
	DefaultListModel model;
	DefaultMutableTreeNode level1;		
	DefaultMutableTreeNode level2;	
	DefaultMutableTreeNode level3;	
	
	JScrollPane scrollPane;
    ImageIcon icon;
    LoginFrame login;
    
    WaitRoomUI waitroomui;
    ServerThread server;
	public PersonalChat(ChatClient Client) {
		setTitle(WaitRoomUI.sendUser + "\'s chatting");
		userArray = new ArrayList<User>();
		client = Client;
		initialize();
	}

	private void initialize() {
		setSize(354, 531);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		setResizable(false);

		// 채팅 패널
		final JPanel panel = new JPanel();
		panel.setBounds(12, 10, 320, 358);
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

		waitRoomArea = new JTextArea();
		waitRoomArea.setBackground(Color.WHITE);
		waitRoomArea.setEditable(false); // 수정불가
		scrollPane.setViewportView(waitRoomArea); // 화면 보임
		waitRoomArea.append("♠매너 채팅 하세요!!\n");

		JPanel panel1 = new JPanel();
		// 글쓰는 패널
		panel1.setBounds(12, 425, 235, 34);
		getContentPane().add(panel1);
		panel1.setLayout(new BorderLayout(0, 0));

		indivChatField = new JTextField();
		panel1.add(indivChatField);
		indivChatField.setColumns(10);
		indivChatField.addKeyListener(new KeyAdapter() {
			// 엔터 버튼 이벤트
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					msgSummit();
				}
			}

		});

		// send button
		JButton roomSendBtn = new JButton("보내기");
		roomSendBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(client.getUser().toString()); 
				msgSummit();
				indivChatField.requestFocus();
			}
		});
		roomSendBtn.setBounds(254, 425, 78, 34);
		getContentPane().add(roomSendBtn);
		
		//색상 변경 버튼
		JButton colorBtn = new JButton("색상");
		colorBtn.setBounds(12, 378, 75, 34);
		getContentPane().add(colorBtn);
		
		//색상 버튼 메소드
		colorBtn.addActionListener(new ActionListener() { //색상 버튼을 누르면
            @Override
            public void actionPerformed(ActionEvent arg0) {
                //Color c=JColorChooser.showDialog(RoomUI.this,"Choose",Color.CYAN);  
                //waitRoomArea.setForeground(c); //아니면 waitRoomArea.setBackground(c); 
                ColorChoose dialog = new ColorChoose(waitRoomArea); //색상 변경의 기능이 포함된 ColorChoose.java 객체를 생성함
                dialog.setVisible(true); //색상 변경 창을 보이도록 한다.
            }
        });
		

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		setVisible(true);
		
		//////////////////////////////////////
		// 채팅내용 저장 및 가져오기 메뉴
		JMenu mnuSaveChat = new JMenu("채팅저장");
		mnuSaveChat.addActionListener(this);
		menuBar.add(mnuSaveChat);
		JMenuItem mitSaveChatToFile = new JMenuItem("채팅저장");
		mitSaveChatToFile.addActionListener(this);
		mnuSaveChat.add(mitSaveChatToFile);
		
		JMenu mnuLoadChat = new JMenu("저장채팅확인");
		mnuLoadChat.addActionListener(this);
		menuBar.add(mnuLoadChat);
		JMenuItem mitLoadChatFromFile = new JMenuItem("채팅파일열기");
		mitLoadChatFromFile.addActionListener(this);
		mnuLoadChat.add(mitLoadChatFromFile);
		
		///////////////////////////////////////////////
		
		indivChatField.requestFocus();
		this.addWindowListener(new WindowAdapter() {	// 윈도우 나가기
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "채팅파일열기":
				String filename = UtilFileIO.getFilenameFromFileOpenDialog("./");
				String text = UtilFileIO.loadFile(filename);
				TextViewUI textview = new TextViewUI(text);
				break;		
			case "채팅저장":
				String chatLog = waitRoomArea.getText(); //채팅 내용을 chatLog 변수에 저장
				User user = new User(); //객체를 생성해 getter, setter 사용
				user.setchatLog(chatLog); //user 클래스에 setter를 통해 채팅 저장
				String resultChatLog = user.getchatLog(); //user 클래스에 getter를 통해 채팅 가져온 후
				RoomUI.getID = client.user.getId(); //user 클래스에 getter를 통해 접속 중인 아이디를 가져온 후
				System.out.println(resultChatLog); //출력 테스트해보기
				System.out.println(client.user.getId()); //출력 테스트해보기
				ChatSave chatSave = ChatSave.getInstance(); //생성된 채팅 저장 객체 가져오기
				//채팅 저장 객체를 이용한 채팅 저장 메소드 호출 
				//이때 생성된 user 클래스 객체를 보냄
				chatSave.create(user); 
				/*
				String filename = UtilFileIO.saveFile(chatArea);
				JOptionPane.showMessageDialog(chatArea.getParent(), 
						"채팅내용을 파일명(" + filename + ")으로 저장하였습니다", 
						"채팅백업", JOptionPane.INFORMATION_MESSAGE);
				break;
				*/
		}
	}
	private void msgSummit() {
		String string = WaitRoomUI.id + indivChatField.getText();// 메시지전송
		if (!string.equals("")) {	
				StringTokenizer token = new StringTokenizer(string, " "); // 토큰 생성
				String id = token.nextToken(); // 토큰으로 분리된 스트링
				String msg = token.nextToken();
				
				try {
					client.getDos().writeUTF(User.WHISPER + id + "/" + indivChatField.getText());
					waitRoomArea.append(WaitRoomUI.sendUser + ": " + indivChatField.getText() + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
				indivChatField.setText("");
			} 
		}		

	private void exit01() {
		try {
			client.getUser().getDos().writeUTF(User.LOGOUT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}