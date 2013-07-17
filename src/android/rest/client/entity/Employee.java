package android.rest.client.entity;


public class Employee {
	private int Id;
    private String Full_Name;
    private String Login_Date;
    private String Logout_Date;

    public static final String EMPLOYEE_ID = "Id";
    public static final String EMPLOYEE_NAME = "Full_Name";
    
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getName() {
        return Full_Name;
    }

    public void setName(String name) {
        this.Full_Name = name;
    }

    public String getLogin_Date() {
		return Login_Date;
	}

	public void setLogin_Date(String login_Date) {
		Login_Date = login_Date;
	}

	public String getLogout_Date() {
		return Logout_Date;
	}

	public void setLogout_Date(String logout_Date) {
		Logout_Date = logout_Date;
	}
}

