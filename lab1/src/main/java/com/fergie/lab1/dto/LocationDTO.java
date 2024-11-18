package com.fergie.lab1.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationDTO {

    private Double x; //Поле не может быть null

    private float y;

    private long z;
}
