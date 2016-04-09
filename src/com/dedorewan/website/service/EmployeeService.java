package com.dedorewan.website.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dedorewan.website.dao.IEmployeeRepository;
import com.dedorewan.website.dom.Employee;

@Transactional
@Service
public class EmployeeService implements IEmployeeService {
	@Autowired
	private IEmployeeRepository employeeRepository;
	
	public List<Employee> availableEmployee(){
		return employeeRepository.availableEmployee();
	}
}
