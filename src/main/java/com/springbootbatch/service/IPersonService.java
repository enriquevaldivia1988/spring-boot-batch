package com.springbootbatch.service;


import com.springbootbatch.entities.Person;

import java.util.List;

public interface IPersonService {

    Iterable<Person> saveAll(List<Person> personList);
}
