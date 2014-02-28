package ca.utoronto.ece1778.baton.models;

import ca.utoronto.ece1778.baton.util.CommonUtilities;

/**
 * 
 * @author Yi Zhao
 *
 */

public class StudentProfile {

	/** string for communication with sync server as a parameter name */
	public static final String POST_FIRST_NAME = "f_name";
	/** string for communication with sync server as a parameter name */
	public static final String POST_LAST_NAME = "l_name";
	/** string for communication with sync server as a parameter name */
	public static final String POST_LOGIN_ID = "nick_name";
	/** string for communication with sync server as a parameter name */
	public static final String POST_EMAIL = "email";
	/** string for communication with sync server as a parameter name */
	public static final String POST_PASSWORD = "password";
	/** string for communication with sync server as a parameter name */
	public static final String POST_GCM_ID = "gcm_regid";

	private String firstName = "";
	private String lastName = "";
	private String loginID = "";
	private String email = "";
	private String password_md5 = "";
	private String gcm_id = "";
	private String[] classroomNames;

	public StudentProfile(String firstName, String lastName, String loginID,
			String email, String password, String gcm_id) {
		if (firstName == null)
			firstName = "";
		if (lastName == null)
			lastName = "";
		if (loginID == null)
			loginID = "";
		if (email == null)
			email = "";
		if (password == null)
			password = "";
		if (gcm_id == null)
			gcm_id = "";
		this.firstName = firstName;
		this.lastName = lastName;
		this.loginID = loginID;
		this.email = email;
		this.password_md5 = CommonUtilities.getMD5Str(password);
		this.gcm_id = gcm_id;
	}

	public String toString() {
		return "user: " + this.firstName + " " + this.lastName + ", "
				+ this.loginID + ", " + this.email + ", " + this.password_md5
				+ ", \n gcm_regid:" + this.gcm_id;
	}

	public String getRegisterParameter() {
		return "gcm_regid=" + this.gcm_id + "&email=" + this.email + "&password="
				+ this.password_md5 + "&f_name=" + this.firstName + "&l_name="
				+ this.lastName + "&nick_name=" + this.loginID;
	}

	public String getLoginParameter() {
		return "gcm_regid=" + this.gcm_id + "&email=" + this.email + "&password="
				+ this.password_md5;
	}

	public String getGcm_id() {
		return gcm_id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLoginID() {
		return loginID;
	}

	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password_md5;
	}

	public String[] getClassroomNames() {
		return classroomNames;
	}

	public void setClassroomNames(String[] classroomNames) {
		this.classroomNames = classroomNames;
	}

}
