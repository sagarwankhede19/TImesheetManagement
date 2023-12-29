package com.camel.timesheet.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.camel.timesheet.model.TimesheetEntity;

@Repository
public interface TimesheetRepo extends ElasticsearchRepository<TimesheetEntity, Integer> {

	TimesheetEntity findByEmployeeName(String name);

	TimesheetEntity findByEmployeeNumber(String empNumber);

	TimesheetEntity findByEmployeeNumberAndMonthAndYear(String employeeNumber, String month, String year);

	void deleteByEmployeeNumberAndMonthAndYear(String empNumber, String month, String year);
	
	List<TimesheetEntity> findByStatusAndMonthAndYear(String status,String month,String year);

}
