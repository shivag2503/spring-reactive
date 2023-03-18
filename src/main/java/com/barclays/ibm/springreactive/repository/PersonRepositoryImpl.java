package com.barclays.ibm.springreactive.repository;

import com.barclays.ibm.springreactive.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class PersonRepositoryImpl implements PersonRepository {

    Person person1 = Person.builder().id(1).firstName("Rakesh").lastName("Somani").build();
    Person person2 = Person.builder().id(2).firstName("Jack").lastName("Thomas").build();
    Person person3 = Person.builder().id(3).firstName("Suresh").lastName("Rana").build();
    Person person4 = Person.builder().id(4).firstName("Surendra").lastName("Bansal").build();

    @Override
    public Mono<Person> getById(final Integer id) {
        return findAll().filter(person -> Objects.equals(person.getId(), id)).next();
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.just(person1, person2, person3, person4);
    }
}