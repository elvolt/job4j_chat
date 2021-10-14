package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.domain.Message;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.domain.Room;
import ru.job4j.chat.service.MessageService;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoomService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;
    private final RoomService roomService;
    private final PersonService personService;

    public MessageController(MessageService messageService, RoomService roomService,
                             PersonService personService) {
        this.messageService = messageService;
        this.roomService = roomService;
        this.personService = personService;
    }

    @GetMapping("/")
    public List<Message> findAll() {
        return messageService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable int id) {
        Message message = messageService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Message with id " + id + " is not found."
                ));
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message message) {
        Objects.requireNonNull(message.getText(), "Text mustn't be empty");
        Objects.requireNonNull(message.getRoom(), "Room mustn't be empty");
        Objects.requireNonNull(message.getPerson(), "Person mustn't be empty");

        Person person = personService.findById(message.getPerson().getId()).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Person with id " + message.getPerson().getId() + " is not found."
                ));
        Room room = roomService.findById(message.getRoom().getId()).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Room with id " + message.getRoom().getId() + " is not found."
                ));
        message.setPerson(person);
        message.setRoom(room);
        message.setCreated(LocalDateTime.now());

        return new ResponseEntity<>(messageService.save(message), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Message message) {
        Objects.requireNonNull(message.getText(), "Text mustn't be empty");
        Objects.requireNonNull(message.getRoom(), "Room mustn't be empty");
        Objects.requireNonNull(message.getPerson(), "Person mustn't be empty");

        Message existingMessage = messageService.findById(message.getId()).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Message with id " + message.getId() + " is not found."
                ));
        Person person = personService.findById(message.getPerson().getId()).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Person with id " + message.getPerson().getId() + " is not found."
                ));
        Room room = roomService.findById(message.getRoom().getId()).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Room with id " + message.getRoom().getId() + " is not found."
                ));
        message.setPerson(person);
        message.setRoom(room);
        message.setCreated(existingMessage.getCreated());

        messageService.save(message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Message message = new Message();
        message.setId(id);
        messageService.delete(message);
        return ResponseEntity.ok().build();
    }
}
