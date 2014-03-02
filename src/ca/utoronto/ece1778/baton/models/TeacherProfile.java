package ca.utoronto.ece1778.baton.models;

/**
 * 
 * @author Yi Zhao
 *
 */
public class TeacherProfile {
	private String firstName;
	private String lastName;
	private String loginID;
	private String email;
	private String password;
	private String[] classrooms;

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
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String[] getClassrooms() {
		return classrooms;
	}

	public void setClassrooms(String[] classrooms) {
		this.classrooms = classrooms;
	}

}
