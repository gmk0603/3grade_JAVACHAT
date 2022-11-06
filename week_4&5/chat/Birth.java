package chat;

import javax.swing.JLayeredPane;

import chat.Imageload.myPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl; //JDatePicker 캘린더 구성 요소 임포트(시각화된 날짜 달력 내 직접 클릭)
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import chat.JoinFrame; //회원가입 창 내 생일 입력창에 접근하기 위한 임포트

public class Birth extends JFrame {	
	private JButton selectBirth; //생일 날짜 선택 버튼
	public Birth() {
		Birth birth;
		birth = this;
		this.setTitle("생일 검색"); //생일 날짜 선택장 제목
		this.setLayout(null);
		this.setSize(300, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		UtilDateModel model = new UtilDateModel(); //캘린더 구성 요소 객체 생성
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		JDatePickerImpl birthChoose = new JDatePickerImpl(datePanel);
		birthChoose.setBounds(40,0,200,30);
		this.add(birthChoose);
		
		selectBirth = new JButton("생일선택완료"); //생일 날짜 선택 버튼 생성
		selectBirth.setBounds(75, 220, 139, 29);
		this.add(selectBirth);
		
		setBounds(800, 300, 300, 300);
		setVisible(true);
		
		// 생일선택버튼 등록
		selectBirth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					Date dateBirth = (Date) birthChoose.getModel().getValue(); //선택한 날짜를 Date 데이터타입으로 가져옴
					String datePattern = "yyyyMMdd"; //4글자 년도, 2글자 월, 2글자 날짜를 표현하는 패턴
					
					SimpleDateFormat format = new SimpleDateFormat(datePattern); //날짜 데이터를 문자열 형식으로 포맷을 참고해 바꿈
					String stringBirth = format.format(dateBirth); //문자열 형식으로 포맷 변환
					JoinFrame.tfBirth.setText(stringBirth); //회원가입창 내 생일 입력창에 생일 입력
					System.out.println(stringBirth);
					dispose(); //생일 날짜 선택 창 나가짐
			}
		});
	}
}