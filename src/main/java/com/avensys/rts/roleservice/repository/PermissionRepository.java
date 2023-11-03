package com.avensys.rts.roleservice.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.avensys.rts.roleservice.entity.PermissionEntity;

@Repository
public interface PermissionRepository extends CrudRepository<PermissionEntity, Long> {
	Boolean existsByPermissionName(String permissionName);

	Optional<PermissionEntity> findByPermissionName(String permissionName);
}
