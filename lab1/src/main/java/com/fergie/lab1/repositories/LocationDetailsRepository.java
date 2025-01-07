package com.fergie.lab1.repositories;


import com.fergie.lab1.models.LocationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationDetailsRepository extends JpaRepository<LocationDetails, Long> {

}
