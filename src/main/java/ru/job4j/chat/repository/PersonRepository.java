package ru.job4j.chat.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.chat.domain.Person;

import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person, Integer> {
    @Override
    @Query("select p from Person p join fetch p.role")
    Iterable<Person> findAll();

    @Override
    @Query("select p from Person p join fetch p.role where p.id = :paramId")
    Optional<Person> findById(@Param("paramId") Integer integer);

    @Query("select p from Person p join fetch p.role where p.login = :paramLogin")
    Optional<Person> findByLogin(@Param("paramLogin") String login);
}
