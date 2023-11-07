package com.avensys.rts.roleservice.service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
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
import com.avensys.rts.roleservice.entity.PermissionEntity;
import com.avensys.rts.roleservice.entity.RoleEntity;
import com.avensys.rts.roleservice.exception.ServiceException;
import com.avensys.rts.roleservice.payload.request.ModuleRequestDTO;
import com.avensys.rts.roleservice.payload.request.RoleRequestDTO;
import com.avensys.rts.roleservice.repository.ModuleRepository;
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
	private ModuleRepository moduleRepository;

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
		Set<ModuleEntity> modules = new HashSet<ModuleEntity>();

		List<ModuleRequestDTO> moduleRequestDTOs = roleRequestDTO.getModules();
		moduleRequestDTOs.forEach(module -> {
			Optional<ModuleEntity> moduleEntity = moduleRepository.findById(module.getId());
			if (moduleEntity.isPresent() && module.getPermissions() != null && module.getPermissions().size() > 0) {
				ModuleEntity moduleEntityOb = moduleEntity.get();
				Set<PermissionEntity> permissions = new HashSet<PermissionEntity>();
				System.out.println("module.getPermissions() " + module.getPermissions().size());
				module.getPermissions().forEach(permissionId -> {
					Optional<PermissionEntity> permissionEntity = permissionRepository.findById(permissionId);
					if (permissionEntity.isPresent()) {
						permissions.add(permissionEntity.get());
					}
				});
				moduleEntityOb.setPermissions(permissions);
				modules.add(moduleEntityOb);
			}
		});
		roleEntity.setModules(modules);
		roleRepository.save(roleEntity);
	}

	public void updateRole(RoleRequestDTO roleRequestDTO) throws ServiceException {
		RoleEntity roleEntity = getRoleById(roleRequestDTO.getId());

		// RoleEntity roleEntity = mapRequestToEntity(roleRequestDTO);
		// List<Long> permissionDTOList = roleRequestDTO.getPermissionDTOList();
		// permissionDTOList.forEach(id -> {
		// Optional<PermissionEntity> permissionEntity =
		// permissionRepository.findById(id);
		// if (permissionEntity.isPresent()) {
		// roleEntity.getPermissions().add(permissionEntity.get());
		// }
		// });
		// roleRepository.save(roleEntity);
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
		Page<RoleEntity> rolesPage = roleRepository.findAll(getSpecification(searchTerm, customView), pageable);
		return rolesPage;
	}

	private Specification<RoleEntity> getSpecification(String searchTerm, List<String> customView) {
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
			return criteriaBuilder.and(searchOrPredicates);
		};
	}

}
