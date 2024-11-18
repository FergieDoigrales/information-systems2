package com.fergie.lab1.controllers;
import com.fergie.lab1.dto.PersonDTO;
import com.fergie.lab1.models.Movie;
import com.fergie.lab1.models.Person;
import com.fergie.lab1.services.LocationService;
import com.fergie.lab1.services.PeopleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/person")
@Controller
public class PeopleController {
    private final PeopleService peopleService;

    private final LocationService locationService;
    private final ModelMapper modelMapper;


    @Autowired
    public PeopleController(PeopleService peopleService, ModelMapper modelMapper, LocationService locationService) {
        this.peopleService = peopleService;
        this.locationService = locationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/save")
    public String savePerson(@ModelAttribute("person") PersonDTO personDTO, Long authorId, @RequestParam("parentModal") String parentModal,
                             Model model) {
        personDTO.setLocation(locationService.findById(personDTO.getLocation().getId()));
        peopleService.addPerson(convertToPerson(personDTO), authorId);
        model.addAttribute("openModal", parentModal);
        return "redirect:/home";
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }
}
