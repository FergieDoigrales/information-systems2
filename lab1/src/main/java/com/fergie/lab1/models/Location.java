package com.fergie.lab1.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "location")
@Entity
public class Location {
    @Column(name = "location_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull(message="field cannot be null")
    private Double x; //Поле не может быть null

    @Column
    private float y;

    @Column
    private long z;

    @Column(name = "author_id")
    private Long authorID;

    @OneToOne
    @JoinColumn(name = "details_id")
    private LocationDetails locationDetails;
}
