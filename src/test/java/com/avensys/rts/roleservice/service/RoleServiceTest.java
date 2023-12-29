package com.avensys.rts.roleservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.avensys.rts.roleservice.entity.BaseEntity;
import com.avensys.rts.roleservice.entity.ModuleEntity;
import com.avensys.rts.roleservice.entity.RoleEntity;
import com.avensys.rts.roleservice.entity.RoleModulePermissionsEntity;
import com.avensys.rts.roleservice.entity.UserEntity;
import com.avensys.rts.roleservice.entity.UserGroupEntity;
import com.avensys.rts.roleservice.payload.request.ModuleRequestDTO;
import com.avensys.rts.roleservice.payload.request.RoleListingRequestDTO;
import com.avensys.rts.roleservice.payload.request.RoleRequestDTO;
import com.avensys.rts.roleservice.repository.ModuleRepository;
import com.avensys.rts.roleservice.repository.RoleRepository;
import com.avensys.rts.roleservice.search.role.RoleSpecificationBuilder;

public class RoleServiceTest {

	@Mock
	private RoleService roleService;
	@Mock
	private RoleRepository roleRepository;
	@Mock
	private ModuleRepository moduleRepository;
	private MessageSource messageSource;
	AutoCloseable autoCloseable;
	@Mock
	RoleRequestDTO roleRequestDTO;
	RoleListingRequestDTO roleListingRequestDTO;
	// Entities
	BaseEntity baseEntity;
	RoleEntity roleEntity;
	RoleEntity roleEntity1;
	ModuleEntity moduleEntity;
	UserEntity userEntity;
	UserGroupEntity userGroupEntity;
	RoleModulePermissionsEntity roleModulePermissionsEntity;
	RoleModulePermissionsEntity roleModulePermissionsEntity1;
	ModuleRequestDTO moduleRequestDTO;
	ModuleRequestDTO moduleRequestDTO1;
	ModuleRequestDTO moduleRequestDTO2;
	List<Long> permissionsList;
	List<ModuleRequestDTO> modules;
	Set<RoleModulePermissionsEntity> modulePermissions;
	Set<UserGroupEntity> groupEntities;
	Set<UserEntity> users;
	Set<RoleEntity> roleEntities;
	Optional<RoleEntity> role;
	Pageable pageable = null;
	List<RoleEntity> roleEntityList;
	Page<RoleEntity> rolesPage;
	Sort sort = null;
	RoleSpecificationBuilder builder;
	List<String> customView;
	Specification<RoleEntity> Specification;

	@BeforeEach
	void setUp() {
		autoCloseable = MockitoAnnotations.openMocks(this);
		userEntity = new UserEntity(1L, "339f35a7-0d3d-431e-9a63-d90d4c342e4a", "Kotai", "Nalleb",
				"kittu1@aven-sys.com", "kittu1@aven-sys.com",
				"$2a$10$pxSQVx/EqvfrehZDdN6Q3.Qg3Agm2S/d60xYqy0rFpuNSgt1DcpvO", "+91-9381515362", "852", false, true,
				groupEntities);
		userGroupEntity = new UserGroupEntity(1L, "Super Admin Group", "Super Admin Description", users, roleEntities);
		roleModulePermissionsEntity = new RoleModulePermissionsEntity(1L, moduleEntity, roleEntity, "permissions");
		roleModulePermissionsEntity1 = new RoleModulePermissionsEntity(2L, moduleEntity, roleEntity, "permissions");
		modulePermissions = new HashSet<RoleModulePermissionsEntity>();
		modulePermissions.add(roleModulePermissionsEntity);
		modulePermissions.add(roleModulePermissionsEntity1);
		roleEntity = new RoleEntity(1L, "Super Admin", "Super admin access to all modules!", modulePermissions,
				groupEntities);
		roleEntity1 = new RoleEntity(2L, "Super Admin1", "Super admin access to all modules1!", modulePermissions,
				groupEntities);
		permissionsList = Arrays.asList(1L, 2L, 3L, 4L);
		moduleRequestDTO = new ModuleRequestDTO(1L, permissionsList);
		moduleRequestDTO1 = new ModuleRequestDTO(2L, permissionsList);
		modules = Arrays.asList(moduleRequestDTO, moduleRequestDTO1);
		roleRequestDTO = new RoleRequestDTO(1L, "Super Admin", "Super admin access to all modules!", modules, 1L, 1L);
		// modules.add(moduleRequestDTO);
		// modules.add(moduleRequestDTO1);

		// moduleRequestDTO2 = new ModuleRequestDTO(1L,permissionsList);
		roleService = new RoleService(roleRepository, messageSource, moduleRepository);

		// role = Optional.of(roleRequestDTO);
		role = Optional.of(roleEntity);
		// role.of(roleEntity);
		roleRepository.save(roleEntity);
		role = roleRepository.findById(1L);
		roleEntityList = Arrays.asList(roleEntity, roleEntity1);
		builder = new RoleSpecificationBuilder();
		customView = Arrays.asList("roleName", "roleDesc");
		

		// rolesPage.isEmpty()
		// rolesPage.toList(roleEntity);
		// rolesPage.and(roleEntity);
		// roleService = new RoleService(messageSource);
//		modules.add(moduleRequestDTO);
//		modules.add(moduleRequestDTO1);
//		modules.add(moduleRequestDTO2);
	}

