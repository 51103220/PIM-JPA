package com.dedorewan.website.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import com.dedorewan.website.configuration.HibernateConfiguration;
import com.dedorewan.website.configuration.PIMConfiguration;
import com.dedorewan.website.dao.IEmployeeRepository;
import com.dedorewan.website.dao.IGroupRepository;
import com.dedorewan.website.dom.Project;
import com.dedorewan.website.dom.Project.STATUS;
import com.dedorewan.website.service.IProjectService;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration
@ContextConfiguration(classes = { PIMConfiguration.class,
		HibernateConfiguration.class })
public class DaoTesting {

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private IProjectService projectService;
	@Autowired
	IEmployeeRepository employeeRepository;

	@Autowired
	IGroupRepository groupRepository;

	@Test
	public void testSaveProject() {
		List<Project> projects = projectService.findAll();
		Integer size = projects.size();
		Project project = new Project(Long.valueOf(1), Long.valueOf(1), 1250, "TEST SAVE PROJECT", "NTT",
				STATUS.NEW, new Date(), new Date(), 10000);
		try {
			projectService.addProject(project);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		projects = projectService.findAll();
		assertEquals(size+1,projects.size());
	}

	@Test
	public void testUpdateProject() {

	}

	@Test
	public void testDeleteProject() {

	}

	@Test
	public void testConcurrentUpdateProject() {

	}

	@Test
	public void testNativeQueryByEntityManager() {

	}
}
