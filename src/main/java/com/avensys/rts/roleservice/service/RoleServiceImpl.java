package com.avensys.rts.roleservice.service;

import com.avensys.rts.roleservice.entity.RoleEntity;
import com.avensys.rts.roleservice.payload.PermissionDTO;
import com.avensys.rts.roleservice.payload.UserDTO;
import com.avensys.rts.roleservice.payload.UserGroupDTO;
import com.avensys.rts.roleservice.payloadrequest.RoleRequestDTO;
import com.avensys.rts.roleservice.repository.PermissionRepository;
import com.avensys.rts.roleservice.repository.RoleRepository;
import com.avensys.rts.roleservice.search.role.RoleSpecificationBuilder;
import jakarta.persistence.EntityNotFoundException;
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
    @Override
    public RoleEntity createRole(RoleRequestDTO roleRequestDTO){
        LOG.info("createRole started");
        List<UserGroupDTO> userGroupDTOList = roleRequestDTO.getUserGroupDTOList();
        List<UserDTO>userDTOList = roleRequestDTO.getUserDTOList();
        List<PermissionDTO> permissionDTOList = roleRequestDTO.getPermissionDTOList();
        RoleEntity saveRole = mapRoleRequestDTOToRoleEntity(roleRequestDTO);

      return saveRole;
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
    public RoleEntity updateRole(RoleRequestDTO roleRequestDTO, Integer id) {
        LOG.info("updateRole request processing");
        RoleEntity roleEntity = roleRepository.findByIdAndIsDeleted(id,false).orElseThrow(
                () -> new EntityNotFoundException("Role with %s not found".formatted(id)));
        roleEntity = mapRequestToEntity(roleRequestDTO);

        return roleRepository.save(roleEntity);
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
