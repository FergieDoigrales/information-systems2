package com.fergie.lab1.services;

import com.fergie.lab1.models.Movie;
import com.fergie.lab1.models.enums.Color;
import com.fergie.lab1.models.enums.Country;
import com.fergie.lab1.models.enums.MovieGenre;
import com.fergie.lab1.models.enums.MpaaRating;
import com.fergie.lab1.repositories.MoviesRepository;
import com.fergie.lab1.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Transactional(readOnly = true)
@Service
public class PreparePageService {

    private final MoviesService moviesService;

    @Autowired
    public PreparePageService(MoviesService moviesService) {
        this.moviesService = moviesService;
    }
    public Page<Movie> getMoviePage(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return moviesService.findAll(pageable);
    }

}
