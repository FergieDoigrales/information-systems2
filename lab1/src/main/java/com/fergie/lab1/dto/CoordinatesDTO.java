package com.fergie.lab1.dto;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class CoordinatesDTO {

    private Integer x; //Поле не может быть null

    private Long y; //Поле не может быть null
}
