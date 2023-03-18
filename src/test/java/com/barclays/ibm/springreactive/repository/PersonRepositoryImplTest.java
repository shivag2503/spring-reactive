package com.barclays.ibm.springreactive.repository;

import com.barclays.ibm.springreactive.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PersonRepositoryImplTest {

    PersonRepository personRepository = new PersonRepositoryImpl();

    @Test
    void testFindById() {
        Mono<Person> personMono = personRepository.getById(3);

        assertTrue(personMono.hasElement().block());
    }

    @Test
    void testFindByIdStepVerifier() {
        Mono<Person> personMono = personRepository.getById(3);

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();

        personMono.subscribe(person -> {
            System.out.println(person.getFirstName());
        });
    }

    @Test
    void testFindByIdNotFound() {
        Mono<Person> personMono = personRepository.getById(13);

        assertFalse(personMono.hasElement().block());
    }

    @Test
    void testFindByIdNotFoundStepVerifier() {
        Mono<Person> personMono = personRepository.getById(13);

        StepVerifier.create(personMono).expectNextCount(0).verifyComplete();

        personMono.subscribe(person -> {
            System.out.println(person.getFirstName());
        });
    }

    @Test
    void testMonoByIdBlock() {
        Mono<Person> personMono = personRepository.getById(1);
        Person person = personMono.block();
        assert person != null;
        System.out.println(person.toString());
    }

    @Test
    void testMonoByIdSubscriber() {
        Mono<Person> personMono = personRepository.getById(1);
        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testMapOperation() {
        Mono<Person> personMono = personRepository.getById(1);
        personMono.map(Person::getFirstName).subscribe(System.out::println);
    }

    @Test
    void testFluxBlockFirst() {
        Flux<Person> personFlux = personRepository.findAll();
        Person person = personFlux.blockFirst();
        assert person != null;
        System.out.println(person.toString());
    }

    @Test
    void testFluxSubscriber() {
        Flux<Person> personFlux = personRepository.findAll();
        personFlux.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testFluxMapOperation() {
        Flux<Person> personFlux = personRepository.findAll();
        personFlux.map(Person::getFirstName).subscribe(System.out::println);
    }

    @Test
    void testFluxToList() {
        Flux<Person> personFlux = personRepository.findAll();
        Mono<List<Person>> listMono = personFlux.collectList();
        listMono.subscribe(people -> {
            people.forEach(person -> System.out.println(person.getFirstName()));
        });
    }

    @Test
    void testFilterOnName() {
        personRepository.findAll()
                .filter(person -> person.getFirstName().equals("Jack"))
                .subscribe(person -> System.out.println(person.getLastName()));
    }

    @Test
    void testGetById() {
        Mono<Person> jackMono = personRepository.findAll().filter(person -> person.getFirstName().equals("Jack"))
                .next();

        jackMono.subscribe(person -> System.out.println(person.getFirstName()));
    }

    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 0;

        Mono<Person> personMono = personFlux.filter(person -> Objects.equals(person.getId(), id)).single()
                .doOnError(throwable -> {
                    System.out.println("Error occurred in flux");
                    System.out.println(throwable.toString());
                });

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        }, throwable -> {
            System.out.println("Error occurred in mono");
            System.out.println(throwable.toString());
        });
    }
}