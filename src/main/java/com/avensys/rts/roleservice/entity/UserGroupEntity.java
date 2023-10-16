package com.avensys.rts.roleservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_group")
public class UserGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="user_group_name",length = 50)
    private String userGroupName;

    @Column(name="description",length = 255)
    private String description;

    @Column(name="created_by")
    private Integer createdBy;

    @Column(name="updated_by")
    private Integer updatedBy;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "userGroupEntityList")
    private List<RoleEntity> roleEntityList;
}
