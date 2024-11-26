package com.fergie.lab1.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "movie_audit")
@Setter
@Getter
public class MovieAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long movieId;

    private String action;
    private String field;
    private String oldValue;
    private String newValue;
    private String changedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date changedAt;

    public MovieAudit() {}

}