package com.fergie.lab1.controllers;

import com.fergie.lab1.dto.LocationDTO;
import com.fergie.lab1.dto.MovieDTO;
import com.fergie.lab1.models.Coordinates;
import com.fergie.lab1.models.Location;
import com.fergie.lab1.models.Movie;
import com.fergie.lab1.models.Person;
import com.fergie.lab1.models.enums.MovieGenre;
import com.fergie.lab1.models.enums.MpaaRating;
import com.fergie.lab1.security.CustomUserDetails;
import com.fergie.lab1.services.CoordinatesService;
import com.fergie.lab1.services.LocationService;
import com.fergie.lab1.services.MoviesService;
import com.fergie.lab1.services.PeopleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/movie")
public class MoviesController {
    private final MoviesService moviesService;
    private final PeopleService peopleService;

    private final CoordinatesService coordinatesService;

    private final ModelMapper modelMapper;




    @Autowired
    public MoviesController(MoviesService moviesService, ModelMapper modelMapper, PeopleService peopleService,
                            CoordinatesService coordinatesService) {
        this.moviesService = moviesService;
        this.modelMapper = modelMapper;
        this.peopleService = peopleService;
        this.coordinatesService = coordinatesService;
    }


    @PostMapping("/save")
    public ResponseEntity<?> saveMovie(@ModelAttribute("movie") Movie movie) {
        try {

            setRelatedEntities(movie);
            CustomUserDetails userDetails = getUserInfo();
            moviesService.addMovie(movie, userDetails.getId());

            return ResponseEntity.ok(Map.of(
                    "success", true
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Movie movie = moviesService.findById(id);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateMovie(@ModelAttribute("movie") MovieDTO movieDTO) {
        System.out.println("Entering updateMovie method");
        CustomUserDetails userDetails = getUserInfo();
        Movie movie = convertToMovie(movieDTO);

        try {
            setRelatedEntities(movie);
            Movie updatedMovie = moviesService.updateMovie(userDetails.getRole(), userDetails.getId(), movie.getMovieId(), movie);

            if (updatedMovie != null) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "updatedMovie", updatedMovie
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Movie not found"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        CustomUserDetails userDetails = getUserInfo();
        try{
        moviesService.deleteMovie(userDetails.getRole(), userDetails.getId(), id);
        return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("success", false, "message", "Access denied"));
        }
    }

    private CustomUserDetails getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserDetails) authentication.getPrincipal();
    }
    private void setRelatedEntities(Movie movie) {
        movie.setCoordinates(coordinatesService.findById(movie.getCoordinates().getId()));
        movie.setOperator(peopleService.findById(movie.getOperator().getId()));
        movie.setScreenwriter(peopleService.findById(movie.getScreenwriter().getId()));
        movie.setDirector(peopleService.findById(movie.getDirector().getId()));
    }
    private Movie convertToMovie(MovieDTO movieDTO) {
        return modelMapper.map(movieDTO, Movie.class);
    }

    private MovieDTO convertToMovieDTO(Movie movie) {
        return modelMapper.map(movie, MovieDTO.class);
    }
}
