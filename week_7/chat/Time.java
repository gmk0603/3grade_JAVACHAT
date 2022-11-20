package chat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Time {
	
	//자바의 LocalDateTime 데이터 타입을 DB에 insert할 때 필요
	public static Timestamp localDateTimeToTimeStamp(LocalDateTime ldt) {
		return Timestamp.valueOf(ldt); // 2018-07-26 01:06:55.323
	}
	
	//DB의 TimeStamp 데이터 타입을 Java로 select하여 가져올 때 필요
	public static LocalDateTime timeStampToLocalDateTime(Timestamp ts) {
		return ts.toLocalDateTime(); // 2018-07-26T01:20:07.364
	}
	
	public static void main(String[] args) {
		Timestamp ts = localDateTimeToTimeStamp(LocalDateTime.now());
		System.out.println("ts : "+ts);
		
		LocalDateTime ldt = timeStampToLocalDateTime(ts);
		System.out.println("ldt : "+ldt);
	}
}