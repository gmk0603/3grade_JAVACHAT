package chat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Member {
	private String id;
	private String password;
	private String name;
	private String gender;
	private String birth;
	private String email;
	private String phone;
	private static String zipcode;
	private String imagepath;
	private String imagename;
	private Timestamp createDate;
	private static String bgPath;
	private static String joinTitle1;
	private static String joinTitle2;
	public Member() {
		
	}
	
	public Member(String id, String password, String name, String gender, String birth, String email, String phone, String zipcode, String imagepath, String imagename,
			Timestamp createDate, String bgPath, String joinTitle1, String joinTitle2) {	
		this.id = id;
		this.password = password;
		this.name = name;
		this.gender = gender;
		this.birth = birth;
		this.email = email;
		this.phone = phone;
		this.zipcode = zipcode;
		this.imagepath = imagepath;
		this.imagename = imagename;
		this.createDate = createDate;
		this.bgPath = bgPath;
		this.joinTitle1 = joinTitle1;
		this.joinTitle2 = joinTitle2;
	}
	public String getJointitle1() {
		return joinTitle1;
	}
	public void setJointitle1(String joinTitle1) {
		this.joinTitle1 = joinTitle1;
	}
	public String getJointitle2() {
		return joinTitle2;
	}
	public void setJointitle2(String joinTitle2) {
		this.joinTitle2 = joinTitle2;
	}
	public String getBgpath() {
		return bgPath;
	}
	public void setBgpath(String bgPath) {
		this.bgPath = bgPath;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirth() {
		return birth;
	}
	
	public void setBirth(String birth) {
		this.birth = birth;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	
	public String getZipcode() {
		return zipcode;
	}
	
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	public String getImagepath() {
		return imagepath;
	}
	
	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}
	
	public String getImagename() {
		return imagename;
	}
	
	public void setImagename(String imagename) {
		this.imagename = imagename;
	}
}