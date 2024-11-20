package com.fergie.lab1.controllers;

import com.fergie.lab1.dto.CoordinatesDTO;
import com.fergie.lab1.dto.LocationDTO;
import com.fergie.lab1.models.Coordinates;
import com.fergie.lab1.models.Location;
import com.fergie.lab1.security.CustomUserDetails;
import com.fergie.lab1.services.CoordinatesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> saveCoordinates(@ModelAttribute("coordinates") CoordinatesDTO coordinatesDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            coordinatesService.addCoordinates(convertToCoordinates(coordinatesDTO), userDetails.getId());
            List<Coordinates> updatedCoordinatesList = coordinatesService.findAll();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "updatedCoordinatesList", updatedCoordinatesList
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    private Coordinates convertToCoordinates(CoordinatesDTO coordinatesDTO) {
        return modelMapper.map(coordinatesDTO, Coordinates.class);
    }

    private CoordinatesDTO convertToCoordinatesDTO(Coordinates coordinates) {
        return modelMapper.map(coordinates, CoordinatesDTO.class);
    }
}
