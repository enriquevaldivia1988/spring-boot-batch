package com.springbootbatch.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springbootbatch.entities.Person;
import com.springbootbatch.persistence.IPersonDAO;

import jakarta.transaction.Transactional;

@Service
public class PersonServiceImpl implements IPersonService {

    private final IPersonDAO personDAO;

    public PersonServiceImpl(IPersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    @Transactional
    public Iterable<Person> saveAll(List<Person> personList) {
        return personDAO.saveAll(personList);
    }
}
