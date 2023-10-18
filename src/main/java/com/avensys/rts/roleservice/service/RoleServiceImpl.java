package com.avensys.rts.roleservice.service;

import com.avensys.rts.roleservice.entity.RoleEntity;
import com.avensys.rts.roleservice.entity.RolePermissionsEntity;
import com.avensys.rts.roleservice.entity.UserGroupRolesEntity;
import com.avensys.rts.roleservice.payload.PermissionDTO;
import com.avensys.rts.roleservice.payload.UserGroupDTO;
import com.avensys.rts.roleservice.payloadrequest.RoleRequestDTO;
import com.avensys.rts.roleservice.repository.PermissionRepository;
import com.avensys.rts.roleservice.repository.RolePermissionsRepository;
import com.avensys.rts.roleservice.repository.RoleRepository;
import com.avensys.rts.roleservice.repository.UserGroupRolesRepository;
import com.avensys.rts.roleservice.search.role.RoleSpecificationBuilder;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RoleServiceImpl implements RoleService  {
    private static final Logger LOG = LoggerFactory.getLogger(RoleServiceImpl.class);
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RolePermissionsRepository rolePermissionsRepository;
    @Autowired
    private UserGroupRolesRepository userGroupRolesRepository;
    @Override
    @Transactional
    public RoleEntity createRole(RoleRequestDTO roleRequestDTO){
        LOG.info("createRole started");
        RoleEntity roleEntity = mapRoleRequestDTOToRoleEntity(roleRequestDTO);
        RoleEntity save = roleRepository.save(roleEntity);
        Integer roleId = save.getId();
        List<UserGroupDTO> userGroupDTOList = roleRequestDTO.getUserGroupDTOList();
        int totalUserGroups=userGroupDTOList.size();
        for (int i=0;i<totalUserGroups;i++){
            UserGroupRolesEntity userGroupRolesEntity = new UserGroupRolesEntity();
            UserGroupDTO userGroupDTO = userGroupDTOList.get(i);
            userGroupRolesEntity.setUserGroupId(userGroupDTO.getId());
            userGroupRolesEntity.setRoleId(save.getId());
            userGroupRolesRepository.save(userGroupRolesEntity);
        }
       // List<UserDTO>userDTOList = roleRequestDTO.getUserDTOList();
        List<PermissionDTO> permissionDTOList = roleRequestDTO.getPermissionDTOList();
        int totalPermissions = permissionDTOList.size();
        for(int i =0;i<totalPermissions;i++){
            RolePermissionsEntity rolePermissionsEntity = new RolePermissionsEntity();
            PermissionDTO permissionDTO=permissionDTOList.get(i);
            rolePermissionsEntity.setPermissionId(permissionDTO.getId());
            rolePermissionsEntity.setRoleId(save.getId());
            rolePermissionsRepository.save(rolePermissionsEntity);
        }


      return save;
    }

    @Override
    public List<RoleEntity> getRoleList(Integer pageNo, Integer pageSize, LocalDateTime sortBy) {
        LOG.info("getRoleList request processing");
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("sortBy"));
        List<RoleEntity> roleEntityList = roleRepository.findAllAndIsDeleted(false,paging);
        return roleEntityList;
    }

    @Override
    public RoleEntity getRole(Integer id) {
        LOG.info("getRole request processing");
        RoleEntity roleEntity=roleRepository.findByIdAndIsDeleted(id,false).orElseThrow(
                () -> new EntityNotFoundException("Role with %s not found".formatted(id)));
        return roleEntity;
    }

    @Override
    public RoleEntity updateRole(RoleRequestDTO roleRequestDTO, Integer roleId) {
        LOG.info("updateRole request processing");
        RoleEntity roleEntity = roleRepository.findByIdAndIsDeleted(roleId,false).orElseThrow(
                () -> new EntityNotFoundException("Role with %s not found".formatted(roleId)));
        roleEntity = mapRequestToEntity(roleRequestDTO);
        RoleEntity save =roleRepository.save(roleEntity);
        roleRequestDTO.getUserGroupDTOList().clear();
        List<UserGroupDTO> userGroupDTOList = roleRequestDTO.getUserGroupDTOList();
        int totalUserGroups=userGroupDTOList.size();
        for (int i=0;i<totalUserGroups;i++){
            UserGroupRolesEntity userGroupRolesEntity = new UserGroupRolesEntity();
            UserGroupDTO userGroupDTO = userGroupDTOList.get(i);
            userGroupRolesEntity.setUserGroupId(userGroupDTO.getId());
            userGroupRolesEntity.setRoleId(save.getId());
            userGroupRolesRepository.save(userGroupRolesEntity);
        }
        roleRequestDTO.getPermissionDTOList().clear();
        List<PermissionDTO> permissionDTOList = roleRequestDTO.getPermissionDTOList();
        int totalPermissions = permissionDTOList.size();
        for(int i =0;i<totalPermissions;i++){
            RolePermissionsEntity rolePermissionsEntity = new RolePermissionsEntity();
            PermissionDTO permissionDTO=permissionDTOList.get(i);
            rolePermissionsEntity.setPermissionId(permissionDTO.getId());
            rolePermissionsEntity.setRoleId(save.getId());
            rolePermissionsRepository.save(rolePermissionsEntity);
        }

        return save;
    }

    @Override
    public void deleteRole(Integer id) {
        LOG.info("deleteRole request processing");
        RoleEntity roleEntity=roleRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Role with %s not found".formatted(id)));
        roleEntity.setDeleted(true);
        roleRepository.save(roleEntity);

    }

    private RoleEntity mapRoleRequestDTOToRoleEntity(RoleRequestDTO roleRequestDTO){
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleName(roleRequestDTO.getRoleName());
        roleEntity.setRoleDescription(roleRequestDTO.getRoleDescription());
        return roleRepository.save(roleEntity);
    }
    private RoleEntity mapRequestToEntity(RoleRequestDTO roleRequestDTO ){
        RoleEntity entity = new RoleEntity();
        entity.setRoleName(roleRequestDTO.getRoleName());
        entity.setRoleDescription(roleRequestDTO.getRoleDescription());
        return entity;
    }

    @Override
    public Page<RoleEntity> search(String search, Pageable pageable) {
        RoleSpecificationBuilder builder = new RoleSpecificationBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");

        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

       return roleRepository.findAll(builder.build(), pageable);

    }


}
