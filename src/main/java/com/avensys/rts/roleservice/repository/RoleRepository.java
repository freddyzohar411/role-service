package com.avensys.rts.roleservice.repository;

import com.avensys.rts.roleservice.entity.RoleEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity,Integer>, JpaSpecificationExecutor<RoleEntity> {
    Optional<RoleEntity> findByIdAndIsDeleted(Integer id, boolean isDeleted);
    @Query(value = "SELECT a FROM RoleEntity a WHERE a.isDeleted = ?1")
    List<RoleEntity> findAllAndIsDeleted(boolean isDeleted, Pageable pageable);


}
