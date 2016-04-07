package com.dedorewan.website.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dedorewan.website.dom.Project;
import com.dedorewan.website.dom.Project.STATUS;

public class IProjectRepositoryImpl implements IProjectRepositoryCustom {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	IEmployeeRepository employeeRepository;

	@Autowired
	IGroupRepository groupRepository;

	@Autowired
	private SessionFactory sessionFactory;

	@Value("${projects.maxProjectPerPage}")
	Integer projectsPerPage;


	@SuppressWarnings("unchecked")
	public List<Project> filterProjects(String keywords, STATUS statusKey) {
		List<Project> searchResult = new ArrayList<Project>();
		String queryKeywords;
		String queryStatus;
		String order = " order by project_number ASC";
		if (!keywords.isEmpty()) {
			if (statusKey != null) {
				queryStatus = " and status = '" + statusKey + "'";
			} else {
				queryStatus = " ";
			}
			if (keywords.matches("^[0-9]+")) {
				Integer projectNumber = -1;
				projectNumber = Integer.parseInt(keywords);
				queryKeywords = "select p from Project p where project_number = "
						+ projectNumber;

			} else {
				keywords = keywords.toLowerCase();
				queryKeywords = "select p from Project p where (LOWER(name) like '%"
						+ keywords
						+ "%' or LOWER(customer) like '%"
						+ keywords
						+ "%')";
			}
		} else {
			queryKeywords = "";
			queryStatus = "select p from Project p where status = '"
					+ statusKey + "'";
		}
		searchResult = (List<Project>) entityManager.createQuery(
				queryKeywords + queryStatus + order).getResultList();
		return searchResult;
	}

	public List<Project> projectsInPage(List<Project> pList, Integer page) {
		List<Project> projects = new ArrayList<Project>();
		Integer start_index = (page - 1) * projectsPerPage;
		Integer end_index = page * projectsPerPage;
		for (Integer i = 0; i < pList.size(); i++) {
			if (i >= start_index && i < end_index) {
				projects.add(pList.get(i));
			}
		}
		return projects;
	}

	public Integer numberPages(List<Project> projects, Integer maxProjects) {
		Integer pages = 0;
		if (projects.size() % maxProjects == 0) {
			pages = projects.size() / maxProjects;
		} else {
			pages = projects.size() / maxProjects + 1;
		}
		return pages;
	}

	public boolean visaExsisted(String visa) {
		if (employeeRepository.findByVisa(visa).size() > 0) {
			return true;
		}
		return false;
	}

	public void insert(Project project) throws Exception {
		project.setVersion(2050512000);
		project.setGroup(groupRepository.getOne(project.getGroupId()));
		project.setEmployees(employeeRepository.getAllEmployeeByVisa(project
				.getMembers()));
		Session session = sessionFactory.getCurrentSession();
		session.merge(project);
	}

	public void update(Project project) throws Exception {
		project.setGroup(groupRepository.getOne(project.getGroupId()));
		project.setEmployees(employeeRepository.getAllEmployeeByVisa(project
				.getMembers()));
		Session session = sessionFactory.getCurrentSession();
		session.merge(project);
	}

	public void delete(Long id) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		Project existing_project = (Project) session.get(Project.class, id);
		if (existing_project != null
				&& existing_project.getStatus() == STATUS.NEW) {
			existing_project.setGroup(null);
			existing_project.getEmployees().clear();
			session.delete(existing_project);
		}

	}
}
