package chat.zip;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;

import chat.JoinFrame;

public class ZipSearch extends JFrame implements MouseListener {

	private JPanel contentPane;
    private JTable table;
    private JComboBox<String> comboBox;
    private JComboBox comboBox_1;
    private JComboBox comboBox_2;
    private JButton chooseBtn;
    private Connection conn = null;
    private PreparedStatement pstmt = null;      
    private ResultSet rs = null;         
    private JScrollPane scrollPane;
    private JPanel panel;
    private JTextField tfDong;
    public String select_zipcode;
    public String select_sido;
    public String select_gugun;
    public String select_dong;
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {    
           EventQueue.invokeLater(new Runnable() {
                   public void run() {
                          try {
                                  ZipSearch frame = new ZipSearch();
                                  frame.setVisible(true);
                                  frame.displaySido();
                          } catch (Exception e) {
                                  e.printStackTrace();
                          }
                   }
           });
    }

    /**
     * Create the frame.
     */
    public ZipSearch() {
          
           setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           setBounds(100, 100, 628, 515);
           contentPane = new JPanel();
           contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
           setContentPane(contentPane);
           contentPane.setLayout(null);
          
           panel = new JPanel();
           panel.setBorder(new TitledBorder(null, "우편번호 검색", TitledBorder.LEADING, TitledBorder.TOP, null, null));
           panel.setBounds(6, 10, 594, 70);
           contentPane.add(panel);
           panel.setLayout(null);
          
           scrollPane = new JScrollPane();
           scrollPane.setBounds(12, 153, 588, 269);
           contentPane.add(scrollPane);
          
           JButton chooseBtn = new JButton("선택");
           chooseBtn.setBounds(227, 432, 139, 29);
           contentPane.add(chooseBtn);
           
           table = new JTable();
           table.setModel(new DefaultTableModel(
                   new Object[][] {
                          {" ", " ", " ", " ", " ", " ", " ", " "},
                   },
                   new String[] {
                          "일련번호", "우편번호", "시도", "구.군", "동", "리", "빌딩", "번지"
                   }
           ) {
                   boolean[] columnEditables = new boolean[] {
                          false, false, false, false, false, false, false, false
                   };
                   public boolean isCellEditable(int row, int column) {
                          return columnEditables[column];
                   }
           });
           table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
           table.addMouseListener(this);
           
           scrollPane.setViewportView(table);
          
          
           //첫번째 combobox 생성
           comboBox = new JComboBox();  
           comboBox.setBounds(146, 40, 100, 20);
           panel.add(comboBox);
           comboBox.addItem("시.도 선택");
          
           displaySido();
           //시.도 콤보박스=============================================
           comboBox.addItemListener(new ItemListener() {
                   public void itemStateChanged(ItemEvent e) {
           if(e.getStateChange()==ItemEvent.SELECTED)
                   selectSido(comboBox.getSelectedItem().toString());
                         
                   }
           });
           comboBox.setToolTipText("");
          
          
           JLabel label = new JLabel("시.도");
           label.setBounds(146, 14, 100, 20);
           panel.add(label);
           label.setHorizontalAlignment(SwingConstants.CENTER);
          
           //구.군 콤보박스=============================================
           comboBox_1 = new JComboBox();
           comboBox_1.setBounds(258, 40, 100, 20);
           panel.add(comboBox_1);
          
           JLabel label_1 = new JLabel("구.군");
           label_1.setBounds(258, 14, 100, 20);
           panel.add(label_1);
           label_1.setHorizontalAlignment(SwingConstants.CENTER);
          
           comboBox_1.addItemListener(new ItemListener() {
                   public void itemStateChanged(ItemEvent e) {
                          if(e.getStateChange()==ItemEvent.SELECTED)
                                  selectGugun(comboBox.getSelectedItem().toString() ,comboBox_1.getSelectedItem().toString());
                   }
           });
          
           //동 콤보박스
           comboBox_2 = new JComboBox();
           comboBox_2.setBounds(370, 40, 100, 20);
           panel.add(comboBox_2);
          
           JLabel label_2 = new JLabel("동");
           label_2.setBounds(370, 14, 100, 20);
           panel.add(label_2);
           label_2.setHorizontalAlignment(SwingConstants.CENTER);
           
           comboBox_2.addItemListener(new ItemListener() {
               public void itemStateChanged(ItemEvent e) {
                      if(e.getStateChange()==ItemEvent.SELECTED)
                     
                      //선택한 콤보박스 정보를 토대로 table을 재출력하는 메소드를 호출 
                      selectDong(comboBox.getSelectedItem().toString(), 
                    		     comboBox_1.getSelectedItem().toString(), 
                    		     comboBox_2.getSelectedItem().toString());
               }             
       });
           
           //동이름 검색 패널
           JPanel panel_1 = new JPanel();
           panel_1.setBorder(new TitledBorder(null, "동이름 검색", TitledBorder.LEADING, TitledBorder.TOP, null, null));
           panel_1.setBounds(6, 86, 594, 57);
           contentPane.add(panel_1);
           
           JLabel lblNewLabel = new JLabel("동이름"); //동이름 검색 라벨
           panel_1.add(lblNewLabel);
           
           tfDong = new JTextField(20); //동이름 검색창
           panel_1.add(tfDong);
           tfDong.setColumns(10);
           
           JButton btnDongSearch = new JButton("search"); //동이름 검색버튼
           panel_1.add(btnDongSearch);
           
           //동이름 검색버튼 클릭시
           btnDongSearch.addActionListener(new ActionListener() {			           
			@Override
			public void actionPerformed(ActionEvent e) {
				searchDong(tfDong.getText()); //검색창에 입력한 내용을 통한 동이름 검색 기반 테이블 출력 메소드 호출
			}
		});
           
	       	chooseBtn.addActionListener(new ActionListener() { //우편번호 창 내 선택 버튼 기능
	    		public void actionPerformed(ActionEvent e) {
	    			//회원가입 창 내 우편번호 입력창에 우편번호와 주소 입력해줌
					JoinFrame.tfZipCode.setText(select_zipcode+ " " +select_sido + " " + select_gugun + " " + select_dong);
					dispose(); //선택버튼 누르는 동시에 우편번호 창에서 나가짐
	    		}
	    	});
    }
    