	@AfterEach
	void tearDown() throws Exception {
		userEntity = null;
		userGroupEntity = null;
		roleModulePermissionsEntity = null;
		roleEntity = null;
		roleRequestDTO = null;
		modules = null;
		autoCloseable.close();

	}

	@Test
	void testCreateRole() throws Exception {
		mock(RoleRequestDTO.class);
		mock(RoleRepository.class);
		mock(RoleEntity.class);
		mock(ModuleRequestDTO.class);
		mock(RoleModulePermissionsEntity.class);
		mock(ModuleEntity.class);
		mock(ModuleRepository.class);
		// assertThat(modules.isEmpty());
		roleService.createRole(roleRequestDTO);
		moduleRepository.findById(modules.get(1).getId());
		roleRepository.save(roleEntity);
		assertThat(1L).isEqualTo(modules.get(0).getId());
		assertThat(roleRequestDTO.getRoleName()).isEqualTo(roleEntity.getRoleName());

	}

	
	/*
	 * @Test void testMapRequestToEntity() throws Exception {
	 * mock(RoleEntity.class); mock(RoleRequestDTO.class); mock(RoleService.class);
	 * RoleService roleService = new RoleService(roleRepository, messageSource,
	 * moduleRepository); Method mapRequestToEntity =
	 * RoleService.class.getDeclaredMethod("mapRequestToEntity",
	 * RoleRequestDTO.class); mapRequestToEntity.setAccessible(true); RoleEntity
	 * roleEntityResult = (RoleEntity) mapRequestToEntity.invoke(roleService,
	 * roleRequestDTO); assertEquals(roleEntityResult, roleEntity); }
	 */

	@Test
	void testUpdateRole() throws Exception {
		mock(RoleRequestDTO.class);
		mock(RoleRepository.class);
		mock(RoleEntity.class);
		mock(ModuleRequestDTO.class);
		mock(RoleModulePermissionsEntity.class);
		mock(ModuleEntity.class);
		mock(ModuleRepository.class);
		roleRepository.findByRoleName(roleRequestDTO.getRoleName());
		moduleRepository.findById(modules.get(1).getId());
		when(roleRepository.findById(roleEntity.getId())).thenReturn(Optional.ofNullable(roleEntity));
		roleService.updateRole(roleRequestDTO);
		// assertThat(roleService.updateRole(roleRequestDTO)).isEqualTo(roleEntity);
		// when
		// (roleRepository.findByRoleName(roleEntity.getRoleName())).thenReturn(role)
		// roleService.updateRole(roleRequestDTO); // roleRepository.save(roleEntity);
		assertThat(roleRequestDTO.getRoleName()).isEqualTo(roleEntity.getRoleName());
		// assertThat(roleRequestDTO).isEqualTo(roleEntity);

	}

	@Test
	void testDeleteRole() throws Exception {
		mock(RoleEntity.class);
		mock(RoleRequestDTO.class);
		// roleEntity.setIsDeleted(true);
		when(roleRepository.findById(roleEntity.getId())).thenReturn(Optional.ofNullable(roleEntity));
		roleService.deleteRole(roleRequestDTO.getId());
		assertThat(roleEntity.getIsDeleted()).isEqualTo(true);

	}

