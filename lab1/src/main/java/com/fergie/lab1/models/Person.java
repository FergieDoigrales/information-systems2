package com.fergie.lab1.models;

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
@Table(name = "person")
@Entity

public class Person {
    @Column(name = "person_id") //??
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull(message="field cannot be null")
    @NotEmpty(message="field cannot be empty")
    private String name; //Поле не может быть null, Строка не может быть пустой

    @Column(name = "eye_color")
    @NotNull(message="field cannot be null")
    @Enumerated(EnumType.STRING)
    private Color eyeColor; //Поле не может быть null

    @Column(name = "hair_color")
    @Enumerated(EnumType.STRING)
    private Color hairColor; //Поле может быть null

    @NotNull(message="field cannot be null")
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location; //Поле не может быть null

    @Column(name = "passport_id")
    @NotNull(message="field cannot be null")
    @Size(min = 10, max = 43)
    private String passportID; //Длина строки должна быть не меньше 10, Длина строки не должна быть больше 43, Поле не может быть null

    @Column
    @NotNull(message="field cannot be null")
    @Enumerated(EnumType.STRING)
    private Country nationality; //Поле не может быть null

    @Column(name = "author_id")
    private Long authorID;
}
