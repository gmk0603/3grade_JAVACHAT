package chat;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chat.JoinFrame;
import chat.Member;

/**
 * Servlet implementation class zipAccess
 */
//servlet mapping이 addr임
@WebServlet("/zipaccess")
public class zipAccess extends HttpServlet {
	public static String zipCode;
	public static String address;
	public static String mixAddress;
	static Member member = new Member();
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("init called");
	}
	
	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		System.out.println("destroy called");
	}
	public static void insert() {
		if(member.getZipcode() == null) {
			JoinFrame.tfZipCode.setText(mixAddress);	
			System.out.println("insert complete");
		}		
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
        
		request.setCharacterEncoding("utf-8");
        //요청의 내용을 받아옴(request.getParameter:name을 알고 값이 하나일 때)
		zipCode = request.getParameter("zipcode");
		address = request.getParameter("address");
		mixAddress = zipCode + " " + address;
		member.setZipcode(mixAddress);
		System.out.println("우편번호:"+ zipCode);
		System.out.println("주소:"+ address);
		System.out.println(member.getZipcode());
		insert();
		
	}
}
