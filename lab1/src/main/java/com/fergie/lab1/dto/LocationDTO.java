package com.fergie.lab1.dto;

import com.fergie.lab1.models.LocationDetails;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationDTO {

    private Double x; //Поле не может быть null

    private float y;

    private long z;

    private LocationDetails locationDetails;
}
