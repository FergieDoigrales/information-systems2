package com.fergie.lab1.repositories;


import com.fergie.lab1.models.Person;
import com.fergie.lab1.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PeopleRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByName(String name);

    @Query(value = "SELECT * FROM get_screenwriters_without_oscar()", nativeQuery = true)
    List<Person> findScreenwritersWithoutOscar();

    @Query(value = "SELECT * FROM get_operators_without_oscar()", nativeQuery = true)
    List<Person> findOperatorsWithoutOscar();
}
