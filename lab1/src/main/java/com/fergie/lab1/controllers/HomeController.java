package com.fergie.lab1.controllers;

import com.fergie.lab1.models.Coordinates;
import com.fergie.lab1.models.Location;
import com.fergie.lab1.models.Movie;
import com.fergie.lab1.models.Person;
import com.fergie.lab1.models.enums.Color;
import com.fergie.lab1.models.enums.Country;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    private final MoviesService moviesService;
    private final CoordinatesService coordinatesService;
    private final PeopleService peopleService;
    private final LocationService locationService;

    @Autowired
    public HomeController(MoviesService moviesService, PeopleService peopleService,
                         CoordinatesService coordinatesService, LocationService locationService) {
        this.moviesService = moviesService;
        this.peopleService = peopleService;
        this.coordinatesService = coordinatesService;
        this.locationService = locationService;
    }

//    @GetMapping("/home")
////    @ResponseBody
//    public String homePage() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal(); //точно ли customuserdetails?
//
////        return userDetails.getUsername();
//
//        return "home";
//    }
    @GetMapping("/home")
    public String getMovies(Model model,
                                 @RequestParam(defaultValue = "0") int page,       // Номер страницы
                                 @RequestParam(defaultValue = "10") int size,     // Размер страницы (по умолчанию 10)
                                 @RequestParam(defaultValue = "name") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        Page<Movie> moviePage = moviesService.findAll(pageable);
        model.addAttribute("moviePage", moviePage);
        return "home";
    }

    @ModelAttribute("movieFormAttributes")
    public void addFormAttributes(Model model) {
        model.addAttribute("movie", new Movie());
        model.addAttribute("coordinatesList", coordinatesService.findAll());
        model.addAttribute("personsList", peopleService.findAll());
        model.addAttribute("mpaaRatings", MpaaRating.values());
        model.addAttribute("genres", MovieGenre.values());
    }
    @ModelAttribute("personFormAttributes")
    public void addMovieFormAttributes(Model model) {
        model.addAttribute("person", new Person());
        model.addAttribute("locationsList", locationService.findAll());
        model.addAttribute("colors", Color.values());
        model.addAttribute("nationalities", Country.values());
    }

    @ModelAttribute("LocationFormAttributes")
    public void addLocationFormAttributes(Model model) {
        model.addAttribute("location", new Location());
    }

    @ModelAttribute("CoordinatesFormAttributes")
    public void addCoordinatesFormAttributes(Model model) {
        model.addAttribute("coordinates", new Coordinates());
    }

//    model.addAttribute("locations", locationService.findAll());
//    model.addAttribute("coordinates", coordinatesService.findAll());
//
//    model.addAttribute("person", new Person());
//    model.addAttribute("location", new Location());
//    model.addAttribute("coordinates", new Coordinates());

}
//    @GetMapping("/home")
//    public String homePage(Model model,
//                           @RequestParam(defaultValue = "0") int page,
//                           @RequestParam(defaultValue = "10") int size) {
//        Page<Movie> moviePage = moviesService.findAll(PageRequest.of(page, size));
//        model.addAttribute("moviePage", moviePage);
//        return "home";
//    }
//}


