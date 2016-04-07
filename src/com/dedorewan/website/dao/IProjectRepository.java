package com.dedorewan.website.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dedorewan.website.dom.Project;

@Repository
public interface IProjectRepository extends JpaRepository<Project, Long>, IProjectRepositoryCustom{
	public List<Project> findAllByOrderByProjectNumberAsc();
	public List<Project> findByProjectNumber(Integer project_number);
}
