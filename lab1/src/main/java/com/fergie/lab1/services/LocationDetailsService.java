package com.fergie.lab1.services;

import com.fergie.lab1.models.LocationDetails;
import com.fergie.lab1.repositories.LocationDetailsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@org.springframework.transaction.annotation.Transactional(readOnly = true)
@Service
public class LocationDetailsService {
    private final LocationDetailsRepository locationDetailsRepository;

    @Autowired
    public LocationDetailsService(LocationDetailsRepository locationDetailsRepository) {
        this.locationDetailsRepository = locationDetailsRepository;
    }

    @Transactional
    public LocationDetails save(LocationDetails locationDetails) {
        return locationDetailsRepository.save(locationDetails);
    }

    public LocationDetails findById(Long id) {
        Optional<LocationDetails> locationDetails = locationDetailsRepository.findById(id);
        return locationDetails.orElse(null);
    }

}
