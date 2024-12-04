package com.fergie.lab1.repositories;
import com.fergie.lab1.models.Movie;
import com.fergie.lab1.models.enums.MovieGenre;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoviesRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    Optional<Movie> findByName(String name);
    @NotNull
    Page<Movie> findAll(@NotNull Pageable pageable);

    @Query("SELECT COUNT(m) FROM Movie m WHERE m.genre = :genre")
    int countMoviesByGenre(@Param("genre") MovieGenre genre);

    @Query(value = "SELECT * FROM get_unique_golden_palm_counts()", nativeQuery = true)
    List<Integer> findUniqueGoldenPalmCounts();

//    @Query(value = "SELECT get_movie_with_min_director()", nativeQuery = true)
//    Optional<Movie> findMovieWithMinDirector();
    @Query(value = "SELECT get_movie_with_min_director()", nativeQuery = true)
    String findMovieWithMinDirector();



}

