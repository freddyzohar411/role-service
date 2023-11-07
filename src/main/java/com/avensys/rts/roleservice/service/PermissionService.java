package com.avensys.rts.roleservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.avensys.rts.roleservice.entity.PermissionEntity;
import com.avensys.rts.roleservice.exception.ServiceException;
import com.avensys.rts.roleservice.repository.PermissionRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class PermissionService {

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private MessageSource messageSource;

	public void save(PermissionEntity permissionEntity) throws ServiceException {

		// add check for name exists in a DB
		if (permissionRepository.existsByPermissionName(permissionEntity.getPermissionName())) {
			throw new ServiceException(
					messageSource.getMessage("error.permissionnametaken", null, LocaleContextHolder.getLocale()));
		}
		permissionRepository.save(permissionEntity);
	}

	public void update(PermissionEntity permissionEntity) throws ServiceException {
		getById(permissionEntity.getId());

		// add check for name exists in a DB
		if (permissionRepository.existsByPermissionName(permissionEntity.getPermissionName())) {
			throw new ServiceException(
					messageSource.getMessage("error.permissionnametaken", null, LocaleContextHolder.getLocale()));
		}

		permissionRepository.save(permissionEntity);
	}

	public void delete(Long id) throws ServiceException {
		PermissionEntity dbPermission = getById(id);
		dbPermission.setIsDeleted(true);
		permissionRepository.save(dbPermission);
	}

	public PermissionEntity getById(Long id) throws ServiceException {
		if (id == null) {
			throw new ServiceException(
					messageSource.getMessage("error.provide.id", new Object[] { id }, LocaleContextHolder.getLocale()));
		}

		Optional<PermissionEntity> permission = permissionRepository.findById(id);
		if (permission.isPresent() && !permission.get().getIsDeleted()) {
			return permission.get();
		} else {
			throw new ServiceException(messageSource.getMessage("error.permissionnotfound", new Object[] { id },
					LocaleContextHolder.getLocale()));
		}
	}

	public List<PermissionEntity> fetchList() {
		return (List<PermissionEntity>) permissionRepository.findAll();
	}

}
