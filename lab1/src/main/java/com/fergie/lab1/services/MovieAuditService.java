package com.fergie.lab1.services;

import com.fergie.lab1.models.Movie;
import com.fergie.lab1.models.MovieAudit;
import com.fergie.lab1.repositories.MovieAuditRepository;
import com.fergie.lab1.repositories.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MovieAuditService {

    private final MovieAuditRepository movieAuditRepository;

    @Autowired
    public MovieAuditService(MovieAuditRepository movieAuditRepository) {
        this.movieAuditRepository = movieAuditRepository;
    }

    public Page<MovieAudit> findAll(Pageable pageable) {
        return movieAuditRepository.findAll(pageable);
    }

    public void recordMovieChange(Movie movie, String action, String field, String oldValue, String newValue, String changedBy) {
        MovieAudit audit = new MovieAudit();
        audit.setMovieId(movie.getMovieId());
        audit.setAction(action);
        audit.setField(field);
        audit.setOldValue(oldValue);
        audit.setNewValue(newValue);
        audit.setChangedBy(changedBy);
        audit.setChangedAt(new Date());

        movieAuditRepository.save(audit);
    }
}
