package com.fergie.lab1.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationDTO {

    @NotNull(message="field cannot be null")
    private Double x; //Поле не может быть null

    private float y;

    private long z;
}
