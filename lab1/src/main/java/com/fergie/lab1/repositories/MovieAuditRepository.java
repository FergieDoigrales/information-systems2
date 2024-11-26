package com.fergie.lab1.repositories;

import com.fergie.lab1.models.MovieAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieAuditRepository extends JpaRepository<MovieAudit, Long> {
}
