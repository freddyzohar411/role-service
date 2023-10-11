package com.avensys.rts.roleservice.service;

import com.avensys.rts.roleservice.entity.RoleEntity;
import com.avensys.rts.roleservice.payload.PermissionDTO;
import com.avensys.rts.roleservice.payload.UserDTO;
import com.avensys.rts.roleservice.payload.UserGroupDTO;
import com.avensys.rts.roleservice.payloadrequest.RoleRequestDTO;
import com.avensys.rts.roleservice.payloadresponse.RoleListResponse;
import com.avensys.rts.roleservice.repository.PermissionRepository;
import com.avensys.rts.roleservice.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleServiceImpl implements RoleService  {
    private static final Logger LOG = LoggerFactory.getLogger(RoleServiceImpl.class);
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Override
    public RoleRequestDTO createRole(RoleRequestDTO roleRequestDTO){
        LOG.info("createRole started");
        RoleRequestDTO requestDTO = new RoleRequestDTO();
        List<UserGroupDTO> userGroupDTOList = roleRequestDTO.getUserGroupDTOList();
        List<UserDTO>userDTOList = roleRequestDTO.getUserDTOList();
        List<PermissionDTO> permissionDTOList = roleRequestDTO.getPermissionDTOList();
        RoleEntity saveRole = mapRoleRequestDTOToRoleEntity(roleRequestDTO);

      return requestDTO;
    }

    @Override
    public List<RoleListResponse> getRoleList() {
        return null;
    }

    @Override
    public RoleRequestDTO getRole(Integer id) {
        return null;
    }

    @Override
    public RoleRequestDTO updateRole(RoleRequestDTO roleRequestDTO, Integer id) {
        return null;
    }

    @Override
    public void deleteRole(Integer id) {

    }

    private RoleEntity mapRoleRequestDTOToRoleEntity(RoleRequestDTO roleRequestDTO){
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleName(roleRequestDTO.getRoleName());
        roleEntity.setRoleDescription(roleRequestDTO.getRoleDescription());
        return roleRepository.save(roleEntity);
    }

}
