package com.avensys.rts.roleservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.avensys.rts.roleservice.entity.RoleEntity;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long>, JpaSpecificationExecutor<RoleEntity> {

	Boolean existsByRoleName(String roleName);

	Optional<RoleEntity> findByRoleName(String roleName);

	Optional<RoleEntity> findByIdAndIsDeleted(Long id, boolean isDeleted);

	@Query(value = "SELECT role FROM RoleEntity role WHERE role.isDeleted = ?1")
	List<RoleEntity> findAllAndIsDeleted(boolean isDeleted, Pageable pageable);

	@Query(value = "SELECT r from RoleEntity r WHERE r.isDeleted = ?1 AND r.isActive = ?2")
	Page<RoleEntity> findAllByPaginationAndSort(Boolean isDeleted, Boolean isActive, Pageable pageable);

	Page<RoleEntity> findAll(Specification<RoleEntity> specification, Pageable pageable);
}
