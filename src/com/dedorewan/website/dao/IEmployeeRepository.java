package com.dedorewan.website.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dedorewan.website.dom.Employee;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long>,IEmployeeRepositoryCustom {
	List<Employee> findByVisa(String visa);
}
