package com.avensys.rts.roleservice.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    //private String password;
    private LocalDateTime lastLoginDatetime;
    private Integer contactId;
    private Integer entityId;
}
