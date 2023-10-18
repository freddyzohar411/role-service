package com.avensys.rts.roleservice.repository;

import com.avensys.rts.roleservice.entity.UserGroupRolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRolesRepository extends JpaRepository<UserGroupRolesEntity,Integer> {
}
