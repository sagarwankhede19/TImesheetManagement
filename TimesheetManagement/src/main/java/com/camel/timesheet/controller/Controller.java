package com.camel.timesheet.controller;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.camel.timesheet.model.TimesheetEntity;
import com.camel.timesheet.model.User;
import com.camel.timesheet.service.TimesheetServices;
import com.camel.timesheet.service.UserService;

@org.springframework.stereotype.Controller

@RestController
public class Controller extends RouteBuilder {

	@Autowired
	TimesheetServices services;

	@Autowired
	UserService userService;

	@Override
	public void configure() throws Exception {
		restConfiguration().component("servlet").port(8082).enableCORS(true).host("localhost")
		.bindingMode(RestBindingMode.json);

		onException(Exception.class).handled(true).log("Exception occurred: ${exception.message}")
		.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
		.setBody(simple("Internal Server Error: ${exception.message}"));

		rest("/email").post("/send").produces("text/plain").to("direct:sendEmail");

		from("direct:sendEmail").setHeader("Subject", constant("Test Email"))
		.setBody(constant("Hello, this is a test email from Camel!"))
		.to("smtps://smtp.gmail.com:465?username=babasahebudamle1007@gmail.com&password=qgge nnbr xjvj tqmn&to=animish.aher@vkraftsoftware.com");

		// ------------------------Save Timesheet----------------------------
		rest().post("/savetimesheet").type(TimesheetEntity.class).to("direct:processTimesheet");
		from("direct:processTimesheet").log("Timesheet : ${body}").process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				TimesheetEntity timesheetDataToSave = exchange.getIn().getBody(TimesheetEntity.class);
				if (services.timesheetExists(timesheetDataToSave)) {
					exchange.getMessage()
					.setBody("Timesheet already exists for " + timesheetDataToSave.getMonth() + " month");
					exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 409);
				} else {
					services.saveTimesheet(timesheetDataToSave);
					System.out.println(timesheetDataToSave);
					exchange.getMessage()
					.setBody("Timesheet is saved for " + timesheetDataToSave.getMonth() + " month");
					exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 201);
				}
			}
		}).end();

		// ------------------------Update Timesheets--------------------------
		rest().put("/updatetimesheet").param().name("employeeNumber").type(RestParamType.query).endParam().param()
		.name("month").type(RestParamType.query).endParam().param().name("year").type(RestParamType.query)
		.endParam().param().name("clientName").type(RestParamType.query).endParam().param()
		.name("assignmentName").type(RestParamType.query).endParam().param().name("holidaysInput")
		.type(RestParamType.query).endParam().to("direct:updateTimeSheet");
		from("direct:updateTimeSheet").process(exchange -> {
			String employeeNumber = exchange.getIn().getHeader("employeeNumber", String.class);
			String month = exchange.getIn().getHeader("month", String.class);
			String year = exchange.getIn().getHeader("year", String.class);
			String clientName = exchange.getIn().getHeader("clientName", String.class);
			String assignmentName = exchange.getIn().getHeader("assignmentName", String.class);
			String holidaysInput = exchange.getIn().getHeader("holidaysInput", String.class);
			String errorMessage = "";
			if (!isValidNumber(employeeNumber)) {
				errorMessage += "Invalid number provided. ";
			}
			if (!isValidMonth(month)) {
				errorMessage += "Invalid month provided. ";
			}
			if (!isValidYear(year)) {
				errorMessage += "Invalid year provided. ";
			}

			if (!errorMessage.isEmpty()) {
				exchange.getMessage().setBody("Error: " + errorMessage.trim());
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
			} else {
				TimesheetEntity existingEntity = services.findByEmployeeNumberAndMonthAndYear(employeeNumber, month,
						year);
				if (existingEntity != null) {
					if (clientName != null) {
						existingEntity.setClientName(clientName);
					}
					if (assignmentName != null) {
						existingEntity.setAssignmentName(assignmentName);
					}
					if (holidaysInput != null) {
						existingEntity.setHolidaysInput(holidaysInput);
					}
					services.saveTimesheet(existingEntity);
					exchange.getMessage().setBody(existingEntity);
					exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);

				} else {
					exchange.getMessage()
					.setBody("Error: TimeSheetEntity with number '" + employeeNumber + "' not found");
					exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 404);
				}
			}
		});
		// ---------------- Delete Timesheet-------------------------
		rest().delete("/deleteemp").param().name("employeeNumber").type(RestParamType.query).endParam().param()
		.name("month").type(RestParamType.query).endParam().param().name("year").type(RestParamType.query)
		.endParam().to("direct:delete");
		from("direct:delete").process(exchange -> {
			String empNumber = exchange.getIn().getHeader("employeeNumber", String.class);
			String month = exchange.getIn().getHeader("month", String.class);
			String year = exchange.getIn().getHeader("year", String.class);
			System.out.println(empNumber + month + year);
			if (isValidNumber(empNumber) && isValidMonth(month) && isValidYear(year)) {
				boolean timesheetEntity = services.deleteByEmployeeNumberAndDate(empNumber, month, year);
				System.out.println(timesheetEntity);
				if (timesheetEntity) {
					System.out.println("valid");
					exchange.getMessage().setBody("Status: Record for Employee " + empNumber + " for Month " + month
							+ " and Year " + year + " Deleted");
				} else {
					exchange.getMessage().setBody("Error: Record not found for Employee " + empNumber + " for Month "
							+ month + " and Year " + year);
					exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 404);
				}
			} else {
				exchange.getMessage().setBody("Error: Invalid input provided");
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
			}
		});

		// -------------------------------------

		rest().get("/gettimesheet").param().name("employeeNumber").type(RestParamType.query).endParam().param()
		.name("month").type(RestParamType.query).endParam().param().name("year").type(RestParamType.query)
		.endParam().to("direct:processName");
		from("direct:processName").process(exchange -> {
			String number = exchange.getIn().getHeader("employeeNumber", String.class);
			String month = exchange.getIn().getHeader("month", String.class);
			String year = exchange.getIn().getHeader("year", String.class);

			String errorMessage = "";
			if (!isValidNumber(number)) {
				errorMessage += "Invalid number provided. ";
			}
			if (!isValidMonth(month)) {
				errorMessage += "Invalid month provided. ";
			}
			if (!isValidYear(year)) {
				errorMessage += "Invalid year provided. ";
			}
			if (!errorMessage.isEmpty()) {
				exchange.getMessage().setBody("Error: " + errorMessage.trim());
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
			} else {
				TimesheetEntity timeSheetEntity = services.findByEmployeeNumberAndMonthAndYear(number, month, year);

				if (timeSheetEntity != null) {
					exchange.getMessage().setBody(timeSheetEntity);
				} else {
					exchange.getMessage().setBody("Error: TimeSheetEntity with number '" + number + "' not found");
					exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 404);
				}
			}
		});

		// ---------------------------Register User----------------------
		rest().post("/registeruser").type(User.class).to("direct:processUser");
		from("direct:processUser").log("User : ${body}").process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				User user = exchange.getIn().getBody(User.class);
				if (userService.userExists(user)) {
					exchange.getMessage().setBody("User already exists for " + user.getEmployeeName() + " with "
							+ user.getEmployeeNumber() + " this employeeNumber");
					exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 409);
				} else {
					userService.saveUser(user);
					exchange.getMessage().setBody(user.getEmployeeName() + " your registration successful.");
					exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 201);
				}
			}
		}).end();

		// --------------------------------------------
		rest("/verifyuser").get().param().name("username").type(RestParamType.query).endParam().param().name("password")
		.type(RestParamType.query).endParam().to("direct:user");
		from("direct:user").process(exchange -> {
			String username = exchange.getIn().getHeader("username", String.class);
			String password = exchange.getIn().getHeader("password", String.class);
			log.info("Received request with username: {} and password: {}", username, password);
			User user = userService.getUserByUsernameAndPassword(username, password);
			boolean isUserValid = verifyUser(user);
			if (isUserValid) {
				exchange.getMessage().setBody(user);
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
			} else {
				exchange.getMessage().setBody("User not found");
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
			}
		});


		//-----------------GetApprovedTimesheets--------------------
		rest().get("/getApprovedTimesheeets")
		.param().name("status").type(RestParamType.query).endParam()
		.param().name("month").type(RestParamType.query).endParam()
		.param().name("year").type(RestParamType.query).endParam().to("direct:approvedTimesheeets");
		from("direct:approvedTimesheeets").process(exchange -> {

			String status = exchange.getIn().getHeader("status", String.class);
			String month = exchange.getIn().getHeader("month", String.class);
			String year = exchange.getIn().getHeader("year", String.class);

			if (services.getApprovedTimesheet(status, month, year)!=null) {

				exchange.getMessage().setBody(services.getApprovedTimesheet(status, month, year));
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
			}
			else {

				exchange.getMessage().setBody("false");
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
			}
		});

	}

	private boolean verifyUser(User user) {
		return userService.validateUser(user.getEmail(), user.getPassword());
	}

	private boolean isValidYear(String year) {
		System.out.println("Valid" + year);
		return year != null && year.matches("^20[2-9]\\d|2[1-9]\\d{2}|3\\d{3}");
	}

	private boolean isValidMonth(String month) {
		System.out.println("Valid" + month);
		return month != null && month.matches(
				"^(January|February|March|April|May|June|July|August|September|October|November|December)|(0?[1-9"
						+ "]|1[0-2])$");
	}

	private boolean isValidNumber(String number) {
		System.out.println("Valid" + number);
		return number != null && number.matches("^(KSS|VKSS)\\d{2,4}$");
	}

}
