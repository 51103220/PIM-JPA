package com.dedorewan.website.service;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.dedorewan.website.dao.IEmployeeRepository;
import com.dedorewan.website.dao.IGroupRepository;
import com.dedorewan.website.dao.IProjectRepository;
import com.dedorewan.website.dom.Project;
import com.dedorewan.website.dom.Project.STATUS;
import com.dedorewan.website.exception.CustomException;

@Service
@Transactional
public class ProjectService implements IProjectService {

	@Autowired
	private IProjectRepository projectRepository;

	@Autowired
	IEmployeeRepository employeeRepository;

	@Autowired
	IGroupRepository groupRepository;

	public List<Project> findAll() {
		return projectRepository.findAllByOrderByProjectNumberAsc();
	}

	public Project getProject(Long id) throws Exception {
		Project project = projectRepository.findOne(id);
		if (project == null) {
			throw new CustomException("custom", "requested project does not exist. Please reload Page");
		}
		return project;
	}

	public void addProject(Project project) throws Exception {
		project.setGroup(groupRepository.findOne(project.getGroupId()));
		project.setEmployees(employeeRepository.getAllEmployeeByVisa(project.getMembers()));
		projectRepository.save(project);
	}

	public boolean projectNumberExisted(Long id, Integer project_number) {
		List<Project> projects = projectRepository.findByProjectNumber(project_number);
		if (projects.size() > 0) {
			if (projects.get(0).getId() == id) {
				return false;
			} else
				return true;
		} else
			return false;
	}

	public boolean visaExsisted(String visa) {
		return projectRepository.visaExsisted(visa);
	}

	public void updateProject(Project project) throws Exception {
		try {
			project.setGroup(groupRepository.findOne(project.getGroupId()));
			project.setEmployees(employeeRepository.getAllEmployeeByVisa(project.getMembers()));
			projectRepository.save(project);
		} catch (StaleObjectStateException s) {
			throw new CustomException("custom",
					"Update Project Failed (Project has been updated or deleted by another user)");
		} catch (OptimisticLockingFailureException e) {
			throw new CustomException("custom",
					"Update Project Failed (Project has been updated or deleted by another user)");
		}
	}

	public void deleteProject(Long id) throws Exception {
		Project project = projectRepository.findOne(id);
		if (project != null) {
			projectRepository.delete(project);
		} else {
			throw new CustomException("custom",
					"Delete unsucessfully!!! Project has been deleted or updated. Please Reload Page");
		}
	}

	public void deleteProjects(Long[] ids) throws Exception {
		for (long id : ids) {
			deleteProject(id);
		}
	}

	public List<Project> filterProjects(String keywords, STATUS statusKey) {
		return projectRepository.filterProjects(keywords, statusKey);
	}

	public List<Project> projectsInPage(List<Project> projects, Integer page) {
		return projectRepository.projectsInPage(projects, page);
	}

	public Integer numberPages(List<Project> projects, Integer maxProjects) {
		return projectRepository.numberPages(projects, maxProjects);
	}

	public String groupLeaderVisa(Project project) {
		return project.getGroup().getLeader().getVisa();
	}
}
