package com.avensys.rts.roleservice.payload.response;

import java.util.List;
import java.util.Set;

import com.avensys.rts.roleservice.entity.UserGroupEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDTO {

	private Long id;
	private String roleName;
	private String roleDescription;
	private List<ModuleResponseDTO> modules;
	private Set<UserGroupEntity> userGroups;
}
