package com.fergie.lab1.models;

import com.fergie.lab1.models.enums.AccessRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "users")
public class User {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message="field cannot be null")
    @NotEmpty(message="field cannot be empty")
    @Column(unique = true)
    private String username;

    @Column
    @NotNull(message="field cannot be null")
    @NotEmpty(message="field cannot be empty")
    private String password;

    @Column
    @NotNull(message="field cannot be null")
    @Enumerated(EnumType.STRING)
    private AccessRole role = AccessRole.USER; //куда тебя переставить????

}
