package com.fergie.lab1.util;

import com.fergie.lab1.models.User;
import com.fergie.lab1.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UsersRepository usersRepository; //userRepo или UserDetailService

    @Autowired
    public UserValidator(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target; //downcast??

        if (usersRepository.findByUsername(user.getUsername()).isPresent()){ //isPresent - true, если значение есть
            errors.rejectValue("username", "", "This username is already taken.");
        }

    }
}
