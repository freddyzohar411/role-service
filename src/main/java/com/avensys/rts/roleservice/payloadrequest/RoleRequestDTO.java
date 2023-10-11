package com.avensys.rts.roleservice.payloadrequest;

import com.avensys.rts.roleservice.payload.PermissionDTO;
import com.avensys.rts.roleservice.payload.UserDTO;
import com.avensys.rts.roleservice.payload.UserGroupDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequestDTO {
    private String roleName;
    private String roleDescription;
    List<UserDTO> userDTOList;
    List<UserGroupDTO> userGroupDTOList;
    List<PermissionDTO>permissionDTOList;
}
