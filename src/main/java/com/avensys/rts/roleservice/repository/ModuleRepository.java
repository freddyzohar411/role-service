package com.avensys.rts.roleservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.avensys.rts.roleservice.entity.ModuleEntity;

@Repository
public interface ModuleRepository extends CrudRepository<ModuleEntity, Long> {
	Boolean existsByModuleName(String moduleName);
}
