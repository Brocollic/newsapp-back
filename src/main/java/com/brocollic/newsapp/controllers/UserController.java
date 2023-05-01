package com.brocollic.newsapp.controllers;

import com.brocollic.newsapp.dtos.requests.UserCreateRequest;
import com.brocollic.newsapp.entities.Role;
import com.brocollic.newsapp.entities.User;
import com.brocollic.newsapp.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<?> all(@AuthenticationPrincipal User currentUser) {
        if(currentUser.getRole().equals(Role.ADMIN)) {
            return ResponseEntity.ok().body(userService.findAll());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/paged")
    public ResponseEntity<?> page(@RequestParam("page") Integer pageNumber,
                                  @RequestParam("size") Integer pageSize,
                                  @AuthenticationPrincipal User currentUser) {
        if(currentUser.getRole().equals(Role.ADMIN)) {
            Pageable page = PageRequest.of(pageNumber, pageSize, Sort.unsorted());
            return ResponseEntity.ok().body(userService.findAll(page));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        if(currentUser.getRole().equals(Role.ADMIN)) {
            User user = userService.findById(id).orElseThrow();
            return ResponseEntity.ok().body(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody UserCreateRequest userCreateRequest,
                                    @AuthenticationPrincipal User currentUser) {
        if(currentUser.getRole().equals(Role.ADMIN)) {
            User newUser = User.builder()
                    .email(userCreateRequest.getEmail())
                    .firstname(userCreateRequest.getFirstname())
                    .lastname(userCreateRequest.getLastname())
                    .password(passwordEncoder.encode(userCreateRequest.getPassword()))
                    .role(Role.valueOf(userCreateRequest.getRole())).build();
            newUser = userService.save(newUser);
            return ResponseEntity.ok().body(newUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping(value = "/{id}",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modify(@Valid @RequestBody UserCreateRequest userCreateRequest,
                                    @PathVariable Long id,
                                    @AuthenticationPrincipal User currentUser) {
        if(currentUser.getRole().equals(Role.ADMIN)) {
            User user = userService.findById(id).orElseThrow();
            user.setEmail(userCreateRequest.getEmail());
            user.setFirstname(userCreateRequest.getFirstname());
            user.setLastname(userCreateRequest.getLastname());
            user.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
            user.setRole(Role.valueOf(userCreateRequest.getRole()));

            user = userService.save(user);
            return ResponseEntity.ok().body(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        if(currentUser.getRole().equals(Role.ADMIN)) {
            User user = userService.findById(id).orElseThrow();
            userService.deleteById(user.getId());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
