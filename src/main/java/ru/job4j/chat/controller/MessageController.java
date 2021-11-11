package ru.job4j.chat.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.domain.Message;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.domain.Room;
import ru.job4j.chat.dto.MessageDto;
import ru.job4j.chat.service.MessageService;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoomService;
import ru.job4j.chat.util.PatchService;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;
    private final RoomService roomService;
    private final PersonService personService;
    private final ModelMapper modelMapper;

    public MessageController(MessageService messageService, RoomService roomService,
                             PersonService personService, ModelMapper modelMapper) {
        this.messageService = messageService;
        this.roomService = roomService;
        this.personService = personService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public List<MessageDto> findAll() {
        return messageService.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageDto> findById(@PathVariable int id) {
        Message message = messageService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Message with id " + id + " is not found."
                ));
        return new ResponseEntity<>(convertToDto(message), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<MessageDto> create(@Valid @RequestBody MessageDto messageDto) {
        Message message = convertToEntity(messageDto);
        message.setCreated(LocalDateTime.now());

        return new ResponseEntity<>(convertToDto(messageService.save(message)), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@Valid @RequestBody MessageDto messageDto) {
        Message existingMessage = messageService.findById(messageDto.getId()).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Message with id " + messageDto.getId() + " is not found."
                ));

        Message message = convertToEntity(messageDto);
        message.setCreated(existingMessage.getCreated());

        messageService.save(message);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/")
    public ResponseEntity<Void> patch(@RequestBody MessageDto messageDto)
            throws InvocationTargetException, IllegalAccessException {
        Message existingMessage = messageService.findById(messageDto.getId()).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Message with id " + messageDto.getId() + " is not found."
                ));
        Message message = convertToEntity(messageDto);
        Message patch = (Message) new PatchService<>().getPatch(existingMessage, message);
        messageService.save(patch);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Message message = new Message();
        message.setId(id);
        messageService.delete(message);
        return ResponseEntity.ok().build();
    }

    private MessageDto convertToDto(Message message) {
        return modelMapper.map(message, MessageDto.class);
    }

    private Message convertToEntity(MessageDto messageDto) {
        Message message = modelMapper.map(messageDto, Message.class);

        if (messageDto.getPersonId() == 0) {
            message.setPerson(null);
        } else {
            Person person = personService.findById(messageDto.getPersonId()).orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Person with id " + messageDto.getPersonId() + " is not found."
                    ));
            message.setPerson(person);
        }

        if (messageDto.getRoomId() == 0) {
            message.setRoom(null);
        } else {
            Room room = roomService.findById(messageDto.getRoomId()).orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Room with id " + messageDto.getRoomId() + " is not found."
                    ));
            message.setRoom(room);
        }

        return message;
    }
}
