package com.avensys.rts.roleservice.service;

import com.avensys.rts.roleservice.entity.RoleEntity;
import com.avensys.rts.roleservice.payloadrequest.RoleRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface RoleService {
    /**
     * This method is used to create a role.
     * @param roleRequestDTO
     * @return
     */
    public RoleEntity createRole(RoleRequestDTO roleRequestDTO);

    /**
     * This method is used to retrieve all role list
     * @return
     */
    public List<RoleEntity>getRoleList(Integer pageNo, Integer pageSize, LocalDateTime sortBy);

    /**
     * This method is used to retrieve role information
     * @param id
     * @return
     */
    public RoleEntity getRole(Integer id);

    /**
     * This method is used to update role information
     * @param roleRequestDTO
     * @param id
     * @return
     */
    public RoleEntity updateRole(RoleRequestDTO roleRequestDTO,Integer id);

    /**
     * This method is used to delete role information
     * @param id
     */
    public void deleteRole(Integer id);

    public Page<RoleEntity> search(String search, Pageable pageable);
}
