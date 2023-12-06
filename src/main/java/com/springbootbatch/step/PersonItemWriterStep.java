package com.springbootbatch.step;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import com.springbootbatch.entities.Person;
import com.springbootbatch.service.IPersonService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonItemWriterStep implements ItemWriter<Person> {

    private final IPersonService personService;

    public PersonItemWriterStep(IPersonService personService) {
        this.personService = personService;
    }

    /**
     * Writes the items in the chunk to the data store.
     * 
     * @param chunk the chunk containing the items to be written
     * @throws Exception if an error occurs during the write operation
     */
    @Override
    public void write(Chunk<? extends Person> chunk) throws Exception {
        log.info("Ingreso al writer");

        Set<Person> uniquePersons = new HashSet<>(chunk.getItems());
        uniquePersons.forEach(person -> log.info(person.toString()));

        personService.saveAll(new ArrayList<>(uniquePersons));
    }
}
