package com.springbootbatch.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "persons")
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_generator")
    @SequenceGenerator(name="person_generator", sequenceName = "person_seq", allocationSize=1)
    private Long id;

    private String cardId;

    private String name;

    @Column(name = "last_name")
    private String lastName;
    private int age;
    private String createAt;

    private static final long serialVersionUID = -3670843597762726141L;
}
