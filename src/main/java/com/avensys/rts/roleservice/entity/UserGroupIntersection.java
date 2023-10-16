package com.avensys.rts.roleservice.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(name= "User_group_intersection")
public class UserGroupIntersection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="user_group_id")
    private Integer userGroupId;

    @Column(name="user_id")
    private Integer userId;

    @Column(name="created_by")
    private Integer createdBy;

    @Column(name="updated_by")
    private Integer updatedBy;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

}