    //동이름으로 검색하는 메소드 
    public void searchDong(String dongName){
    	//선언
        ZipDAO controller = new ZipDAO();
        //DB연결
        controller.connection();   
        
        //입력한 동에 대한 연관된 데이터를 리스트에 모두 저장 후 해당 리스트를 끝까지 돌려서 테이블에 출력 
        ArrayList<ZipDTO> addressList = controller.searchKeyDong(dongName);
        Object[][] arrAdd = new Object[addressList.size()][8];
        for(int i = 0 ; i < addressList.size() ; i++){
                ZipDTO zipcode = addressList.get(i);
                
                arrAdd[i][0] = zipcode.getSeq(); //일련번호
                arrAdd[i][1] = zipcode.getZipcode(); //우편번호
                arrAdd[i][2] = zipcode.getSido(); //시.도
                arrAdd[i][3] = zipcode.getGugun(); //구.군
                arrAdd[i][4] = zipcode.getDong(); //동
                arrAdd[i][5] = zipcode.getRi(); //리
                arrAdd[i][6] = zipcode.getBldg(); //빌딩
                arrAdd[i][7] = zipcode.getBunji(); //번지
               
                //테이블 갱신(검색한 동으로 구성된 테이블 재출력)
                table.setModel(new ZipTable(arrAdd)); 
        }             
        //DB연결 해제
        controller.disconnection();
    }
    
