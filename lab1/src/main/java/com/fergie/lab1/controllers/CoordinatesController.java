package com.fergie.lab1.controllers;

import com.fergie.lab1.dto.CoordinatesDTO;
import com.fergie.lab1.models.Coordinates;
import com.fergie.lab1.services.CoordinatesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/coordinates")
@Controller
public class CoordinatesController {
    private final CoordinatesService coordinatesService;
    private final ModelMapper modelMapper;


    @Autowired
    public CoordinatesController(ModelMapper modelMapper, CoordinatesService coordinatesService) {
        this.coordinatesService = coordinatesService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/save")
    public String saveCoordinates(@ModelAttribute("coordinates") CoordinatesDTO coordinatesDTO, Long authorId) {
        coordinatesService.addCoordinates(convertToCoordinates(coordinatesDTO), authorId);
        return "redirect:/home";
    }

    private Coordinates convertToCoordinates(CoordinatesDTO coordinatesDTO) {
        return modelMapper.map(coordinatesDTO, Coordinates.class);
    }

    private CoordinatesDTO convertToCoordinatesDTO(Coordinates coordinates) {
        return modelMapper.map(coordinates, CoordinatesDTO.class);
    }
}
