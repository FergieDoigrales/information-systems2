package com.fergie.lab1.services;

import com.fergie.lab1.dto.MovieDTO;

import com.fergie.lab1.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fergie.lab1.models.enums.ImportStatus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class ImportService {

    private final MoviesService moviesService;
    private final CoordinatesService coordinatesService;
    private final PeopleService peopleService;
    private final LocationService locationService;
    private final LocationDetailsService locationDetailsService;
    private final ImportAuditService importAuditService;

    @Autowired
    public ImportService(MoviesService moviesService, CoordinatesService coordinatesService, ImportAuditService importAuditService,
                         PeopleService peopleService, LocationService locationService, LocationDetailsService locationDetailsService) {
        this.moviesService = moviesService;
        this.coordinatesService = coordinatesService;
        this.peopleService = peopleService;
        this.locationService = locationService;
        this.locationDetailsService = locationDetailsService;
        this.importAuditService = importAuditService;

    }

    @Transactional
    public ImportAudit importFile(MultipartFile file, Long userId, String fileHash) throws IOException {
        Optional<ImportAudit> existingAudit = importAuditService.findByHash(fileHash);
        if (existingAudit.isPresent()) {
            ImportAudit audit = existingAudit.get();
            if (audit.getErrorRecords() > 0.5 * audit.getTotalRecords()) {
                throw new IllegalArgumentException("This file was previously rejected (more than 50% errors)");
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();

        int totalRecords = 0;
        int errorRecords = 0;
        int validRecords = 0;

        try {
            List<Movie> movies = new ArrayList<>();
            List<MovieDTO> movieDTOList = objectMapper.readValue(file.getInputStream(), new TypeReference<List<MovieDTO>>() {
            });

            for (MovieDTO movieDTO : movieDTOList) {
                totalRecords++;

                Movie movie = moviesService.processMovieDTO(movieDTO, userId);
                if (movie != null) {
                    movies.add(saveMovieRelatedEntities(movie, userId));
                    validRecords++;
                } else {
                    errorRecords++;
                }
            }

            ImportAudit audit = new ImportAudit();
            audit.setFileHash(fileHash);
            audit.setAuthorID(userId);
            audit.setTotalRecords(totalRecords);
            audit.setSuccessRecords(validRecords);
            audit.setErrorRecords(errorRecords);
            audit.setImportDate(new Date());
            audit.setStatus(validRecords >= 0.5 * totalRecords ? ImportStatus.SUCCESS : ImportStatus.FAILED);

            importAuditService.save(audit);

            if (errorRecords < 0.5 * totalRecords) {
                moviesService.saveAll(movies);
            } else {
                throw new IllegalArgumentException("More than 50% of records are invalid");
            }
            return audit;

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("More than 50% of records are invalid", e);
        } catch (Exception e) {
            ImportAudit audit = new ImportAudit();
            audit.setFileHash(fileHash);
            audit.setAuthorID(userId);
            audit.setTotalRecords(totalRecords);
            audit.setSuccessRecords(validRecords);
            audit.setErrorRecords(errorRecords);
            audit.setImportDate(new Date());
            audit.setStatus(ImportStatus.FAILED);

            importAuditService.save(audit);
            throw new RuntimeException("Failed to import file", e);
        }
    }

    @Transactional
    public Movie saveMovieRelatedEntities(Movie movie, Long userId) {
        System.out.println(movie.getCoordinates());
        if (movie.getCoordinates().getId() == null) {
            movie.setCoordinates(coordinatesService.addCoordinates(movie.getCoordinates(), userId));
        } else {
            movie.setCoordinates(coordinatesService.findById(movie.getCoordinates().getId()));
        }

        if (movie.getDirector() != null && movie.getDirector().getId() == null) {
            if (movie.getDirector().getLocation().getId() == null) {
                movie.getDirector().getLocation().setLocationDetails(saveLocationDetails(movie.getDirector()));
            }
            movie.getDirector().setLocation(saveLocation(movie.getDirector(), userId));
        }

        if (movie.getScreenwriter() != null && movie.getScreenwriter().getId() == null) {
            if (movie.getScreenwriter().getLocation().getId() == null) {
                movie.getScreenwriter().getLocation().setLocationDetails(saveLocationDetails(movie.getScreenwriter()));
            }
            movie.getScreenwriter().setLocation(saveLocation(movie.getScreenwriter(), userId));
        }

        if (movie.getOperator().getId() == null) {
            if (movie.getOperator().getLocation().getId() == null) {
                movie.getOperator().getLocation().setLocationDetails(saveLocationDetails(movie.getOperator()));
            }
            movie.getOperator().setLocation(saveLocation(movie.getOperator(), userId));
        }

        movie.setDirector(savePerson(movie.getDirector(), userId));
        movie.setScreenwriter(savePerson(movie.getScreenwriter(), userId));
        movie.setOperator(savePerson(movie.getOperator(), userId));

        return movie;
    }

    private Person savePerson(Person person, Long userId) {
        if (person != null) {
            if (person.getId() == null) {
                return peopleService.addPerson(person, userId);
            } else {
                return peopleService.findById(person.getId());
            }
        } else {return null;}
    }

    private Location saveLocation(Person person, Long userId) {
        if (person.getLocation().getId() == null) {
            return locationService.addLocation(person.getLocation(), userId);
        } else {
            return locationService.findById(person.getLocation().getId());
        }
    }

    private LocationDetails saveLocationDetails(Person person) {
        if (person.getLocation().getLocationDetails() != null) {
            if (person.getLocation().getLocationDetails().getId() == null) {
                return locationDetailsService.save(person.getLocation().getLocationDetails());
            } else {
                return locationDetailsService.findById(person.getLocation().getLocationDetails().getId());
            }
        } else {return null; }
    }

}

