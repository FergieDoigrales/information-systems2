package com.fergie.lab1.dto;

import com.fergie.lab1.models.Coordinates;
import com.fergie.lab1.models.Person;
import com.fergie.lab1.models.enums.MovieGenre;
import com.fergie.lab1.models.enums.MpaaRating;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter

public class MovieDTO {
    @NotNull(message="field cannot be null")
    @NotEmpty(message="field cannot be empty")
    private String name; //Поле не может быть null, Строка не может быть пустой

    @NotNull(message="field cannot be null")
    private Coordinates coordinates; //Поле не может быть null

    @Positive(message="The field value must be greater than 0")
    private int oscarsCount; //Значение поля должно быть больше 0

    @Positive(message="The field value must be greater than 0")
    @NotNull(message="field cannot be null")
    private Double budget; //Значение поля должно быть больше 0, Поле не может быть null

    @NotNull(message="field cannot be null")
    @Positive(message="The field value must be greater than 0")
    private Float totalBoxOffice; //Поле не может быть null, Значение поля должно быть больше 0

    @Enumerated(EnumType.STRING)
    private MpaaRating mpaaRating; //Поле может быть null

    private Person director; //Поле может быть null
    private Person screenwriter;
    @NotNull(message="field cannot be null")
    private Person operator; //Поле не может быть null

    @Positive(message="The field value must be greater than 0")
    private Long length; //Поле может быть null, Значение поля должно быть больше 0

    @Positive(message="The field value must be greater than 0")
    private Integer goldenPalmCount; //Значение поля должно быть больше 0, Поле может быть null

    @Enumerated(EnumType.STRING)
    private MovieGenre genre; //Поле может быть null

}
