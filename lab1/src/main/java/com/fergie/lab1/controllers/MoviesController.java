package com.fergie.lab1.controllers;

import com.fergie.lab1.dto.MovieDTO;
import com.fergie.lab1.models.Coordinates;
import com.fergie.lab1.models.Movie;
import com.fergie.lab1.models.Person;
import com.fergie.lab1.models.enums.MovieGenre;
import com.fergie.lab1.models.enums.MpaaRating;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

//    @GetMapping("/create")
//    public String showCreateForm(Model model) {
//        model.addAttribute("movie", new Movie());
//        model.addAttribute("coordinatesList", coordinatesService.findAll());
//        model.addAttribute("personsList", peopleService.findAll());
//        model.addAttribute("mpaaRatings", MpaaRating.values());
//        model.addAttribute("genres", MovieGenre.values());
//        return "movie";
//    }

    @PostMapping("/save")
    public String saveMovie(@ModelAttribute("movie") Movie movie) {
        movie.setCoordinates(coordinatesService.findById(movie.getCoordinates().getId()));
        movie.setOperator(peopleService.findById(movie.getOperator().getId()));
        movie.setScreenwriter(peopleService.findById(movie.getScreenwriter().getId()));
        movie.setDirector(peopleService.findById(movie.getDirector().getId()));
        moviesService.addMovie(movie);
        return "redirect:/home";
    }


    private Movie convertToMovie(MovieDTO movieDTO) {
        return modelMapper.map(movieDTO, Movie.class);
    }

    private MovieDTO convertToMovieDTO(Movie movie) {
        return modelMapper.map(movie, MovieDTO.class);
    }
}
