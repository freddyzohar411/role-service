package com.avensys.rts.roleservice.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.avensys.rts.roleservice.entity.ModuleEntity;
import com.avensys.rts.roleservice.entity.PermissionEntity;
import com.avensys.rts.roleservice.entity.RoleEntity;
import com.avensys.rts.roleservice.payload.response.ModuleResponseDTO;
import com.avensys.rts.roleservice.payload.response.RoleListingResponseDTO;
import com.avensys.rts.roleservice.payload.response.RoleResponseDTO;
import com.avensys.rts.roleservice.repository.PermissionRepository;
import com.avensys.rts.roleservice.response.HttpResponse;

@Component
public class ResponseUtil {

	private static PermissionRepository permissionRepository;

	@Autowired
	public void setSomeThing(PermissionRepository permissionRepository) {
		ResponseUtil.permissionRepository = permissionRepository;
	}

	public static ResponseEntity<Object> generateSuccessResponse(Object dataObject, HttpStatus httpStatus,
			String message) {
		HttpResponse httpResponse = new HttpResponse();
		httpResponse.setData(dataObject);
		httpResponse.setCode(httpStatus.value());
		httpResponse.setMessage(message);
		return new ResponseEntity<>(httpResponse, httpStatus);
	}

	public static ResponseEntity<Object> generateErrorResponse(HttpStatus httpStatus, String message) {
		HttpResponse httpResponse = new HttpResponse();
		httpResponse.setCode(httpStatus.value());
		httpResponse.setError(true);
		httpResponse.setMessage(message);
		return new ResponseEntity<>(httpResponse, httpStatus);
	}

	public static RoleResponseDTO mapRoleEntitytoResponse(RoleEntity role) {
		RoleResponseDTO dto = new RoleResponseDTO();
		dto.setId(role.getId());
		dto.setRoleName(role.getRoleName());
		dto.setRoleDescription(role.getRoleDescription());
		dto.setUserGroups(role.getGroupEntities());

		List<ModuleResponseDTO> moduleList = new ArrayList<ModuleResponseDTO>();

		Map<Long, String> permissionsMap = new HashMap<Long, String>();
		Iterable<PermissionEntity> permissionEntities = permissionRepository.findAll();

		if (permissionEntities.spliterator().getExactSizeIfKnown() > 0) {
			permissionEntities.forEach(per -> {
				permissionsMap.put(per.getId(), per.getPermissionName());
			});
		}

		if (role.getModulePermissions() != null && role.getModulePermissions().size() > 0) {
			role.getModulePermissions().forEach(modulePermission -> {
				ModuleEntity module = modulePermission.getModule();
				ModuleResponseDTO moduleResponseDTO = new ModuleResponseDTO();
				moduleResponseDTO.setId(module.getId());
				moduleResponseDTO.setModuleName(module.getModuleName());

				List<String> permissions = new ArrayList<String>();
				if (modulePermission.getPermissions() != null && modulePermission.getPermissions().length() > 0) {
					String[] permissionIds = modulePermission.getPermissions().split(",");
					for (int i = 0; i < permissionIds.length; i++) {
						Long id = Long.parseLong(permissionIds[i]);
						if (permissionsMap.get(id) != null) {
							permissions.add(permissionsMap.get(id));
						}
					}
				}
				moduleResponseDTO.setPermissions(permissions);
				moduleList.add(moduleResponseDTO);
			});
		}

		dto.setModules(moduleList);
		return dto;
	}

	public static List<RoleResponseDTO> mapRoleEntityListtoResponse(List<RoleEntity> roles) {
		List<RoleResponseDTO> response = new ArrayList<RoleResponseDTO>();
		if (roles != null && roles.size() > 0) {
			roles.forEach(role -> {
				response.add(mapRoleEntitytoResponse(role));
			});
		}
		return response;
	}

	public static RoleListingResponseDTO mapRoleEntityPageToRoleListingResponse(Page<RoleEntity> roleEntityPage) {
		RoleListingResponseDTO roleListingResponseDTO = new RoleListingResponseDTO();
		roleListingResponseDTO.setTotalElements(roleEntityPage.getTotalElements());
		roleListingResponseDTO.setTotalPages(roleEntityPage.getTotalPages());
		roleListingResponseDTO.setRoles(mapRoleEntityListtoResponse(roleEntityPage.getContent()));
		return roleListingResponseDTO;
	}

}
