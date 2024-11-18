package com.fergie.lab1.repositories;


import com.fergie.lab1.models.Person;
import com.fergie.lab1.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PeopleRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByName(String name);
}
