package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.domain.Role;
import ru.job4j.chat.repository.PersonRepository;
import ru.job4j.chat.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    public PersonService(PersonRepository personRepository, RoleRepository roleRepository) {
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
    }

    public List<Person> findAll() {
        return (List<Person>) personRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    public Person save(Person person) {
        Optional<Role> role = roleRepository.findById(person.getRole().getId());
        person.setRole(role.orElse(new Role()));
        return personRepository.save(person);
    }

    public void delete(Person person) {
        personRepository.delete(person);
    }
}
