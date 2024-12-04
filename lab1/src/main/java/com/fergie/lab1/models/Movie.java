package com.fergie.lab1.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fergie.lab1.models.enums.MovieGenre;
import com.fergie.lab1.models.enums.MpaaRating;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Getter
@Setter
@Table(name = "movie")
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id") //???
    private Long movieId; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @Column
    @NotNull(message="field cannot be null")
    @NotEmpty(message="field cannot be empty")
    private String name; //Поле не может быть null, Строка не может быть пустой

    @NotNull(message="field cannot be null")
    @ManyToOne
    @JoinColumn(name = "coordinates_id")
    private Coordinates coordinates; //Поле не может быть null

    @Column(name = "creation_date")
    @CreatedDate
    @NotNull(message="field cannot be null")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Person director; //Поле может быть null

    @ManyToOne
    @JoinColumn(name = "screenwriter_id")
    private Person screenwriter;

    @NotNull(message="field cannot be null")
    @ManyToOne
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
    @CreatedBy
    private Long authorID;

    @Column(name = "modified_by")
    @LastModifiedBy
    private String modifiedBy;

    @Column(name = "modified_at")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date modifiedAt;

    public Movie() {
        this.creationDate = new Date();
    }

}

//проверить типы связей
