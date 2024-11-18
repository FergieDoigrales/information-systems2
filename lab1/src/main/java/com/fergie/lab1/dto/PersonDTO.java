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

    private String name; //Поле не может быть null, Строка не может быть пустой

    private Color eyeColor; //Поле не может быть null

    private Color hairColor; //Поле может быть null

    private Location location; //Поле не может быть null

    private String passportID; //Длина строки должна быть не меньше 10, Длина строки не должна быть больше 43, Поле не может быть null

    private Country nationality; //Поле не может быть null

}