package com.camel.timesheet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.camel.timesheet.model.User;
import com.camel.timesheet.repository.UserRepo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.StringIdGenerator;

@Service
public class UserService {

	@Autowired
	UserRepo repo;

	public User saveUser(User user) {
		return repo.save(user);
	}
	
	public boolean userExists(User user) {

		if (repo.findByEmployeeNameAndEmployeeNumber(user.getEmployeeName(),user.getEmployeeNumber()) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean validateUser(String username, String password) {
        User user = repo.findByEmail(username);
        return user != null && user.getPassword().equals(password);
    }
	
	public User getUserByUsernameAndPassword(String email, String password ) {
		return repo.findByEmailAndPassword(email, password);
	}

}
