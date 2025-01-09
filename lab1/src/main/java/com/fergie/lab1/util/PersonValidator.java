package com.fergie.lab1.util;


import com.fergie.lab1.models.Person;
import com.fergie.lab1.repositories.PeopleRepository;
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

    public static boolean isPersonValid(Person person) {
        if (person.getName() == null || person.getName().isEmpty()) {
            System.out.println("Name cannot be null or empty.");
            return false;
        }
        if (person.getEyeColor() == null) {
            System.out.println("Eye color cannot be null.");
            return false;
        }
        if (person.getLocation() == null) {
            System.out.println("Location cannot be null.");
            return false;
        }
        if (person.getPassportID() == null || person.getPassportID().length() < 10 || person.getPassportID().length() > 43) {
            System.out.println("Passport ID length must be between 10 and 43.");
            return false;
        }
        if (person.getNationality() == null) {
            System.out.println("Nationality cannot be null.");
            return false;
        }
        return true;
    }
}
