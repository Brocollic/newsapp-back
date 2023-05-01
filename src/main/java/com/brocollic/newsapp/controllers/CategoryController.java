package com.brocollic.newsapp.controllers;

import com.brocollic.newsapp.entities.Category;
import com.brocollic.newsapp.entities.Role;
import com.brocollic.newsapp.entities.User;
import com.brocollic.newsapp.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> all() {
        return ResponseEntity.ok().body(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        Category category = categoryService.findById(id).orElseThrow();
        return ResponseEntity.ok().body(category);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody Category category,
                                    @AuthenticationPrincipal User currentUser) {
        if(currentUser.getRole().equals(Role.ADMIN)) {
            return ResponseEntity.ok().body(categoryService.save(category));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modify(@Valid @RequestBody Category newCategory,
                                    @PathVariable Long id,
                                    @AuthenticationPrincipal User currentUser) {
        if(currentUser.getRole().equals(Role.ADMIN)) {
            Category category = categoryService.findById(id).orElseThrow();
            category.setLabel(newCategory.getLabel());
            category.setDescription(newCategory.getDescription());
            return ResponseEntity.ok().body(categoryService.save(category));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        if(currentUser.getRole().equals(Role.ADMIN)) {
            Category category = categoryService.findById(id).orElseThrow();
            categoryService.deleteById(category.getId());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
