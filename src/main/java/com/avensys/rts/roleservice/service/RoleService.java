package com.avensys.rts.roleservice.service;

import com.avensys.rts.roleservice.payloadrequest.RoleRequestDTO;
import com.avensys.rts.roleservice.payloadresponse.RoleListResponse;

import java.util.List;

public interface RoleService {
    /**
     * This method is used to create a role.
     * @param roleRequestDTO
     * @return
     */
    public RoleRequestDTO createRole(RoleRequestDTO roleRequestDTO);

    /**
     * This method is used to retrieve all role list
     * @return
     */
    public List<RoleListResponse>getRoleList();

    /**
     * This method is used to retrieve role information
     * @param id
     * @return
     */
    public RoleRequestDTO getRole(Integer id);

    /**
     * This method is used to update role information
     * @param roleRequestDTO
     * @param id
     * @return
     */
    public RoleRequestDTO updateRole(RoleRequestDTO roleRequestDTO,Integer id);

    /**
     * This method is used to delete role information
     * @param id
     */
    public void deleteRole(Integer id);
}
