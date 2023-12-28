package com.avensys.rts.roleservice.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.avensys.rts.roleservice.entity.ModuleEntity;
import com.avensys.rts.roleservice.entity.RoleEntity;
import com.avensys.rts.roleservice.entity.RoleModulePermissionsEntity;
import com.avensys.rts.roleservice.exception.ServiceException;
import com.avensys.rts.roleservice.payload.request.ModuleRequestDTO;
import com.avensys.rts.roleservice.payload.request.RoleRequestDTO;
import com.avensys.rts.roleservice.repository.ModuleRepository;
import com.avensys.rts.roleservice.repository.RoleRepository;
import com.avensys.rts.roleservice.search.role.RoleSpecificationBuilder;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

@Transactional
@Service
public class RoleService {

	private static final Logger LOG = LoggerFactory.getLogger(RoleService.class);

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ModuleRepository moduleRepository;

	@Autowired
	private MessageSource messageSource;

	private RoleEntity mapRequestToEntity(RoleRequestDTO roleRequestDTO) {
		RoleEntity entity = new RoleEntity();
		if (roleRequestDTO.getId() != null) {
			entity.setId(roleRequestDTO.getId());
		}
		entity.setIsActive(true);
		entity.setIsDeleted(false);
		entity.setRoleName(roleRequestDTO.getRoleName());
		entity.setRoleDescription(roleRequestDTO.getRoleDescription());
		return entity;
	}

	public void createRole(RoleRequestDTO roleRequestDTO) throws ServiceException {
		LOG.info("createRole started ");

		// add check for role name exists in a DB
		if (roleRepository.existsByRoleName(roleRequestDTO.getRoleName())) {
			throw new ServiceException(
					messageSource.getMessage("error.rolenametaken", null, LocaleContextHolder.getLocale()));
		}

		RoleEntity roleEntity = mapRequestToEntity(roleRequestDTO);

		List<ModuleRequestDTO> moduleRequestDTOs = roleRequestDTO.getModules();

		Set<RoleModulePermissionsEntity> roleModulePermissions = new HashSet<RoleModulePermissionsEntity>();

		moduleRequestDTOs.forEach(module -> {
			Optional<ModuleEntity> moduleEntity = moduleRepository.findById(module.getId());
			if (moduleEntity.isPresent() && module.getPermissions() != null && module.getPermissions().size() > 0) {
				String permissions = StringUtils.join(module.getPermissions(), ',');

				RoleModulePermissionsEntity roleModulePermissionsEntity = new RoleModulePermissionsEntity();
				roleModulePermissionsEntity.setRole(roleEntity);
				roleModulePermissionsEntity.setModule(moduleEntity.get());
				roleModulePermissionsEntity.setPermissions(permissions);
				roleModulePermissionsEntity.setCreatedBy(roleRequestDTO.getCreatedBy());
				roleModulePermissionsEntity.setUpdatedBy(roleRequestDTO.getUpdatedBy());
				roleModulePermissions.add(roleModulePermissionsEntity);
			}
		});
		roleEntity.setModulePermissions(roleModulePermissions);
		roleEntity.setCreatedBy(roleRequestDTO.getCreatedBy());
		roleEntity.setUpdatedBy(roleRequestDTO.getUpdatedBy());
		roleRepository.save(roleEntity);
	}

