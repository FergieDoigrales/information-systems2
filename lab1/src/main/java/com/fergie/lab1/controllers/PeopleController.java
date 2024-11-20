package com.fergie.lab1.controllers;
import com.fergie.lab1.dto.PersonDTO;
import com.fergie.lab1.models.Coordinates;
import com.fergie.lab1.models.Location;
import com.fergie.lab1.models.Movie;
import com.fergie.lab1.models.Person;
import com.fergie.lab1.models.enums.Color;
import com.fergie.lab1.models.enums.Country;
import com.fergie.lab1.security.CustomUserDetails;
import com.fergie.lab1.services.LocationService;
import com.fergie.lab1.services.PeopleService;
import com.fergie.lab1.services.PreparePageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/person")
@Controller
public class PeopleController {
    private final PeopleService peopleService;

    private final LocationService locationService;
    private final ModelMapper modelMapper;

    private final PreparePageService preparePageService;


    @Autowired
    public PeopleController(PeopleService peopleService, ModelMapper modelMapper, LocationService locationService,
                            PreparePageService preparePageService) {
        this.peopleService = peopleService;
        this.locationService = locationService;
        this.modelMapper = modelMapper;
        this.preparePageService = preparePageService;
    }

    @ModelAttribute("LocationFormAttributes")
    public void addLocationFormAttributes(Model model) {
        model.addAttribute("location", new Location());
    }
    @ModelAttribute("CoordinatesFormAttributes")
    public void addCoordinatesFormAttributes(Model model) {
        model.addAttribute("coordinates", new Coordinates());
    }
//    @PostMapping("/save")
//    @ResponseBody
//    public ResponseEntity<?> savePerson(@ModelAttribute("person") PersonDTO personDTO, Long authorId, @RequestParam("parentModal") String parentModal,
//                                        Model model) {
//        personDTO.setLocation(locationService.findById(personDTO.getLocation().getId()));
//        peopleService.addPerson(convertToPerson(personDTO), authorId);
//        model.addAttribute("openModal", parentModal);
//        return ResponseEntity.ok().build();
//    }
@PostMapping("/save")
public ResponseEntity<?> savePerson(@ModelAttribute("person") PersonDTO personDTO) {
    try {
        Person person = convertToPerson(personDTO);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        peopleService.addPerson(person, userDetails.getId());

        List<Person> updatedPersonsList = peopleService.findAll();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "updatedPersonsList", updatedPersonsList
        ));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "message", e.getMessage()
        ));
    }
}
    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }
}
