package com.dedorewan.website.dom;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
@Table(name = "GROUPS")
public class Group implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Transient
	private Long groupLeaderId;
	
	@Version
	@Column(name = "VERSION", nullable = false)
	Long version;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GROUP_LEADER_ID", nullable = false)
	private Employee leader;
	
	@OneToMany(mappedBy = "group")
	private List<Project> projects;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGroupLeaderId() {
		return groupLeaderId;
	}

	public void setGroupLeaderId(Long id) {
		this.groupLeaderId = id;
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

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public Group() {

	}

	public void setLeader(Employee leader) {
		this.leader = leader;
	}

	public Employee getLeader() {
		return this.leader;
	}

	public Group(Long id, Long groupLeaderId, Long version) {
		this.id = id;
		this.groupLeaderId = groupLeaderId;
		this.version = version;
	}
}
