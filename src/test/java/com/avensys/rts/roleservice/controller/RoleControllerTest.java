package com.avensys.rts.roleservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.avensys.rts.roleservice.entity.BaseEntity;
import com.avensys.rts.roleservice.entity.ModuleEntity;
import com.avensys.rts.roleservice.entity.RoleEntity;
import com.avensys.rts.roleservice.entity.RoleModulePermissionsEntity;
import com.avensys.rts.roleservice.entity.UserGroupEntity;
import com.avensys.rts.roleservice.payload.request.ModuleRequestDTO;
import com.avensys.rts.roleservice.payload.request.RoleRequestDTO;
import com.avensys.rts.roleservice.payload.response.ModuleResponseDTO;
import com.avensys.rts.roleservice.payload.response.RoleResponseDTO;
import com.avensys.rts.roleservice.repository.PermissionRepository;
import com.avensys.rts.roleservice.repository.RoleRepository;
import com.avensys.rts.roleservice.service.RoleService;
import com.avensys.rts.roleservice.util.JwtUtil;
import com.avensys.rts.roleservice.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.qos.logback.core.status.Status;

@RunWith(MockitoJUnitRunner.class)
public class RoleControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private RoleController roleController;

	@Mock
	private RoleService roleService;

	@Autowired
	private ObjectMapper objectMapper;

	@Mock
	private MessageSource messageSource;

	@Mock
	private JwtUtil jwtUtil;
	@MockBean
	AutoCloseable autoCloseable;
	BaseEntity baseEntity;
	RoleEntity roleEntity;
	RoleEntity roleEntity1;
	ModuleEntity moduleEntity;
	Set<UserGroupEntity> groupEntities;
	@MockBean
	RoleRequestDTO roleRequestDTO;
	@MockBean
	RoleResponseDTO roleResponseDTO;
	RoleResponseDTO roleResponseDTO1;
	@Mock
	RoleRepository roleRepository;
	HttpStatus httpStatus;
	List<ModuleRequestDTO> modules;
	List<ModuleResponseDTO> modules1;
	List<Long> permissionsList;
	List<String>permissionsList1;
	Set<RoleModulePermissionsEntity> modulePermissions;
	RoleModulePermissionsEntity roleModulePermissionsEntity;
	RoleModulePermissionsEntity roleModulePermissionsEntity1;
	ModuleRequestDTO moduleRequestDTO;
	ModuleRequestDTO moduleRequestDTO1;
	ModuleResponseDTO moduleResponseDTO;
	ModuleResponseDTO moduleResponseDTO1;
	@Mock
	ResponseUtil responseUtil;
	@Mock
	PermissionRepository permissionRepository;
	List<RoleResponseDTO> response;
	List<RoleEntity> roleEntityList;

	@BeforeEach
	void setUp() {
		autoCloseable = MockitoAnnotations.openMocks(this);
		roleModulePermissionsEntity = new RoleModulePermissionsEntity(1L, moduleEntity, roleEntity, "permissions");
		roleModulePermissionsEntity1 = new RoleModulePermissionsEntity(2L, moduleEntity, roleEntity, "permissions");
		modulePermissions = new HashSet<RoleModulePermissionsEntity>();
		modulePermissions.add(roleModulePermissionsEntity);
		modulePermissions.add(roleModulePermissionsEntity1);
		roleEntity = new RoleEntity(1L, "Super Admin", "Super admin access to all modules!", modulePermissions,
				groupEntities);
		roleEntity1 = new RoleEntity(2L, "Super Admin", "Super admin access to all modules!", modulePermissions,
				groupEntities);
		roleEntityList = Arrays.asList(roleEntity,roleEntity1);
		permissionsList = Arrays.asList(1L, 2L, 3L, 4L);
		permissionsList1 = Arrays.asList("Read", "Write","Edit","Delete");
		moduleRequestDTO = new ModuleRequestDTO(1L, permissionsList);
		moduleRequestDTO1 = new ModuleRequestDTO(2L, permissionsList);
		moduleResponseDTO = new ModuleResponseDTO(1L,"Hi",permissionsList1);
		moduleResponseDTO1 = new ModuleResponseDTO(1L,"Hi1",permissionsList1);
		modules1 = Arrays.asList(moduleResponseDTO, moduleResponseDTO1);
		modules = Arrays.asList(moduleRequestDTO, moduleRequestDTO1);
		roleRequestDTO = new RoleRequestDTO(1L, "Super Admin", "Super admin access to all modules!", modules, 1L, 1L);
		roleResponseDTO = new RoleResponseDTO(1L, "Super Admin", "Super admin access to all modules!", modules1);
		roleResponseDTO1 = new RoleResponseDTO(2L, "Super Admin", "Super admin access to all modules!", modules1);
		response = Arrays.asList(roleResponseDTO,roleResponseDTO1);
		this.mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
		
	}

	@AfterEach
	void tearDown() throws Exception {
		roleEntity = null;
		moduleRequestDTO = null;
		moduleRequestDTO1 = null;
		modules = null;
		roleModulePermissionsEntity = null;
		roleModulePermissionsEntity1 = null;
		permissionsList = null;
		roleRequestDTO = null;
		autoCloseable.close();

	}

	@Test
	void testCreateRole() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		RequestBuilder request = MockMvcRequestBuilders.post("/api/role")
				.content(asJsonString(
						new RoleRequestDTO(1L, "Super Admin", "Super admin access to all modules!", modules, 1L, 1L)))
				.header("Authorization",
						"Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI0WndUaGhXVUtGSjhUdE1NdFZrcm1Edk9TdGdRcS1Sa3MwUnEwRE5IRG5jIn0.eyJleHAiOjE3MDMyMzI3MTQsImlhdCI6MTcwMzIzMjQxNCwianRpIjoiNmMwYjBlMmYtMDZmYi00YzU3LWJmMWQtM2MzNmEzZGUxOGQxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3JlYWxtcy9ydHNyZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiIzMzlmMzVhNy0wZDNkLTQzMWUtOWE2My1kOTBkNGMzNDJlNGEiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJydHNjbGllbnQiLCJzZXNzaW9uX3N0YXRlIjoiMzExMDI3MDYtYmJmZS00MGJjLWE4YmMtMDEzYTgzYzIzMTVlIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3d3dy5rZXljbG9hay5vcmciXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJkZWZhdWx0LXJvbGVzLXJ0c3JlYWxtIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiMzExMDI3MDYtYmJmZS00MGJjLWE4YmMtMDEzYTgzYzIzMTVlIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJLb3RhaSBOYWxsZWIiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJraXR0dTFAYXZlbi1zeXMuY29tIiwiZ2l2ZW5fbmFtZSI6IktvdGFpIiwiZmFtaWx5X25hbWUiOiJOYWxsZWIiLCJlbWFpbCI6ImtpdHR1MUBhdmVuLXN5cy5jb20ifQ.A314CP_nu6x3qENsK8fyZP8SXXJO9y1nAcUXHU2FRRZ2vtPjD-T6rUoHQ_CZgMXnPg4Rl4MOlSCQ5leTiWix9kfBYkDQGar7GPSf9UnnPai7adiLV8Rb6OUYykHPjN_Wy3A0CVyGbsBB1ow7uhmgPkM7aMBUUYikkYK0aLremKn9vXJCpC7G2UTCW_BOjl7Bb5atic3J328ieN8nu0_W_Zd61ux1zm7skX4TPLNTC-4dAc16O-6IOo6JChQLUublfm-CcVC_i7oIv0Nuw7hOj5m5_e0klNcK-dw9bArBkRCGU9Sr4ieFIkjaLxt22Z3ZDg0C9SeB268OvnKXrjDKiQ")
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(request);
	}

	@Test
	void testUpdateRole() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = writer.writeValueAsString(roleRequestDTO);
		RequestBuilder request = MockMvcRequestBuilders.put("/api/role").content(requestJson).header("Authorization",
				"Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI0WndUaGhXVUtGSjhUdE1NdFZrcm1Edk9TdGdRcS1Sa3MwUnEwRE5IRG5jIn0.eyJleHAiOjE3MDMyMzI3MTQsImlhdCI6MTcwMzIzMjQxNCwianRpIjoiNmMwYjBlMmYtMDZmYi00YzU3LWJmMWQtM2MzNmEzZGUxOGQxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3JlYWxtcy9ydHNyZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiIzMzlmMzVhNy0wZDNkLTQzMWUtOWE2My1kOTBkNGMzNDJlNGEiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJydHNjbGllbnQiLCJzZXNzaW9uX3N0YXRlIjoiMzExMDI3MDYtYmJmZS00MGJjLWE4YmMtMDEzYTgzYzIzMTVlIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3d3dy5rZXljbG9hay5vcmciXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJkZWZhdWx0LXJvbGVzLXJ0c3JlYWxtIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiMzExMDI3MDYtYmJmZS00MGJjLWE4YmMtMDEzYTgzYzIzMTVlIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJLb3RhaSBOYWxsZWIiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJraXR0dTFAYXZlbi1zeXMuY29tIiwiZ2l2ZW5fbmFtZSI6IktvdGFpIiwiZmFtaWx5X25hbWUiOiJOYWxsZWIiLCJlbWFpbCI6ImtpdHR1MUBhdmVuLXN5cy5jb20ifQ.A314CP_nu6x3qENsK8fyZP8SXXJO9y1nAcUXHU2FRRZ2vtPjD-T6rUoHQ_CZgMXnPg4Rl4MOlSCQ5leTiWix9kfBYkDQGar7GPSf9UnnPai7adiLV8Rb6OUYykHPjN_Wy3A0CVyGbsBB1ow7uhmgPkM7aMBUUYikkYK0aLremKn9vXJCpC7G2UTCW_BOjl7Bb5atic3J328ieN8nu0_W_Zd61ux1zm7skX4TPLNTC-4dAc16O-6IOo6JChQLUublfm-CcVC_i7oIv0Nuw7hOj5m5_e0klNcK-dw9bArBkRCGU9Sr4ieFIkjaLxt22Z3ZDg0C9SeB268OvnKXrjDKiQ")
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(request).andExpect(status().isOk()).andReturn();
	}


	
	/*
	 * @Test void testFind() throws Exception {
	 * //when(roleService.getRoleById(1L)).thenReturn(roleEntity);
	 * when(roleService.getRoleById(1L)).thenReturn(roleEntity); RoleResponseDTO
	 * roleResponseDTO1 = ResponseUtil.mapRoleEntitytoResponse(roleEntity);
	 * responseUtil.mapRoleEntitytoResponse(roleEntity); RequestBuilder request =
	 * MockMvcRequestBuilders.get("/api/role/{id}", 1L);
	 * mockMvc.perform(request).andReturn(); }
	 * 
	 * RoleResponseDTO testMapRoleEntitytoResponse() { RoleResponseDTO
	 * roleResponseDTO1 = responseUtil.mapRoleEntitytoResponse(roleEntity); return
	 * roleResponseDTO1;
	 * 
	 * }
	 */
	 

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	@Test
	void testGetRoleList()  throws Exception{
		when(roleService.getRoleList(0,10,"Super Admin")).thenReturn(roleEntityList);
		RequestBuilder request = MockMvcRequestBuilders.get("/api/role");
		mockMvc.perform(request).andReturn();
	}
	
	@Test
	void testDeleteRole() throws Exception {
		roleService.deleteRole(1L);
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/role/{id}", 1)).andExpect(status().isOk()).andReturn();

	}

}
