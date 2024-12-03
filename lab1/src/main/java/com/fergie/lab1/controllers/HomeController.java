package com.fergie.lab1.controllers;

import com.fergie.lab1.models.*;
import com.fergie.lab1.models.enums.Color;
import com.fergie.lab1.models.enums.Country;
import com.fergie.lab1.models.enums.MovieGenre;
import com.fergie.lab1.models.enums.MpaaRating;
import com.fergie.lab1.security.CustomUserDetails;
import com.fergie.lab1.security.UserDetailService;
import com.fergie.lab1.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class HomeController {
    private final MoviesService moviesService;
    private final CoordinatesService coordinatesService;
    private final PeopleService peopleService;
    private final LocationService locationService;

    private final RequestsService requestsService;

    private final UserDetailService userDetailService;

    @Autowired
    public HomeController(MoviesService moviesService, PeopleService peopleService,
                          CoordinatesService coordinatesService, LocationService locationService,
                          RequestsService requestsService, UserDetailService userDetailService) {
        this.moviesService = moviesService;
        this.peopleService = peopleService;
        this.coordinatesService = coordinatesService;
        this.locationService = locationService;
        this.requestsService = requestsService;
        this.userDetailService = userDetailService;
    }

    @GetMapping("/home")
    public String getMovies(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(defaultValue = "name") String sort
    ) {
//        String token = (String) authentication.getCredentials();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = userDetailService.getUserInfo();
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());
        model.addAttribute("currentPage", page);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        Page<Movie> moviePage = moviesService.findAll(pageable);
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("currentUserId", userDetails.getId());
        model.addAttribute("userRole", userDetails.getRole().name());
        return "home";
    }

    @PostMapping("/sendRequest")
    public ResponseEntity<?> sendRequest(@ModelAttribute("request") RoleRequest roleRequest) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            roleRequest.setUsername(userDetails.getUsername());
            requestsService.addRequest(roleRequest);

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

    @GetMapping("/operations")
    public String getOperations(Model model) {
        CustomUserDetails userDetails = userDetailService.getUserInfo();
        model.addAttribute("currentUserId", userDetails.getId());
        model.addAttribute("userRole", userDetails.getRole().name());
        model.addAttribute("username", userDetails.getUsername());
        return "operations";
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

}