    //프로그램 시작시 시.도 보여주기====================================================================
    public void displaySido(){
           //선언
           ZipDAO controller = new ZipDAO();
           //DB연결
           controller.connection();             
           //
           ArrayList<ZipDTO> sidoList = controller.searchSido();
           for(int i = 0 ; i < sidoList.size() ; i++){
                   ZipDTO zipcode = sidoList.get(i);
                   comboBox.addItem(zipcode.getSido());
           }             
           //DB연결 해제
           controller.disconnection();
    }
    //sido 선택(gugun 출력) 위의 시도 콤보박스와 연계됨
    public void selectSido(String sido){
           System.out.println(sido);
           ZipDAO controller = new ZipDAO();
           //DB연결
           controller.connection();             
           //
           ArrayList<ZipDTO> gugunList = controller.searchGugun(sido);
           comboBox_1.removeAllItems();
           comboBox_2.removeAllItems();
           comboBox_1.addItem("구.군 선택");
           for(int i = 0 ; i < gugunList.size() ; i++){
                   ZipDTO zipcode = gugunList.get(i);
                   comboBox_1.insertItemAt(zipcode.getGugun(), i);
           }
           table.setModel(new ZipTable());
           //DB연결 해제
           controller.disconnection();
    }      
    //gugun 선택(dong 출력) 위의 구군 콤보박스와 연계됨
    public void selectGugun(String sido, String gugun){
           System.out.println(gugun);
           ZipDAO controller = new ZipDAO();
           //DB연결
           controller.connection();             
           //
           ArrayList<ZipDTO> dongList = controller.searchDong(sido, gugun);
           comboBox_2.removeAllItems();
           comboBox_2.addItem("동 선택");
           for(int i = 0 ; i < dongList.size() ; i++){
                   ZipDTO zipcode = dongList.get(i);
                   comboBox_2.insertItemAt(zipcode.getDong(),i);
           }
           table.setModel(new ZipTable());
           //DB연결 해제
           controller.disconnection();                 
    }
   
    /*마지막 Dong 선택(테이블에 출력) 위의 동 콤보박스와 연계됨
     * 최종적으로 동 콤보박스까지 선택하면 선택한 콤보박스를 토대로 테이블 출력
     * 선택한 콤보박스에 대한 연관된 데이터를 리스트에 모두 저장 후 해당 리스트를 끝까지 돌려서 테이블에 출력  */
    public void selectDong(String sido, String gugun, String dong){
          System.out.println("Selected Dong : " + dong);
           ZipDAO controller = new ZipDAO();
           //DB연결
           controller.connection();             
           //DB에서 선택한 콤보박스를 토대로 데이터 가져오기
           ArrayList<ZipDTO> addressList = controller.searchAddress(sido, gugun, dong);
           //테이블에 재출력할 리스트 생성
           Object[][] arrAdd = new Object[addressList.size()][8];
           //반복을 통해 데이터를 리스트에 삽입
           for(int i = 0 ; i < addressList.size() ; i++){
                   ZipDTO zipcode = addressList.get(i);
                   //출력
                   System.out.println(zipcode.getSeq() + " " + zipcode.getZipcode()+ " " 
                   + zipcode.getSido()+ " " + zipcode.getGugun()+ " " 
                   + zipcode.getDong() + " " + zipcode.getRi() + " " 
                   + zipcode.getBldg() + " " + zipcode.getBunji());                      
                   //테이블에 넣기!
                   arrAdd[i][0] = zipcode.getSeq();
                   arrAdd[i][1] = zipcode.getZipcode();
                   arrAdd[i][2] = zipcode.getSido();
                   arrAdd[i][3] = zipcode.getGugun();
                   arrAdd[i][4] = zipcode.getDong();
                   arrAdd[i][5] = zipcode.getRi();
                   arrAdd[i][6] = zipcode.getBldg();
                   arrAdd[i][7] = zipcode.getBunji();
                  
                   //테이블 갱신(선택한 콤보박스로 구성된 테이블 재출력)
                   table.setModel(new ZipTable(arrAdd));
                   System.out.println("table Setting ");
           }
           //DB연결 해제
           controller.disconnection();      
    }
    
    public void mouseClicked(MouseEvent e) {//우편번호 테이블 내 원하는 주소 선택 기능
    	int row = table.getSelectedRow(); //테이블 내 선택한 위치의 행값 저장=db 데이터의 순서를 의미함
    	int column = table.getSelectedColumn(); //테이블 내 선택한 위치의 열값 저장
    	
    	select_zipcode = (String)table.getValueAt(row, 1); //선택한 주소의 우편번호 값 가져온 후 필드에 저장
    	select_sido = (String)table.getValueAt(row, 2); //선택한 주소의 시.도 값 가져온 후 필드에 저장
    	select_gugun = (String)table.getValueAt(row, 3); //선택한 주소의 구.군 값 가져온 후 필드에 저장
    	select_dong = (String)table.getValueAt(row, 4); //선택한 주소의 동 값 가져온 후 필드에 저장
    	//우편번호 및 주소를 콘솔에 출력
    	System.out.println(select_zipcode+ " " +select_sido + " " + select_gugun + " " + select_dong);
    }
    
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}	
}
