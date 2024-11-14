package com.fergie.lab1.repositories;

import com.fergie.lab1.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoviesRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByName(String name);
}

