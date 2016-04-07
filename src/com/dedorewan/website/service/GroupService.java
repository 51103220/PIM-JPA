package com.dedorewan.website.service;

import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dedorewan.website.dao.IGroupRepository;
import com.dedorewan.website.dom.Group;

@Service
@Transactional
public class GroupService implements IGroupService {
	@Autowired
	private IGroupRepository groupRepository;

	public HashMap<Long, String> groupLeaders() {
		List<Group> groups = groupRepository.findAll();
		HashMap<Long, String> leaders = new HashMap<Long, String>();
		for (Group group : groups) {
			leaders.put(group.getId(), group.getLeader().getVisa());
		}
		return leaders;
	}
}
