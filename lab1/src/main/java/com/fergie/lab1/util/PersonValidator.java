package com.fergie.lab1.util;

import com.fergie.lab1.models.Location;
import com.fergie.lab1.models.Person;
import com.fergie.lab1.models.User;
import com.fergie.lab1.models.enums.Color;
import com.fergie.lab1.models.enums.Country;
import com.fergie.lab1.repositories.PeopleRepository;
import com.fergie.lab1.repositories.UsersRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonValidator(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (person.getName() == null || person.getName().isEmpty()) {
            errors.rejectValue("name", "", "Name cannot be null or empty.");
        }

        if (person.getEyeColor() == null) {
            errors.rejectValue("eyeColor", "", "Eye color cannot be null.");
        }

        if (person.getLocation() == null) {
            errors.rejectValue("location", "", "Location cannot be null.");
        }

        if (person.getPassportID() == null || person.getPassportID().length() < 10 || person.getPassportID().length() > 43) {
            errors.rejectValue("passportID", "", "Passport ID length must be between 10 and 43.");
        }

        if (person.getNationality() == null) {
            errors.rejectValue("nationality", "", "Nationality cannot be null.");

        }
    }
}
