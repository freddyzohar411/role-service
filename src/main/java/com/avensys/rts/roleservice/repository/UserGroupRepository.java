package com.avensys.rts.roleservice.repository;

import com.avensys.rts.roleservice.entity.UserGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroupEntity,Integer> {
}
