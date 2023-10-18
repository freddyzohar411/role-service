package com.avensys.rts.roleservice.repository;

import com.avensys.rts.roleservice.entity.PermissionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionsEntity,Integer> {
}
