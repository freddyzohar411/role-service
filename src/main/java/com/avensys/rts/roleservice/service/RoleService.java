package com.avensys.rts.roleservice.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.avensys.rts.roleservice.entity.PermissionEntity;
import com.avensys.rts.roleservice.entity.RoleEntity;
import com.avensys.rts.roleservice.exception.ServiceException;
import com.avensys.rts.roleservice.payloadrequest.RoleRequestDTO;
import com.avensys.rts.roleservice.repository.PermissionRepository;
import com.avensys.rts.roleservice.repository.RoleRepository;
import com.avensys.rts.roleservice.search.role.RoleSpecificationBuilder;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class RoleService {

	private static final Logger LOG = LoggerFactory.getLogger(RoleService.class);

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private MessageSource messageSource;

	private RoleEntity mapRequestToEntity(RoleRequestDTO roleRequestDTO) {
		RoleEntity entity = new RoleEntity();
		entity.setRoleName(roleRequestDTO.getRoleName());
		entity.setRoleDescription(roleRequestDTO.getRoleDescription());
		return entity;
	}

	public void createRole(RoleRequestDTO roleRequestDTO) throws ServiceException {
		LOG.info("createRole started");

		// add check for role name exists in a DB
		if (roleRepository.existsByRoleName(roleRequestDTO.getRoleName())) {
			throw new ServiceException(
					messageSource.getMessage("error.rolenametaken", null, LocaleContextHolder.getLocale()));
		}

		RoleEntity roleEntity = mapRequestToEntity(roleRequestDTO);
		List<Long> permissionDTOList = roleRequestDTO.getPermissionDTOList();
		Set<PermissionEntity> permissions = new HashSet<PermissionEntity>();
		permissionDTOList.forEach(id -> {
			Optional<PermissionEntity> permissionEntity = permissionRepository.findById(id);
			if (permissionEntity.isPresent()) {
				permissions.add(permissionEntity.get());
			}
		});
		//roleEntity.setPermissions(permissions);
		roleRepository.save(roleEntity);
	}

	public void updateRole(RoleRequestDTO roleRequestDTO) throws ServiceException {
		RoleEntity roleEntity = getRoleById(roleRequestDTO.getId());

//		RoleEntity roleEntity = mapRequestToEntity(roleRequestDTO);
//		List<Long> permissionDTOList = roleRequestDTO.getPermissionDTOList();
//		permissionDTOList.forEach(id -> {
//			Optional<PermissionEntity> permissionEntity = permissionRepository.findById(id);
//			if (permissionEntity.isPresent()) {
//				roleEntity.getPermissions().add(permissionEntity.get());
//			}
//		});
//		roleRepository.save(roleEntity);
	}

	public void deleteRole(Long id) throws ServiceException {
		RoleEntity dbUser = getRoleById(id);
		dbUser.setIsDeleted(true);
		roleRepository.save(dbUser);
	}

	public RoleEntity getRoleById(Long id) throws ServiceException {
		if (id == null) {
			throw new ServiceException(
					messageSource.getMessage("error.provide.id", new Object[] { id }, LocaleContextHolder.getLocale()));
		}
		Optional<RoleEntity> role = roleRepository.findById(id);
		if (role.isPresent() && !role.get().getIsDeleted()) {
			return role.get();
		} else {
			throw new ServiceException(messageSource.getMessage("error.rolenotfound", new Object[] { id },
					LocaleContextHolder.getLocale()));
		}
	}

	public List<RoleEntity> getRoleList(Integer pageNo, Integer pageSize, String sortBy) {
		LOG.info("getRoleList request processing");
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		List<RoleEntity> roleEntityList = roleRepository.findAllAndIsDeleted(false, paging);
		return roleEntityList;
	}

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
