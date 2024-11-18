package com.fergie.lab1.controllers;
import com.fergie.lab1.dto.LocationDTO;
import com.fergie.lab1.dto.PersonDTO;
import com.fergie.lab1.models.Location;
import com.fergie.lab1.models.Person;
import com.fergie.lab1.services.LocationService;
import com.fergie.lab1.services.PeopleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/location")
@Controller
public class LocationController {
    private final LocationService locationService;
    private final ModelMapper modelMapper;


    @Autowired
    public LocationController(ModelMapper modelMapper, LocationService locationService) {
        this.locationService = locationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/save")
    public String saveLocation(@ModelAttribute("location") LocationDTO locationDTO, Long authorId) {
        locationService.addLocation(convertToLocation(locationDTO), authorId);
        return "redirect:/home";
    }

    private Location convertToLocation(LocationDTO locationDTO) {
        return modelMapper.map(locationDTO, Location.class);
    }

    private LocationDTO convertToLocationDTO(Location location) {
        return modelMapper.map(location, LocationDTO.class);
    }
}
