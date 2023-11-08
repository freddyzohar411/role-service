package com.avensys.rts.roleservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.avensys.rts.roleservice.entity.ModuleEntity;

@Repository
public interface ModuleRepository extends JpaRepository<ModuleEntity, Long> {
	Boolean existsByModuleName(String moduleName);

	Optional<ModuleEntity> findByModuleName(String moduleName);
}
