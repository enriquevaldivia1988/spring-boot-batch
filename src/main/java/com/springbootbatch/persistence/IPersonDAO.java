package com.springbootbatch.persistence;

import com.springbootbatch.entities.Person;
import org.springframework.data.repository.CrudRepository;

public interface IPersonDAO extends CrudRepository<Person, Long> {
}
