package com.dedorewan.website.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

	private Locale locale;

	@Autowired
	private IEmployeeRepository employeeRepository;

	@Autowired
	private IGroupRepository groupRepository;

	@Autowired
	private MessageSource messageSource;

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
		locale = LocaleContextHolder.getLocale();
		String message = messageSource.getMessage("concurrent.projectNotExist", new Object[] {}, locale);
		Project project = projectRepository.findOne(id);
		if (project == null) {
			throw new CustomException("projectNotFound", message);
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
		locale = LocaleContextHolder.getLocale();
		String message = messageSource.getMessage("concurrent.update", new Object[] {}, locale);
		if (projectRepository.exists(project.getId())) {
			try {
				project.setGroup(groupRepository.findOne(project.getGroupId()));
				project.setEmployees(getAllEmployeeByVisa(project.getMembers()));
				projectRepository.save(project);
			} catch (StaleObjectStateException s) {
				throw new CustomException("concurrentUpdate", message);
			} catch (OptimisticLockingFailureException e) {
				throw new CustomException("concurrentUpdate", message);
			}
		} else{
			message = messageSource.getMessage("concurrent.updateDeleted", new Object[] {}, locale);
			throw new CustomException("projectDeleted", message);
		}
	}

	public void deleteProject(Project project) throws Exception {
		locale = LocaleContextHolder.getLocale();
		String message = messageSource.getMessage("concurrent.delete", new Object[] {}, locale);
		if (projectRepository.exists(project.getId()) && project.getStatus() == STATUS.NEW) {
			try {
				projectRepository.delete(project);
			} catch (OptimisticLockingFailureException e) {
				throw new CustomException("concurrentDelete", message);
			} catch (StaleObjectStateException s) {
				throw new CustomException("concurrentDelete", message);
			}
		} else {
			message = messageSource.getMessage("concurrent.projectDeleted", new Object[] {}, locale);
			throw new CustomException("projectDeleted", message);
		}
	}

	public void deleteProjects(List<Project> projects) throws Exception {
		for (Project project : projects) {
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
