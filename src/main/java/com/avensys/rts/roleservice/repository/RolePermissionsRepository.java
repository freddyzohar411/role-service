package com.avensys.rts.roleservice.repository;

import com.avensys.rts.roleservice.entity.RolePermissionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionsRepository extends JpaRepository<RolePermissionsEntity,Integer> {
}
