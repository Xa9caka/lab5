package com.validator;

import com.people.Coordinates;
import com.people.Location;
import com.people.Person;
import com.util.ConsoleManager;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class Validators {

    public static void validateClass(ArrayDeque<Person> collection) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        for (Person person : collection) {
            Set<ConstraintViolation<Coordinates>> validatedCoordinates = validator.validate(person.getCoordinates());
            Set<ConstraintViolation<Location>> validatedLocation = new HashSet<>();
            if (person.getLocation() != null) {
                validatedLocation = validator.validate(person.getLocation());
            }
            Set<ConstraintViolation<Person>> validatedPerson = validator.validate(person);
            if (!validatedPerson.isEmpty() || !validatedCoordinates.isEmpty() || !validatedLocation.isEmpty()) {
                ConsoleManager.printErr("В исходном файле допущены ошибки");
                validatedPerson.stream().map(ConstraintViolation::getMessage)
                        .forEach(ConsoleManager::printErr);
                validatedCoordinates.stream().map(ConstraintViolation::getMessage)
                        .forEach(ConsoleManager::printErr);
                validatedLocation.stream().map(ConstraintViolation::getMessage)
                        .forEach(ConsoleManager::printErr);
                System.exit(1);
            }
        }
        ConsoleManager.printSuccessfulMessage("Данные из файла перенесы в коллекцию, приложение успешно запущено");
    }


}