package com.dedorewan.website.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dedorewan.website.dom.Employee;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
	List<Employee> findByVisa(String visa);

	@Query(value = "select * from employee where id NOT IN(select group_leader_id from groups)", nativeQuery = true)
	List<Employee> availableEmployee();
}
