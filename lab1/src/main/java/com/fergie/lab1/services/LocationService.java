package com.fergie.lab1.services;

import com.fergie.lab1.models.Location;
import com.fergie.lab1.models.enums.AccessRole;
import com.fergie.lab1.repositories.LocationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;
    
    @Autowired
    public LocationService(LocationRepository locationRepository, ModelMapper modelMapper) {
        this.locationRepository = locationRepository;
        this.modelMapper = modelMapper;
    }

    @Cacheable(value = "locationCache", key = "#locationId")
    public Location findById(Long locationId){
        Optional<Location> location = locationRepository.findById(locationId);
        return location.orElse(null);
    }
    @Cacheable(value = "locationCache", key = "'allLocation'")
    public List<Location> findAll(){
        return locationRepository.findAll();
    }

    @CacheEvict(value = "locationCache", allEntries = true)
    @Transactional
    public Location addLocation(Location location, Long authorId) {
        if (!isLocationUnique(location.getX(), location.getY(), location.getZ())) {
            throw new IllegalArgumentException("Location is not unique");
        }
        return locationRepository.save(enrichLocation(location, authorId));
    }

    @Transactional
    public Location updateLocation(AccessRole userRole, Long authorID, Long id, Location updatedLocation) {
        Optional<Location> existingLocation = locationRepository.findById(id);
        if (existingLocation.isPresent()) {
            Location location = existingLocation.get();
            if (!authorID.equals(location.getAuthorID()) && !userRole.equals(AccessRole.ADMIN)) {
                throw new IllegalArgumentException("Author ID does not match and user is not an admin");
            } //или возвращать null, или кидать исключение

            modelMapper.map(updatedLocation, location);

            return locationRepository.save(location);
        } else {
            return null;
        }
    }

    @Transactional
    public void deleteLocation(AccessRole userRole, Long authorID, Long id) {
        Optional<Location> existingLocation = locationRepository.findById(id);
        if (existingLocation.isPresent()) {
            Location location = existingLocation.get();
            if (!authorID.equals(location.getAuthorID()) && !userRole.equals(AccessRole.ADMIN)) {
                throw new IllegalArgumentException("Author ID does not match and user is not an admin");
            } //или возвращать null, или кидать исключение
            locationRepository.deleteById(id);
        }
    }

    private Boolean isLocationUnique(Double x, float y, long z) {
        return locationRepository.findByXAndYAndZ(x, y, z).isEmpty();
    }

    private Location enrichLocation(Location location, Long authorId) { //как получить Id автора
        location.setAuthorID(authorId);
        return location;
    }
}
