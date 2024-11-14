package com.fergie.lab1.dto;

import com.fergie.lab1.models.enums.AccessRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotNull(message="field cannot be null")
    @NotEmpty(message="field cannot be empty")
    private String username;


    @NotNull(message="field cannot be null")
    @NotEmpty(message="field cannot be empty")
    private String password;


    @NotNull(message="field cannot be null")
    @Enumerated(EnumType.STRING)
    private AccessRole role; //куда тебя переставить????
}
