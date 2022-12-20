package chat;

import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class UtilFileIO {		
	// 파일읽어오기 : File에 데이타를 읽어와서 String 문자열 반환
	public static String loadFile(File file) {
		String result = null;
		// 파일에서 내용 가져오기
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer(4096);
		
		try {
			br = new BufferedReader(new FileReader(file));
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str +"\n");
			}
			result = sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	// 파일읽어오기 : 파일명(dir + filename)을 String으로 받아 내용을 String으로 반환
	// filename : 디렉토리 + 파일명 + 확장자 정보가 포함된 문자열
	// return : filename의 내용을 String으로 return
	public static String loadFile(String filename) {
		if (filename == "") return "";
		String result = null;
		// 파일에서 내용 가져오기
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer(4096);
		File file = new File(filename);
		
		try {
			br = new BufferedReader(new FileReader(file));
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str + "\n");
			}
			result = sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	// 파일다이얼로그를 이용하여 파일명(디렉토리 + 파일) 가져오기
	// currDir : 파일검색할 디렉토리 위치
	// return : 파일 다이얼로그를 통해 선택한 파일명(디렉토리 + 파일)
	public static String getFilenameFromFileOpenDialog(String currDir) {
		String filename = null;
		FileDialog fd = null;
		fd = new FileDialog(fd, "파일열기", FileDialog.LOAD);
		fd.setVisible(true);
		String dir = fd.getDirectory();
		String fname = fd.getFile();
		filename = dir + fname;
		return filename;
	}
}
