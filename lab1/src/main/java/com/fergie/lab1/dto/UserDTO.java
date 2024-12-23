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

    private String username;

    private String password;

    private AccessRole role; //куда тебя переставить????
}
