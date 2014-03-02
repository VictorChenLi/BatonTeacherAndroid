package ca.utoronto.ece1778.baton.models;

/**
 * 
 * @author Yi Zhao
 *
 */
public class Classroom {
	/** database id */
	int id;
	/** name, need to be universally unique */
	String name;

	int[] studentDevicesID;
	int[] teacherDevicesID;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[] getStudentDevicesID() {
		return studentDevicesID;
	}

	public void setStudentDevicesID(int[] studentDevicesID) {
		this.studentDevicesID = studentDevicesID;
	}

	public int[] getTeacherDevicesID() {
		return teacherDevicesID;
	}

	public void setTeacherDevicesID(int[] teacherDevicesID) {
		this.teacherDevicesID = teacherDevicesID;
	}

}
