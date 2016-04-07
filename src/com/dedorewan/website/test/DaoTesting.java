package com.dedorewan.website.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.dedorewan.website.configuration.HibernateConfiguration;
import com.dedorewan.website.configuration.PIMConfiguration;
import com.dedorewan.website.dom.Project;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration
@ContextConfiguration(classes = { PIMConfiguration.class,
		HibernateConfiguration.class })
@WebAppConfiguration
public class DaoTesting {

	@Autowired
	private SessionFactory sessionFactory;

	@Test
	public void testSaveProject() {

	}

	@Test
	public void testUpdateProject() {

	}

	@Test
	public void testConcurrentUpdateProject() {
		Session session = sessionFactory.openSession();
		Project project = (Project) session.get(Project.class,
				Long.valueOf(11), new LockOptions(LockMode.OPTIMISTIC));
		try {
			Transaction t = session.beginTransaction();
			Project _project = (Project) session.get(Project.class,
					Long.valueOf(11), new LockOptions(LockMode.OPTIMISTIC));
			_project.setCustomer("New Name For _Project ");
			project.setCustomer("New Name For Project");
			session.merge(project);
			session.merge(_project);
			t.commit();
			fail("It should have thrown OptimisticEntityLockException!");
		} catch (OptimisticEntityLockException expected) {
			expected.printStackTrace();
		} finally {
			session.close();
		}

	}

	@Test
	public void testGetAndLoad() {
		Session session = sessionFactory.openSession();
		Project project = (Project) session.load(Project.class,
				Long.valueOf(12), new LockOptions(LockMode.OPTIMISTIC));
		Transaction t = session.beginTransaction();
		project.setCustomer("Michael Jackson");
		session.persist(project);
		t.commit();
		Project _project = (Project) session.get(Project.class,
				Long.valueOf(12), new LockOptions(LockMode.OPTIMISTIC));
		assertEquals(_project.getCustomer(), "Michael Jackson");
		session.close();
	}

	@Test
	public void testGetSameEntityWithSameId() {
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();
		Project project = (Project) session
				.get(Project.class, Long.valueOf(13));
		Project anotherproject = (Project) session.get(Project.class,
				Long.valueOf(13));
		Project _project = (Project) session.get(Project.class,
				Long.valueOf(13));
		project.setCustomer("Michael Jackson");
		_project.setCustomer("DEDO");
		anotherproject.setCustomer("REWAN");
		t.commit();
		session.close();
	}
}
