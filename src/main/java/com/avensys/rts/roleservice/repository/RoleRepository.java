package com.avensys.rts.roleservice.repository;

import com.avensys.rts.roleservice.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity,Integer> {

}
