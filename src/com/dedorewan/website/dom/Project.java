package com.dedorewan.website.dom;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.persistence.CascadeType;

@Entity
@Table(name = "PROJECT")
public class Project implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum STATUS {
		NEW("New"), PLA("Planned"), INP("In Progress"), FIN("Finished");
		String m_name;

		STATUS(String name) {
			m_name = name;
		}

		public String getValue() {
			return m_name;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE})
	@JoinColumn(name = "GROUP_ID", nullable = false)
	private Group group;

	@Transient
	private Long groupId;

	@Column(name = "PROJECT_NUMBER", nullable = false)
	private Integer projectNumber;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "CUSTOMER", nullable = false)
	private String customer;

	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private STATUS status;

	@Column(name = "START_DATE", nullable = false)
	private Date startDate;

	@Column(name = "END_DATE", nullable = true)
	private Date endDate;

	@Version
	@Column(name = "VERSION", nullable = false)
	private Integer version;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE})
	@JoinTable(name = "PROJECT_EMPLOYEE", joinColumns = { @JoinColumn(name = "PROJECT_ID", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "EMPLOYEE_ID", nullable = false) })
	private List<Employee> employees;

	@Transient
	private String[] members;

	public Project() {

	}

	public Project(Long id, Long groupId, Integer project_number, String name,
			String customer, STATUS status, Date start_date, Date end_date,
			Integer version) {
		this.id = id;
		this.groupId = groupId;
		this.projectNumber = project_number;
		this.name = name;
		this.customer = customer;
		this.version = version;
		this.status = status;
		this.startDate = start_date;
		this.endDate = end_date;
	}

	public Long getId() {
		return id;
	}

	public Group getGroup() {
		return this.group;
	}

	public List<Employee> getEmployees() {
		return this.employees;
	}

	public Long getGroupId() {
		if (group != null)
			return group.getId();
		else
			return this.groupId;
	}

	public Integer getProjectNumber() {
		return projectNumber;
	}

	public String getName() {
		return name;
	}

	public String getCustomer() {
		return customer;
	}

	public STATUS getStatus() {
		return status;
	}

	public Integer getVersion() {
		return version;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String[] getMembers() {
		return members;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public void setProjectNumber(Integer project_number) {
		this.projectNumber = project_number;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}

	public void setStartDate(Date date) {
		this.startDate = date;
	}

	public void setEndDate(Date date) {
		this.endDate = date;
	}

	public void setMembers(String[] members) {
		this.members = members;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public Boolean isNew() {
		if (this.status == STATUS.NEW) {
			return true;
		}
		return false;
	}

	public String membersToString() {
		StringBuilder builder = new StringBuilder("");

		Boolean first = true;
		for (Employee e : this.employees) {
			if (first) {
				builder.append(e.getVisa());
				first = false;
			} else {
				builder.append("," + e.getVisa());
			}
		}

		return builder.toString();
	}

	public void updateData(Project project) {
		this.groupId = project.getGroupId();
		this.customer = project.getCustomer();
		this.members = project.getMembers();
		this.endDate = project.getEndDate();
		this.startDate = project.getStartDate();
		this.name = project.getName();
		this.projectNumber = project.getProjectNumber();
		this.status = project.getStatus();
		this.group = project.getGroup();
		this.employees = project.getEmployees();
	}
}
