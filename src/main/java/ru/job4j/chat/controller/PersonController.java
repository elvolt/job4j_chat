package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.service.PersonService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService service;
    private final BCryptPasswordEncoder encoder;

    public PersonController(PersonService service, BCryptPasswordEncoder encoder) {
        this.service = service;
        this.encoder = encoder;
    }

    @GetMapping({"/", "/all"})
    public List<Person> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        Optional<Person> person = service.findById(id);
        return new ResponseEntity<>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        return new ResponseEntity<>(
                service.save(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        Optional<Person> existingPerson = service.findById(person.getId());
        if (existingPerson.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
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
        person.setPassword(encoder.encode(person.getPassword()));
        service.save(person);
    }
}
