package com.avensys.rts.roleservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_group_roles")
public class UserGroupRolesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="user_group_role_id")
    private Integer userGroupRoleId;

    @Column(name = "user_group_Id")
    private Integer userGroupId;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name="created_by")
    private Integer createdBy;

    @Column(name="updated_by")
    private Integer updatedBy;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
