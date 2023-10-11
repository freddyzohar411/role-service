package com.avensys.rts.roleservice.repository;

import com.avensys.rts.roleservice.entity.UserGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroupEntity,Integer> {
}
