package com.avensys.rts.roleservice.util;

import java.util.ArrayList;
import java.util.List;

import com.avensys.rts.roleservice.payload.response.RoleListingResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.avensys.rts.roleservice.entity.RoleEntity;
import com.avensys.rts.roleservice.payload.response.ModuleResponseDTO;
import com.avensys.rts.roleservice.payload.response.RoleResponseDTO;
import com.avensys.rts.roleservice.response.HttpResponse;

public class ResponseUtil {
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

		List<ModuleResponseDTO> moduleList = new ArrayList<ModuleResponseDTO>();

		if (role.getModules() != null && role.getModules().size() > 0) {
			role.getModules().forEach(module -> {
				ModuleResponseDTO moduleResponseDTO = new ModuleResponseDTO();
				moduleResponseDTO.setId(module.getId());
				moduleResponseDTO.setModuleName(module.getModuleName());
				List<String> permissions = new ArrayList<String>();
				if (module.getPermissions() != null && module.getPermissions().size() > 0) {
					module.getPermissions().forEach(permission -> {
						permissions.add(permission.getPermissionName());
					});
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
