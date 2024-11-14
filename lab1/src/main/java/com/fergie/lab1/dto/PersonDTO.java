package com.fergie.lab1.dto;

import com.fergie.lab1.models.Location;
import com.fergie.lab1.models.enums.Color;
import com.fergie.lab1.models.enums.Country;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PersonDTO {

    @NotNull(message="field cannot be null")
    @NotEmpty(message="field cannot be empty")
    private String name; //Поле не может быть null, Строка не может быть пустой

    @NotNull(message="field cannot be null")
    @Enumerated(EnumType.STRING)
    private Color eyeColor; //Поле не может быть null

    @Enumerated(EnumType.STRING)
    private Color hairColor; //Поле может быть null

    @NotNull(message="field cannot be null")
    private Location location; //Поле не может быть null

    @NotNull(message="field cannot be null")
    @Size(min = 10, max = 43)
    private String passportID; //Длина строки должна быть не меньше 10, Длина строки не должна быть больше 43, Поле не может быть null

    @NotNull(message="field cannot be null")
    @Enumerated(EnumType.STRING)
    private Country nationality; //Поле не может быть null

    private Long authorID;
}