package com.camel.timesheet.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;

@Data
@Document(indexName = "timesheetentity")
public class TimesheetEntity {

	@Id
	private String id;
	private String employeeName;
	private String employeeNumber;
	private String month;
	private String year;
	private String clientName;
	private String supervisorName;
	private String assignmentName;
	private String holidaysInput;
	private List<DaywiseActivity> timesheetData;

	public TimesheetEntity() {
	}

	public TimesheetEntity(String id, String employeeName, String employeeNumber, String month, String year,
			String clientName, String supervisorName, String assignmentName, String holidaysInput,
			List<DaywiseActivity> timesheetData) {
		this.id = id;
		this.employeeName = employeeName;
		this.employeeNumber = employeeNumber;
		this.month = month;
		this.year = year;
		this.clientName = clientName;
		this.supervisorName = supervisorName;
		this.assignmentName = assignmentName;
		this.holidaysInput = holidaysInput;
		this.timesheetData = timesheetData;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAssignmentName() {
		return assignmentName;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}

	public String getHolidaysInput() {
		return holidaysInput;
	}

	public void setHolidaysInput(String holidaysInput) {
		this.holidaysInput = holidaysInput;
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

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}

	public List<DaywiseActivity> getTimesheetData() {
		return timesheetData;
	}

	public void setTimesheetData(List<DaywiseActivity> timesheetData) {
		this.timesheetData = timesheetData;
	}

	@Override
	public String toString() {
		return "TimesheetEntity [id=" + id + ", employeeName=" + employeeName + ", employeeNumber=" + employeeNumber
				+ ", month=" + month + ", year=" + year + ", clientName=" + clientName + ", supervisorName="
				+ supervisorName + ", assignmentName=" + assignmentName + ", holidaysInput=" + holidaysInput
				+ ", timesheetData=" + timesheetData + "]";
	}
}
