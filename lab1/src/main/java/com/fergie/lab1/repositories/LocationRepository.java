package com.fergie.lab1.repositories;

import com.fergie.lab1.models.Location;
import com.fergie.lab1.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
