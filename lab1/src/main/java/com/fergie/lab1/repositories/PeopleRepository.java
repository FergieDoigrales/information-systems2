package com.fergie.lab1.repositories;


import com.fergie.lab1.models.Person;
import com.fergie.lab1.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PeopleRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByName(String name);
}
