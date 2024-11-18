package com.fergie.lab1.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "coordinates")
public class Coordinates {
    @Column(name = "coordinates_id") //???
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull(message="field cannot be null")
    private Integer x; //Поле не может быть null

    @Column
    @NotNull(message="field cannot be null")
    private Long y; //Поле не может быть null

    @Column(name = "author_id")
    private Long authorID;
}
