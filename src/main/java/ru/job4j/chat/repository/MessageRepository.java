package ru.job4j.chat.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.chat.domain.Message;

import java.util.Optional;

public interface MessageRepository extends CrudRepository<Message, Integer> {
    @Override
    @Query("select m from Message m join fetch m.person join fetch m.room")
    Iterable<Message> findAll();

    @Override
    @Query("select m from Message m join fetch m.person join fetch m.room where m.id = :paramId")
    Optional<Message> findById(@Param("paramId") Integer integer);
}
