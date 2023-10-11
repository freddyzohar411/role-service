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
@Entity(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="username",length = 250)
    private String username;

    @Column(name = "password",length = 20)
    private String password;

    @Column(name = "last_login_datetime")
    private LocalDateTime lastLoginDatetime;

    @Column(name ="contact_id")
    private Integer contactId;

    @Column(name="entity_id")
    private Integer entityId;
}
