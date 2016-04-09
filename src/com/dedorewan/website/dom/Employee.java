package com.dedorewan.website.dom;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "EMPLOYEE")
public class Employee implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "VISA", unique = true, nullable = false)
	private String visa;

	@Column(name = "FIRST_NAME", nullable = false)
	private String firstName;

	@Column(name = "LAST_NAME", nullable = false)
	private String lastName;

	@Column(name = "BIRTH_DATE", nullable = false)
	private Date birthDate;

	@Version
	@Column(name = "VERSION", nullable = false)
	private Long version;
	@JsonIgnore
	@ManyToMany(mappedBy = "employees")
	private List<Project> projects;

	@Transient
	private String fullName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVisa() {
		return visa;
	}

	public void setVisa(String visa) {
		this.visa = visa;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String name) {
		this.firstName = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String name) {
		this.lastName = name;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date date) {
		this.birthDate = date;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public List<Project> getProjects() {
		return this.projects;
	}

	public void setProject(List<Project> projects) {
		this.projects = projects;
	}

	public Employee() {

	}

	public Employee(Long id, String visa, String firstName, String lastName,
			Date birthDate, Long version) {
		this.id = id;
		this.visa = visa;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.version = version;
		this.fullName = firstName + " " + lastName;

	}

	public void setFullName(String fullname) {
		this.fullName = fullname;
	}

	public String getFullName() {
		return this.fullName;
	}
}
