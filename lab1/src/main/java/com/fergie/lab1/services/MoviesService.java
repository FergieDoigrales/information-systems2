package com.fergie.lab1.services;

import com.fergie.lab1.models.Movie;
import com.fergie.lab1.models.enums.*;
import com.fergie.lab1.repositories.MoviesRepository;
import com.fergie.lab1.specification.MovieSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class MoviesService {
    private final MoviesRepository moviesRepository;
    private final MovieAuditService movieAuditService;

    @Autowired
    public MoviesService(MoviesRepository moviesRepository, MovieAuditService movieAuditService) {
        this.moviesRepository = moviesRepository;
        this.movieAuditService = movieAuditService;
    }

    @Cacheable(value = "moviesCache", key = "#Id")
    public Movie findById(Long Id){
        Optional<Movie> movie = moviesRepository.findById(Id);
        return movie.orElse(null);
    }

    public Page<Movie> searchMovies(String query, Pageable pageable) {
        MovieSpecification specification = new MovieSpecification(query);
        return moviesRepository.findAll(specification, pageable);
    }


    @Cacheable(value = "moviesCache", key = "#name")
    public Movie findByName(String name){
        Optional<Movie> movie = moviesRepository.findByName(name);
        return movie.orElse(null);
    }

    @Cacheable(value = "moviesCache", key = "#pageable")
    public Page<Movie> findAll(Pageable pageable) {
        return moviesRepository.findAll(pageable);
    }

    @CacheEvict(value = "moviesCache", allEntries = true)
    @Transactional
    public void addMovie(Movie movie, Long authorID) {
        Movie addedMovie = moviesRepository.save(enrichMovie(movie, authorID));
        movieAuditService.recordMovieChange(addedMovie, "CREATE", "name", movie.getName(), movie.getName(), String.valueOf(authorID));

    }

    @CacheEvict(value = "moviesCache", allEntries = true)
    @Transactional
    public Movie updateMovie(AccessRole userRole, Long authorID, Long id, Movie updatedMovie) {
        Optional<Movie> existingMovie = moviesRepository.findById(id);

        if (existingMovie.isPresent()) {
            Movie movie = existingMovie.get();
            System.out.println("Author ID: " + authorID);
            System.out.println("Movie author ID: " + movie.getAuthorID());
            if (!authorID.equals(movie.getAuthorID()) && !userRole.equals(AccessRole.ADMIN)) {
                throw new IllegalArgumentException("Author ID does not match and user is not an admin");
            } //или возвращать null, или кидать исключение
            movieAuditService.recordMovieChange(movie, "UPDATE", "name", movie.getName(), updatedMovie.getName(), String.valueOf(authorID));
            movie.setName(updatedMovie.getName());
            movie.setName(updatedMovie.getName());
            movie.setCoordinates(updatedMovie.getCoordinates());
            movie.setOscarsCount(updatedMovie.getOscarsCount());
            movie.setBudget(updatedMovie.getBudget());
            movie.setTotalBoxOffice(updatedMovie.getTotalBoxOffice());
            movie.setMpaaRating(updatedMovie.getMpaaRating());
            movie.setDirector(updatedMovie.getDirector());
            movie.setScreenwriter(updatedMovie.getScreenwriter());
            movie.setOperator(updatedMovie.getOperator());
            movie.setLength(updatedMovie.getLength());
            movie.setGoldenPalmCount(updatedMovie.getGoldenPalmCount());
            movie.setGenre(updatedMovie.getGenre());

            return moviesRepository.save(movie);
        } else {
            return null;
        }
    }

    @CacheEvict(value = "moviesCache", allEntries = true)
    @Transactional
    public void deleteMovie(AccessRole userRole, Long authorID, Long id) {
        Optional<Movie> existingMovie = moviesRepository.findById(id);
        if (existingMovie.isPresent()) {
            Movie movie = existingMovie.get();
            if (!authorID.equals(movie.getAuthorID()) && !userRole.equals(AccessRole.ADMIN)) {
                throw new IllegalArgumentException("Author ID does not match and user is not an admin");
            }
            movieAuditService.recordMovieChange(movie, "DELETE", "name", movie.getName(), "DELETED", String.valueOf(authorID));
        }
        moviesRepository.deleteById(id);
    }

    private Movie enrichMovie(Movie movie, Long authorId) { //как получить Id автора
        movie.setCreationDate(new java.util.Date());
        movie.setAuthorID(authorId);
        return movie;
    }

    private Sort.Order getSortObject(String sortField, String sortOrder) {
        return "desc".equalsIgnoreCase(sortOrder)
                ? Sort.Order.desc(sortField)
                : Sort.Order.asc(sortField);
    }

}