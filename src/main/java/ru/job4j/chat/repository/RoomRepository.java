package ru.job4j.chat.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.chat.domain.Room;

import java.util.Optional;

public interface RoomRepository extends CrudRepository<Room, Integer> {
    @Override
    @Query("select r from Room r left join fetch r.messages where r.id = :paramId")
    Optional<Room> findById(@Param("paramId") Integer integer);
}
