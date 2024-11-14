package com.fergie.lab1.models;


import com.fergie.lab1.models.enums.MovieGenre;
import com.fergie.lab1.models.enums.MpaaRating;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Table(name = "movie")
@Entity
public class Movie {
    @Column(name = "movie_id") //???
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @Column
    @NotNull(message="field cannot be null")
    @NotEmpty(message="field cannot be empty")
    private String name; //Поле не может быть null, Строка не может быть пустой

    @NotNull(message="field cannot be null")
    @ManyToOne(cascade = CascadeType.ALL) // несколько фильмов могут быть связаны с одной и той же координатой
    @JoinColumn(name = "coordinates_id")
    private Coordinates coordinates; //Поле не может быть null

    @Column
    @NotNull(message="field cannot be null")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @Column(name = "oscars_count")
    @Positive(message="The field value must be greater than 0")
    private int oscarsCount; //Значение поля должно быть больше 0

    @Column
    @Positive(message="The field value must be greater than 0")
    @NotNull(message="field cannot be null")
    private Double budget; //Значение поля должно быть больше 0, Поле не может быть null

    @Column(name = "total_box_office")
    @NotNull(message="field cannot be null")
    @Positive(message="The field value must be greater than 0")
    private Float totalBoxOffice; //Поле не может быть null, Значение поля должно быть больше 0

    @Column(name = "mpaa_rating")
    @Enumerated(EnumType.STRING)
    private MpaaRating mpaaRating; //Поле может быть null

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "director_id")
    private Person director; //Поле может быть null

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "screenwriter_id")
    private Person screenwriter;

    @NotNull(message="field cannot be null")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "operator_id")
    private Person operator; //Поле не может быть null

    @Column
    @Positive(message="The field value must be greater than 0")
    private Long length; //Поле может быть null, Значение поля должно быть больше 0

    @Column(name = "golden_palm_count")
    @Positive(message="The field value must be greater than 0")
    private Integer goldenPalmCount; //Значение поля должно быть больше 0, Поле может быть null

    @Column
    @Enumerated(EnumType.STRING)
    private MovieGenre genre; //Поле может быть null

    @Column(name = "author_id")
    private Long authorID;

    public Movie() {
        this.creationDate = new Date();
    }

}

//проверить типы связей
