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
    private final MoviesRepository moviesRepository;
    private final CoordinatesService coordinatesService;
    private final PeopleService peopleService;
    private final LocationService locationService;

    @Autowired
    public PreparePageService(MoviesRepository moviesRepository,
                         CoordinatesService coordinatesService, PeopleService peopleService, LocationService locationService) {
        this.moviesRepository = moviesRepository;
        this.coordinatesService = coordinatesService;
        this.peopleService = peopleService;
        this.locationService = locationService;
    }
    public void prepareHomePage(Model model, int page, int size, String sort, String openModal) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        Page<Movie> moviePage = moviesRepository.findAll(pageable);
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("movie", new Movie());
        model.addAttribute("coordinatesList", coordinatesService.findAll());
        model.addAttribute("personsList", peopleService.findAll());
        model.addAttribute("mpaaRatings", MpaaRating.values());
        model.addAttribute("genres", MovieGenre.values());
        model.addAttribute("locationsList", locationService.findAll());
        model.addAttribute("colors", Color.values());
        model.addAttribute("nationalities", Country.values());
        model.addAttribute("openModal", openModal);
    }
}
