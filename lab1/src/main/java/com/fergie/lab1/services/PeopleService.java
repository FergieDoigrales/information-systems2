package com.fergie.lab1.services;

import com.fergie.lab1.models.Movie;
import com.fergie.lab1.models.Person;
import com.fergie.lab1.models.User;
import com.fergie.lab1.models.enums.AccessRole;
import com.fergie.lab1.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Transactional(readOnly = true)
@Service
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public Person findById(Long personId){
        Optional<Person> person = peopleRepository.findById(personId);
        return person.orElse(null);
    }

    public Person findByName(String name){
        Optional<Person> person = peopleRepository.findByName(name);
        return person.orElse(null);
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    @Transactional
    public Person addPerson(Person person, Long authorId) {
        return peopleRepository.save(enrichPerson(person, authorId));
    }

    @Transactional
    public Person updatePerson(AccessRole userRole, Long authorID, Long id, Person updatedPerson) {
        Optional<Person> existingPerson = peopleRepository.findById(id);
        if (existingPerson.isPresent()) {
            Person person = existingPerson.get();
                if (!authorID.equals(person.getAuthorID()) && !userRole.equals(AccessRole.ADMIN)) {
                    throw new IllegalArgumentException("Author ID does not match and user is not an admin");
                } //или возвращать null, или кидать исключение

            person.setName(updatedPerson.getName());
            person.setEyeColor(updatedPerson.getEyeColor());
            person.setHairColor(updatedPerson.getHairColor());
            person.setLocation(updatedPerson.getLocation());
            person.setPassportID(updatedPerson.getPassportID());
            person.setNationality(updatedPerson.getNationality());


            return peopleRepository.save(person);
        } else {
            return null;
        }
    }

    @Transactional
    public void deletePerson(AccessRole userRole, Long authorID, Long id) {
        Optional<Person> existingPerson = peopleRepository.findById(id);
        if (existingPerson.isPresent()) {
            Person person = existingPerson.get();
            if (!authorID.equals(person.getAuthorID()) && !userRole.equals(AccessRole.ADMIN)) {
                throw new IllegalArgumentException("Author ID does not match and user is not an admin");
            } //или возвращать null, или кидать исключение
            peopleRepository.deleteById(id);
        }
    }

    private Person enrichPerson(Person person, Long authorId) { //как получить Id автора
        person.setAuthorID(authorId);
        return person;
    }

}
