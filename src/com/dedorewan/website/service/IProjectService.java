package com.dedorewan.website.service;

import java.util.List;

import com.dedorewan.website.dom.Project;
import com.dedorewan.website.dom.Project.STATUS;

public interface IProjectService {
	List<Project> findAll();

	List<Project> projectsInPage(List<Project> projects, Integer page);

	Project getProject(Long id) throws Exception;

	void addProject(Project project) throws Exception;

	boolean projectNumberExisted(Long id, Integer project_number);

	boolean visaExsisted(String visa);

	void updateProject(Project project) throws Exception;

	void deleteProject(Project project) throws Exception;

	void deleteProjects(List<Project> projects) throws Exception;

	Integer numberPages(List<Project> projects, Integer maxProjects);

	List<Project> filterProjects(String keywords, STATUS statusKey);

	String groupLeaderVisa(Project project);
}
