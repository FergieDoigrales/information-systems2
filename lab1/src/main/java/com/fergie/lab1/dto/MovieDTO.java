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

    private Long MovieId;

    private String name; //Поле не может быть null, Строка не может быть пустой

    private Coordinates coordinates; //Поле не может быть null

    private int oscarsCount; //Значение поля должно быть больше 0

    private Double budget; //Значение поля должно быть больше 0, Поле не может быть null

    private Float totalBoxOffice; //Поле не может быть null, Значение поля должно быть больше 0

    private MpaaRating mpaaRating; //Поле может быть null

    private Person director; //Поле может быть null
    private Person screenwriter;

    private Person operator; //Поле не может быть null

    private Long length; //Поле может быть null, Значение поля должно быть больше 0

    private Integer goldenPalmCount; //Значение поля должно быть больше 0, Поле может быть null

    private MovieGenre genre; //Поле может быть null

    private Long authorID;

}
