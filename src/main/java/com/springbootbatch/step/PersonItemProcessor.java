package com.springbootbatch.step;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.item.ItemProcessor;

import com.springbootbatch.entities.Person;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    /**
     * This class represents a person.
     */
    @Override
    public Person process(Person person) throws Exception {

        if (person == null) {
            log.error("The item of ItemProcessor is null");
            throw new RuntimeException();
        }

        if (!isValid(person)) {
            throw new RuntimeException("Los datos de la persona no son válidos");
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();

        person.setCreateAt(dateTimeFormatter.format(localDateTime));

        return person;
    }

    private boolean isValid(Person person) {

        if (person.getName() == null || person.getName().isEmpty() || person.getName().length() > 50) {
            return false;
        }

        if (person.getLastName() == null || person.getLastName().isEmpty() || person.getLastName().length() > 50) {
            return false;
        }

        return true;
    }
}
