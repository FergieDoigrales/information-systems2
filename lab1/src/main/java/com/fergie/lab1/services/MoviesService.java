package com.fergie.lab1.services;

import com.fergie.lab1.models.Movie;
import com.fergie.lab1.models.enums.*;
import com.fergie.lab1.repositories.MoviesRepository;
import com.fergie.lab1.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class MoviesService {
    private final MoviesRepository moviesRepository;
    private final CoordinatesService coordinatesService;
    private final PeopleService peopleService;
    private final LocationService locationService;

    @Autowired
    public MoviesService(MoviesRepository moviesRepository, UsersRepository usersRepository,
                         CoordinatesService coordinatesService, PeopleService peopleService, LocationService locationService) {
        this.moviesRepository = moviesRepository;
        this.coordinatesService = coordinatesService;
        this.peopleService = peopleService;
        this.locationService = locationService;
    }

    public Movie findById(Long Id){
        Optional<Movie> movie = moviesRepository.findById(Id);
        return movie.orElse(null);
    }

    public Movie findByName(String name){
        Optional<Movie> movie = moviesRepository.findByName(name);
        return movie.orElse(null);
    }

//    public List<Movie> findAll(){
//        return moviesRepository.findAll();
//    }
    public Page<Movie> findAll(Pageable pageable) {
        return moviesRepository.findAll(pageable);
    }

    @Transactional
    public Movie addMovie(Movie movie, Long authorID) {
        return moviesRepository.save(enrichMovie(movie, authorID));
    }

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
            return null; // если пользователь не найден
        }
    }

    @Transactional
    public void deleteMovie(AccessRole userRole, Long authorID, Long id) {
        Optional<Movie> existingMovie = moviesRepository.findById(id);
        if (existingMovie.isPresent()) {
            Movie movie = existingMovie.get();
            if (!authorID.equals(movie.getAuthorID()) && !userRole.equals(AccessRole.ADMIN)) {
                throw new IllegalArgumentException("Author ID does not match and user is not an admin");
            }
        }
        moviesRepository.deleteById(id);
    }

    private Movie enrichMovie(Movie movie, Long authorId) { //как получить Id автора
        movie.setCreationDate(new java.util.Date());
        movie.setAuthorID(authorId);
        return movie;
    }

}