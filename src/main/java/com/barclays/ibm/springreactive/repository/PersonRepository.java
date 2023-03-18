package com.barclays.ibm.springreactive.repository;

import com.barclays.ibm.springreactive.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonRepository {

    Mono<Person> getById(Integer id);
    Flux<Person> findAll();
}
