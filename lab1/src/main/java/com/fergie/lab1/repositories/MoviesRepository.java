package com.fergie.lab1.repositories;
import com.fergie.lab1.models.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoviesRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByName(String name);
    Page<Movie> findAll(Pageable pageable);

}

