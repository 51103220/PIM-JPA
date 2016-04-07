package com.dedorewan.website.dao;

import java.util.List;

import com.dedorewan.website.dom.Project;
import com.dedorewan.website.dom.Project.STATUS;

public interface IProjectRepositoryCustom {
	List<Project> filterProjects(String keywords, STATUS statusKey);

	List<Project> projectsInPage(List<Project> projects, Integer page);

	Integer numberPages(List<Project> projects, Integer maxProjects);

	boolean visaExsisted(String visa);
	
	void insert(Project project) throws Exception;
	
	void update(Project project) throws Exception;
	
	public void delete(Long id) throws Exception;
	
}
