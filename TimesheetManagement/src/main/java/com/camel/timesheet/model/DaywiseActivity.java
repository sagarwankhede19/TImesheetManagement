package com.camel.timesheet.model;

import lombok.Data;

@Data
public class DaywiseActivity {

	private int date;
	private String leaveOrNonWorkingDays;
	private double nonWorkingHours;
	private String clientName;
	private String assignmentName;
	private double workingHours;

	public DaywiseActivity() {
	}

	public DaywiseActivity(int date, String leaveOrNonWorkingDays, double nonWorkingHours, String clientName,
			String assignmentName, double workingHours) {
		this.date = date;
		this.leaveOrNonWorkingDays = leaveOrNonWorkingDays;
		this.nonWorkingHours = nonWorkingHours;
		this.clientName = clientName;
		this.assignmentName = assignmentName;
		this.workingHours = workingHours;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public String getLeaveOrNonWorkingDays() {
		return leaveOrNonWorkingDays;
	}

	public void setLeaveOrNonWorkingDays(String leaveOrNonWorkingDays) {
		this.leaveOrNonWorkingDays = leaveOrNonWorkingDays;
	}

	public double getNonWorkingHours() {
		return nonWorkingHours;
	}

	public void setNonWorkingHours(double nonWorkingHours) {
		this.nonWorkingHours = nonWorkingHours;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getAssignmentName() {
		return assignmentName;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}

	public double getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(double workingHours) {
		this.workingHours = workingHours;
	}

	@Override
	public String toString() {
		return " [date=" + date + ", leaveOrNonWorkingDays=" + leaveOrNonWorkingDays
				+ ", nonWorkingHours=" + nonWorkingHours + ", clientName=" + clientName + ", assignmentName="
				+ assignmentName + ", workingHours=" + workingHours + "]";
	}

}