	public void updateRole(RoleRequestDTO roleRequestDTO) throws ServiceException {
		// check for role name exists in a DB

		Optional<RoleEntity> dbRole = roleRepository.findByRoleName(roleRequestDTO.getRoleName());

		if (dbRole.isPresent() && dbRole.get().getId() != roleRequestDTO.getId()) {
			throw new ServiceException(
					messageSource.getMessage("error.rolenametaken", null, LocaleContextHolder.getLocale()));
		}

		RoleEntity roleEntity = getRoleById(roleRequestDTO.getId());
		roleEntity.setIsActive(true);
		roleEntity.setIsDeleted(false);
		roleEntity.setRoleName(roleRequestDTO.getRoleName());
		roleEntity.setRoleDescription(roleRequestDTO.getRoleDescription());

		List<ModuleRequestDTO> moduleRequestDTOs = roleRequestDTO.getModules();

		Set<RoleModulePermissionsEntity> roleModulePermissions = roleEntity.getModulePermissions();
		moduleRequestDTOs.forEach(module -> {
			Optional<ModuleEntity> moduleEntity = moduleRepository.findById(module.getId());

			if (moduleEntity.isPresent() && module.getPermissions() != null && module.getPermissions().size() > 0) {
				String permissions = StringUtils.join(module.getPermissions(), ',');

				RoleModulePermissionsEntity roleModulePermissionsEntity = null;

				Optional<RoleModulePermissionsEntity> rpm = roleModulePermissions.stream()
						.filter(data -> data.getModule().getId() == module.getId()).findFirst();
				if (rpm.isPresent()) {
					roleModulePermissionsEntity = rpm.get();
					roleModulePermissionsEntity.setPermissions(permissions);
					roleModulePermissionsEntity.setUpdatedBy(roleRequestDTO.getUpdatedBy());
				} else {
					roleModulePermissionsEntity = new RoleModulePermissionsEntity();
					roleModulePermissionsEntity.setRole(roleEntity);
					roleModulePermissionsEntity.setModule(moduleEntity.get());
					roleModulePermissionsEntity.setPermissions(permissions);
					roleModulePermissionsEntity.setCreatedBy(roleRequestDTO.getCreatedBy());
					roleModulePermissionsEntity.setUpdatedBy(roleRequestDTO.getUpdatedBy());
					roleModulePermissions.add(roleModulePermissionsEntity);
				}
			}

			// Added by Hx - Remove all module permission is length is 0. Get error how to
			// remove permission
			if (moduleEntity.isPresent() && module.getPermissions() != null && module.getPermissions().size() == 0) {
				RoleModulePermissionsEntity roleModulePermissionsEntity = null;
				Optional<RoleModulePermissionsEntity> rpm = roleModulePermissions.stream()
						.filter(data -> data.getModule().getId() == module.getId()).findFirst();
				if (rpm.isPresent()) {
					roleModulePermissionsEntity = rpm.get();
					roleModulePermissionsEntity.setPermissions("");
					roleModulePermissionsEntity.setUpdatedBy(roleRequestDTO.getUpdatedBy());
				}
			}

		});
		roleEntity.setModulePermissions(roleModulePermissions);
		roleEntity.setUpdatedBy(roleRequestDTO.getUpdatedBy());
		roleRepository.save(roleEntity);
	}

	public void deleteRole(Long id) throws ServiceException {
		RoleEntity dbUser = getRoleById(id);
		dbUser.setIsDeleted(true);
		dbUser.setIsActive(false);
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

	public Page<RoleEntity> getUserGroupListingPage(Integer page, Integer size, String sortBy, String sortDirection) {
		Sort sort = null;
		if (sortBy != null) {
			// Get direction based on sort direction
			Sort.Direction direction = Sort.DEFAULT_DIRECTION;
			if (sortDirection != null) {
				direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
			}
			sort = Sort.by(direction, sortBy);
		} else {
			sort = Sort.by(Sort.Direction.DESC, "updatedAt");
		}
		Pageable pageable = null;
		if (page == null && size == null) {
			pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
		} else {
			pageable = PageRequest.of(page, size, sort);
		}
		Page<RoleEntity> rolesPage = roleRepository.findAllByPaginationAndSort(false, true, pageable);
		return rolesPage;
	}

	public Page<RoleEntity> getUserGroupListingPageWithSearch(Integer page, Integer size, String sortBy,
			String sortDirection, String searchTerm) {
		Sort sort = null;
		if (sortBy != null) {
			// Get direction based on sort direction
			Sort.Direction direction = Sort.DEFAULT_DIRECTION;
			if (sortDirection != null) {
				direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
			}
			sort = Sort.by(direction, sortBy);
		} else {
			sort = Sort.by(Sort.Direction.DESC, "updatedAt");
		}

		Pageable pageable = null;
		if (page == null && size == null) {
			pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
		} else {
			pageable = PageRequest.of(page, size, sort);
		}
		// Dynamic search based on custom view (future feature)
		List<String> customView = List.of("roleName", "roleDescription");
		Page<RoleEntity> rolesPage = roleRepository.findAll(getSpecification(searchTerm, customView, false, true),
				pageable);
		return rolesPage;
	}

	private Specification<RoleEntity> getSpecification(String searchTerm, List<String> customView, Boolean isDeleted,
			Boolean isActive) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			// Custom fields you want to search in
			for (String field : customView) {
				Path<Object> fieldPath = root.get(field);
				if (fieldPath.getJavaType() == Integer.class) {
					try {
						Integer id = Integer.parseInt(searchTerm);
						predicates.add(criteriaBuilder.equal(fieldPath, id));
					} catch (NumberFormatException e) {
						// Ignore if it's not a valid integer
					}
				} else {
					predicates.add(criteriaBuilder.like(criteriaBuilder.lower(fieldPath.as(String.class)),
							"%" + searchTerm.toLowerCase() + "%"));
				}
			}

			Predicate searchOrPredicates = criteriaBuilder.or(predicates.toArray(new Predicate[0]));

			List<Predicate> fixPredicates = new ArrayList<>();
			// Add conditions for isDeleted and isActive
			fixPredicates.add(criteriaBuilder.equal(root.get("isDeleted"), isDeleted));
			fixPredicates.add(criteriaBuilder.equal(root.get("isActive"), isActive));

			// Combine all predicates with AND
			Predicate finalPredicate = criteriaBuilder.and(searchOrPredicates,
					criteriaBuilder.and(fixPredicates.toArray(new Predicate[0])));

			return finalPredicate;
		};
	}

}
