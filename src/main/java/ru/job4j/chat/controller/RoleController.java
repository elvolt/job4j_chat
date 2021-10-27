package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.domain.Role;
import ru.job4j.chat.service.RoleService;
import ru.job4j.chat.util.PatchService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Role> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> findById(@PathVariable int id) {
        Role role = service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Role with id " + id + " is not found."
                ));

        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Role> create(@RequestBody Role role) {
        Objects.requireNonNull(role.getAuthority(), "Authority mustn't be empty");

        return new ResponseEntity<>(service.save(role), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Role role) {
        Objects.requireNonNull(role.getAuthority(), "Authority mustn't be empty");

        service.findById(role.getId()).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Role with id " + role.getId() + " is not found."
        ));

        service.save(role);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/")
    public ResponseEntity<Void> patch(@RequestBody Role role)
            throws InvocationTargetException, IllegalAccessException {
        Role existingRole = service.findById(role.getId()).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Role with id " + role.getId() + " is not found."
                ));

        Role patch = (Role) new PatchService<>().getPatch(existingRole, role);
        service.save(patch);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Role role = new Role();
        role.setId(id);
        service.delete(role);
        return ResponseEntity.ok().build();
    }
}
