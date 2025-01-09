package com.fergie.lab1.services;
import com.fergie.lab1.models.Person;
import com.fergie.lab1.models.enums.AccessRole;
import com.fergie.lab1.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value = "peopleCache", key = "#personId")
    public Person findById(Long personId){
        Optional<Person> person = peopleRepository.findById(personId);
        return person.orElse(null);
    }

    @Cacheable(value = "peopleCache", key = "#name")
    public Person findByName(String name){
        Optional<Person> person = peopleRepository.findByName(name);
        return person.orElse(null);
    }
    @Cacheable(value = "peopleCache", key = "'allPeople'")
    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    public List<Person> getScreenwritersWithoutOscar() {
        return peopleRepository.findScreenwritersWithoutOscar();
    }
    public List<Person> getOperatorsWithoutOscar() {
        return peopleRepository.findOperatorsWithoutOscar();
    }

    @CacheEvict(value = "peopleCache", allEntries = true)
    @Transactional
    public Person addPerson(Person person, Long authorId) {
        if (!isPassportUnique(person.getPassportID())) {
            throw new IllegalArgumentException("Passport ID is not unique");
        }
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

    private Boolean isPassportUnique(String passportID) {
        return peopleRepository.findFirstByPassportID(passportID).isEmpty();
    }

    private Person enrichPerson(Person person, Long authorId) { //как получить Id автора
        person.setAuthorID(authorId);
        return person;
    }

}
