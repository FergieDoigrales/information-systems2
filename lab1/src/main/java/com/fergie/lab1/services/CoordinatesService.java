package com.fergie.lab1.services;

import com.fergie.lab1.models.Coordinates;
import com.fergie.lab1.models.enums.AccessRole;
import com.fergie.lab1.repositories.CoordinatesRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class CoordinatesService {
    private final CoordinatesRepository coordinatesRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CoordinatesService(CoordinatesRepository coordinatesRepository, ModelMapper modelMapper) {
        this.coordinatesRepository = coordinatesRepository;
        this.modelMapper = modelMapper;
    }

    public Coordinates findById(Long id){
        Optional<Coordinates> coordinates = coordinatesRepository.findById(id);
        return coordinates.orElse(null);
    }


    public List<Coordinates> findAll(){
        return coordinatesRepository.findAll();
    }

    @Transactional
    public Coordinates addCoordinates(Coordinates coordinates, Long authorId) {
        return coordinatesRepository.save(enrichCoordinates(coordinates, authorId));
    }

    @Transactional
    public Coordinates updateCoordinates(AccessRole userRole, Long authorID, Long id, Coordinates updatedCoordinates) {
        Optional<Coordinates> existingCoordinates = coordinatesRepository.findById(id);
        if (existingCoordinates.isPresent()) {
            Coordinates coordinates = existingCoordinates.get();
            if (!authorID.equals(coordinates.getAuthorID()) && !userRole.equals(AccessRole.ADMIN)) {
                throw new IllegalArgumentException("Author ID does not match and user is not an admin");
            } //или возвращать null, или кидать исключение

            modelMapper.map(updatedCoordinates, coordinates);

            return coordinatesRepository.save(coordinates);
        } else {
            return null;
        }
    }

    @Transactional
    public void deleteCoordinates(AccessRole userRole, Long authorID, Long id) {
        Optional<Coordinates> existingCoordinates = coordinatesRepository.findById(id);
        if (existingCoordinates.isPresent()) {
            Coordinates coordinates = existingCoordinates.get();
            if (!authorID.equals(coordinates.getAuthorID()) && !userRole.equals(AccessRole.ADMIN)) {
                throw new IllegalArgumentException("Author ID does not match and user is not an admin");
            } //или возвращать null, или кидать исключение
            coordinatesRepository.deleteById(id);
        }
    }

    private Coordinates enrichCoordinates(Coordinates coordinates, Long authorId) { //как получить Id автора
        coordinates.setAuthorID(authorId);
        return coordinates;
    }
}
