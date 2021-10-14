package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/person")
public class PersonController {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(PersonController.class.getSimpleName());
    private final PersonService service;
    private final BCryptPasswordEncoder encoder;
    private final ObjectMapper mapper;

    public PersonController(PersonService service,
                            BCryptPasswordEncoder encoder,
                            ObjectMapper mapper) {
        this.service = service;
        this.encoder = encoder;
        this.mapper = mapper;
    }

    @GetMapping({"/", "/all"})
    public List<Person> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        Person person = service.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Person with id " + id + " is not found."
        ));

        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        Objects.requireNonNull(person.getName(), "Name mustn't be empty");
        Objects.requireNonNull(person.getLogin(), "Login mustn't be empty");
        Objects.requireNonNull(person.getPassword(), "Password mustn't be empty");

        if (person.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password length must be more than 5 characters.");
        }

        return new ResponseEntity<>(service.save(person), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        Objects.requireNonNull(person.getName(), "Name mustn't be empty");
        Objects.requireNonNull(person.getLogin(), "Login mustn't be empty");
        Objects.requireNonNull(person.getPassword(), "Password mustn't be empty");

        if (person.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password length must be more than 5 characters.");
        }

        service.findById(person.getId()).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Person with id " + person.getId() + " is not found."
        ));

        service.save(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        service.delete(person);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-up")
    public void register(@RequestBody Person person) {
        Objects.requireNonNull(person.getName(), "Name mustn't be empty");
        Objects.requireNonNull(person.getLogin(), "Login mustn't be empty");
        Objects.requireNonNull(person.getPassword(), "Password mustn't be empty");

        person.setPassword(encoder.encode(person.getPassword()));
        service.save(person);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void exceptionHandler(Exception e, HttpServletRequest request,
                                 HttpServletResponse response)
            throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(new HashMap<>() {
            {
                put("message", e.getMessage());
                put("type", e.getClass());
            }
        }));
        LOGGER.error(e.getLocalizedMessage());
    }
}
