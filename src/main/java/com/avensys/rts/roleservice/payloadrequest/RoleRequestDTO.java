package com.avensys.rts.roleservice.payloadrequest;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequestDTO {

	private Long id;
	private String roleName;
	private String roleDescription;
	private List<Long> permissionDTOList;
}
