package com.camel.timesheet.model;
 
 
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
 
@Document(indexName = "registration")
public class User {
	
	@Id
    private String id;
   
    private String employeeName;
    private String employeeNumber;
    private String email;
    private String password;
    private String designation;
    private String gender;
    private long mobileNumber;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
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
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public long getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", employeeName=" + employeeName + ", employeeNumber=" + employeeNumber + ", email="
				+ email + ", password=" + password + ", designation=" + designation + ", gender=" + gender
				+ ", mobileNumber=" + mobileNumber + "]";
	}
	public User(String id, String employeeName, String employeeNumber, String email, String password,
			String designation, String gender, long mobileNumber) {
		this.id = id;
		this.employeeName = employeeName;
		this.employeeNumber = employeeNumber;
		this.email = email;
		this.password = password;
		this.designation = designation;
		this.gender = gender;
		this.mobileNumber = mobileNumber;
	}
	public User() {
	}
    
    
 
}