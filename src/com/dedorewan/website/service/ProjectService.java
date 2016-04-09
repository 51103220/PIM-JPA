package com.dedorewan.website.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.dedorewan.website.dao.IEmployeeRepository;
import com.dedorewan.website.dao.IGroupRepository;
import com.dedorewan.website.dao.IProjectRepository;
import com.dedorewan.website.dom.Employee;
import com.dedorewan.website.dom.Project;
import com.dedorewan.website.dom.Project.STATUS;
import com.dedorewan.website.exception.CustomException;

@Service
@Transactional
public class ProjectService implements IProjectService {

	@Autowired
	private IProjectRepository projectRepository;

	@Autowired
	private IEmployeeRepository employeeRepository;

	@Autowired
	private IGroupRepository groupRepository;

	public List<Project> findAll() {
		return projectRepository.findAllByOrderByProjectNumberAsc();
	}

	private List<Employee> getAllEmployeeByVisa(String[] visas) {
		List<Employee> employees = new ArrayList<Employee>();
		for (String visa : visas) {
			employees.addAll(employeeRepository.findByVisa(visa));
		}
		return employees;
	}

	public Project getProject(Long id) throws Exception {
		Project project = projectRepository.findOne(id);
		if (project == null) {
			throw new CustomException("projectNotFound", "requested project does not exist. Please reload Page");
		}
		return project;
	}

	public void addProject(Project project) throws Exception {
		project.setGroup(groupRepository.findOne(project.getGroupId()));
		project.setEmployees(getAllEmployeeByVisa(project.getMembers()));
		projectRepository.save(project);
	}

	public boolean projectNumberExisted(Long id, Integer project_number) {
		List<Project> projects = projectRepository.findByProjectNumber(project_number);
		return projects.size() > 0 && projects.get(0).getId() != id;
	}

	public boolean visaExsisted(String visa) {
		return projectRepository.visaExsisted(visa);
	}

	public void updateProject(Project project) throws Exception {
		try {
			project.setGroup(groupRepository.findOne(project.getGroupId()));
			project.setEmployees(getAllEmployeeByVisa(project.getMembers()));
			projectRepository.save(project);
		} catch (StaleObjectStateException s) {
			throw new CustomException("concurrentUpdate",
					"Update Project Failed (Project has been updated or deleted by another user)");
		} catch (OptimisticLockingFailureException e) {
			throw new CustomException("concurrentUpdate",
					"Update Project Failed (Project has been updated or deleted by another user)");
		}
	}

	public void deleteProject(Project project) throws Exception {
		if (projectRepository.exists(project.getId()) && project.getStatus() == STATUS.NEW) {
			try {
				projectRepository.delete(project);
			} catch (OptimisticLockingFailureException e) {
				throw new CustomException("concurrentDelete",
						"Delete Project Failed (Project has been updated or deleted by another user)");
			} catch (StaleObjectStateException s) {
				throw new CustomException("concurrentDelete",
						"Delete Project Failed (Project has been updated or deleted by another user)");
			}
		} else {
			throw new CustomException("projectDeleted",
					"Delete unsucessfully!!! Project has been deleted or updated or not a new Project. Please Reload Page");
		}
	}

	public void deleteProjects(List<Project> projects) throws Exception {
		for(Project project: projects){
			deleteProject(project);
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
