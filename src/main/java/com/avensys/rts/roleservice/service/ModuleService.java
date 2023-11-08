package com.avensys.rts.roleservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.avensys.rts.roleservice.entity.ModuleEntity;
import com.avensys.rts.roleservice.exception.ServiceException;
import com.avensys.rts.roleservice.repository.ModuleRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class ModuleService {

	@Autowired
	private ModuleRepository moduleRepository;

	@Autowired
	private MessageSource messageSource;

	public void save(ModuleEntity moduleEntity) throws ServiceException {

		// add check for name exists in a DB
		if (moduleRepository.existsByModuleName(moduleEntity.getModuleName())) {
			throw new ServiceException(
					messageSource.getMessage("error.modulenametaken", null, LocaleContextHolder.getLocale()));
		}

		moduleRepository.save(moduleEntity);
	}

	public void update(ModuleEntity moduleEntity) throws ServiceException {
		getById(moduleEntity.getId());

		// add check for name exists in a DB
		if (moduleRepository.existsByModuleName(moduleEntity.getModuleName())) {
			throw new ServiceException(
					messageSource.getMessage("error.modulenametaken", null, LocaleContextHolder.getLocale()));
		}

		moduleRepository.save(moduleEntity);
	}

	public void delete(Long id) throws ServiceException {
		ModuleEntity dbPermission = getById(id);
		dbPermission.setIsDeleted(true);
		moduleRepository.save(dbPermission);
	}

	public ModuleEntity getById(Long id) throws ServiceException {
		if (id == null) {
			throw new ServiceException(
					messageSource.getMessage("error.provide.id", new Object[] { id }, LocaleContextHolder.getLocale()));
		}

		Optional<ModuleEntity> permission = moduleRepository.findById(id);
		if (permission.isPresent() && !permission.get().getIsDeleted()) {
			return permission.get();
		} else {
			throw new ServiceException(messageSource.getMessage("error.modulenotfound", new Object[] { id },
					LocaleContextHolder.getLocale()));
		}
	}

	public List<ModuleEntity> fetchList() {

//		return (List<ModuleEntity>) moduleRepository.findAll();
		return moduleRepository.findAll();
	}

}
