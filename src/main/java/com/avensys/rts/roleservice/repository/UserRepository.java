package com.avensys.rts.roleservice.repository;

import com.avensys.rts.roleservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {
}
