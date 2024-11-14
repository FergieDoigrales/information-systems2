package com.fergie.lab1.controllers;

import com.fergie.lab1.dto.MovieDTO;
import com.fergie.lab1.models.Movie;
import com.fergie.lab1.services.MoviesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MoviesController {
    private final MoviesService moviesService;
    private final ModelMapper modelMapper;

    @Autowired
    public MoviesController(MoviesService moviesService, ModelMapper modelMapper) {
        this.moviesService = moviesService;
        this.modelMapper = modelMapper;
    }

    private Movie convertToMovie(MovieDTO movieDTO) {
        return modelMapper.map(movieDTO, Movie.class);
    }

    private MovieDTO convertToMovieDTO(Movie movie) {
        return modelMapper.map(movie, MovieDTO.class);
    }
}