	@Test
	void testGetRoleById() throws Exception {
		mock(RoleEntity.class);
		mock(MessageSource.class);
		mock(RoleRepository.class);
		mock(RoleRequestDTO.class); //
		roleRepository.save(roleEntity);
		// roleEntity.seti
		assertThat(roleRequestDTO.getId()).isEqualTo(1L);
		// assertThat(role.isEmpty());
		// roleService.getRoleById(roleRequestDTO.getId());
		// when(roleService.getRoleById(roleRequestDTO.getId())).thenReturn(Optional.ofNullable(roleEntity).get());
		// roleService.getRoleById(roleRequestDTO.getId());
		// roleRepository.findById(roleRequestDTO.getId()); //role =
		// roleRepository.findById(modules.get(1).getId());
		// assertThat(1L).isEqualTo(role.get().getId());//role.get().getId()
		// when(roleService.getRoleById(roleRequestDTO.getId())).thenReturn(roleEntity
		when(roleRepository.findById(roleEntity.getId())).thenReturn(Optional.ofNullable(roleEntity));
		assertThat(roleService.getRoleById(1L)).isEqualTo(roleEntity);
	}

	@Test
	void testGetRoleList() {
		mock(RoleEntity.class);
		mock(Pageable.class);
		mock(RoleRepository.class);
		String RoleName = roleEntity.getRoleName();
		Pageable paging = PageRequest.of(1, 1, Sort.by(RoleName));
		when(roleRepository.findAllAndIsDeleted(false, paging)).thenReturn(roleEntityList);
		assertThat(roleService.getRoleList(1, 1, RoleName)).isEqualTo(roleEntityList);

	}

	@Test
	void testSearch() {
		mock(RoleEntity.class);
		mock(Pageable.class);
		mock(RoleRepository.class);
		mock(RoleSpecificationBuilder.class);
		String RoleName = roleEntity.getRoleName();
		Pageable paging = PageRequest.of(1, 1, Sort.by(RoleName));
		when(roleRepository.findAll(builder.build(), pageable)).thenReturn(rolesPage);
		assertThat(roleService.search("name", pageable)).isEqualTo(rolesPage);

	}

	@Test
	void testGetUserGroupListingPage() {
		mock(RoleEntity.class);
		mock(Pageable.class);
		mock(RoleRepository.class);
		// Sort.Direction direction = Sort.DEFAULT_DIRECTION;
		// direction = sortDirection.equals("desc") ? Sort.Direction.DESC :
		// Sort.Direction.ASC;
		// sort = Sort.by(direction, sortBy);
		sort = Sort.by(Sort.Direction.DESC, "updatedAt");
		pageable = PageRequest.of(0, 1, sort);
		when(roleRepository.findAllByPaginationAndSort(false, true, pageable)).thenReturn(rolesPage);
		assertThat(roleService.getUserGroupListingPage(1, 1, "updatedAt", "DEFAULT_DIRECTION")).isEqualTo(rolesPage);

	}

	@Test
	void testGetUserGroupListingPageWithSearch() {
		mock(RoleEntity.class);
		mock(Pageable.class);
		mock(RoleRepository.class);
		when(roleService.getUserGroupListingPageWithSearch(1, 1, "updatedAt", "DEFAULT_DIRECTION", "name")).thenReturn(rolesPage);
		//assertThat(rolesPage.hasContent()).isEqualTo(rolesPage.hasContent());
		assertNull(rolesPage);
		//when(roleRepository.findAll(roleService.getSpecification("name",customView,false,true), pageable));

	}
	  
		/*
		 * @Test void testGetSpecification() throws Exception{ mock(RoleEntity.class);
		 * mock(RoleService.class); RoleService roleService = new
		 * RoleService(roleRepository, messageSource, moduleRepository); Method
		 * getSpecification =
		 * RoleService.class.getDeclaredMethod("getSpecification",String.class,List<
		 * String>,Boolean,Boolean); getSpecification.setAccessible(true); RoleEntity
		 * roleEntityResult = (RoleEntity) getSpecification.invoke(roleService,
		 * roleRequestDTO); //when(roleService.ge)
		 * 
		 * 
		 * }
		 */
	 

}
