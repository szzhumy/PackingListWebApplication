package javaClasses;

public class User {
	private int userID;
	private String firstName;
	private String email;
	
	public User(int id, String firstName, String email) {
		this.userID = id;
		this.firstName = firstName;
		this.email = email;
	}
	
	public void setUserID(int id) {
		this.userID = id;
	}
	
	public void setName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getEmail() {
		return email;
	}
}
