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
@Entity
@Table(name = "role")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "role_description",length =50 )
    private String roleDescription;

    @Column(name = "role_name",length = 50)
    private String roleName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name="is_deleted")
    private boolean isDeleted;

   // @ManyToMany
   // @JoinTable(name = "role_permissions",joinColumns = @JoinColumn(name="role_id",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name="permissions_id",referencedColumnName = "id"))
   // private List<PermissionsEntity> permissionsEntityList;

   // @ManyToMany
   // @JoinTable(name="user_group_roles",joinColumns = @JoinColumn(name="role_id",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name ="user_group_id",referencedColumnName = "id"))
   // private List<UserGroupEntity> userGroupEntityList;
}
