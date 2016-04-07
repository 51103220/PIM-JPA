package com.dedorewan.website.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.dedorewan.website.dom.Employee;

public class IEmployeeRepositoryImpl implements IEmployeeRepositoryCustom {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	IEmployeeRepository employeeRepository;

	@SuppressWarnings("unchecked")
	public List<Employee> availableEmployee() {
		String query = "Select * from employee where id NOT IN(select group_leader_id from groups)";
		List<Employee> employees = new ArrayList<Employee>();
		List<Employee> eList = new ArrayList<Employee>();
		eList = (List<Employee>) entityManager.createNativeQuery(query, Employee.class).getResultList();
		for (Employee e : eList) {
			Employee employee = new Employee(e.getId(), e.getVisa(), e.getFirstName(), e.getLastName(),
					e.getBirthDate(), e.getVersion());
			employees.add(employee);
		}

		return employees;
	}

	public List<Employee> getAllEmployeeByVisa(String[] visas) {
		List<Employee> employees = new ArrayList<Employee>();
		for (String visa : visas) {
			employees.addAll(employeeRepository.findByVisa(visa));
		}
		return employees;
	}
}
