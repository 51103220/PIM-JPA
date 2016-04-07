package com.dedorewan.website.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dedorewan.website.dom.Group;

@Repository
public interface IGroupRepository extends JpaRepository<Group, Long> {
}
