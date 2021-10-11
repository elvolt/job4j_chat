package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import ru.job4j.chat.domain.Message;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.domain.Room;
import ru.job4j.chat.repository.MessageRepository;
import ru.job4j.chat.repository.PersonRepository;
import ru.job4j.chat.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final PersonRepository personRepository;

    public MessageService(MessageRepository messageRepository,
                          RoomRepository roomRepository,
                          PersonRepository personRepository) {
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.personRepository = personRepository;
    }

    public List<Message> findAll() {
        return (List<Message>) messageRepository.findAll();
    }

    public Optional<Message> findById(int id) {
        return messageRepository.findById(id);
    }

    public Message save(Message message) {
        Optional<Room> room = roomRepository.findById(message.getRoom().getId());
        message.setRoom(room.orElse(new Room()));
        Optional<Person> person = personRepository.findById(message.getPerson().getId());
        message.setPerson(person.orElse(new Person()));
        return messageRepository.save(message);
    }

    public void delete(Message message) {
        messageRepository.delete(message);
    }
}
