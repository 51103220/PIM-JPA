package com.dedorewan.website.dao;

import java.util.List;
import com.dedorewan.website.dom.Employee;
public interface IEmployeeRepositoryCustom {
	List<Employee> availableEmployee();
	List<Employee> getAllEmployeeByVisa(String[] visas);
}
