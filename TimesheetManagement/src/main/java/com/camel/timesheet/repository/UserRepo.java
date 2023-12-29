package com.camel.timesheet.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import com.camel.timesheet.model.User;

@EnableElasticsearchRepositories
public interface UserRepo extends ElasticsearchRepository<User,Integer> {

	
	User findByEmployeeNameAndEmployeeNumber(String employeeName,String employeeNumber);
	User findByEmail(String email);

    User findByPassword(String password);
    
    User findByEmailAndPassword(String email,String password);
}
