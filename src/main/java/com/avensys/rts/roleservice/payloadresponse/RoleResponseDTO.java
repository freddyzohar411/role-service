package com.avensys.rts.roleservice.payloadresponse;

import com.avensys.rts.roleservice.payload.PermissionDTO;
import com.avensys.rts.roleservice.payload.UserGroupDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDTO {
    private String roleName;
    private String roleDescription;
    //List<UserDTO> userDTOList;
    List<UserGroupDTO> userGroupDTOList;
    List<PermissionDTO>permissionDTOList;
}
