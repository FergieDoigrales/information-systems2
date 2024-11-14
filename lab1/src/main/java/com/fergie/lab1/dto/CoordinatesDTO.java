package com.fergie.lab1.dto;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class CoordinatesDTO {
    @NotNull(message="field cannot be null")
    private Integer x; //Поле не может быть null

    @NotNull(message="field cannot be null")
    private Long y; //Поле не может быть null
}
