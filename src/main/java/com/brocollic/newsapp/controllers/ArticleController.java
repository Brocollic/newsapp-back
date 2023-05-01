package com.brocollic.newsapp.controllers;

import com.brocollic.newsapp.dtos.requests.ArticleRequest;
import com.brocollic.newsapp.entities.Article;
import com.brocollic.newsapp.entities.Category;
import com.brocollic.newsapp.entities.Role;
import com.brocollic.newsapp.entities.User;
import com.brocollic.newsapp.services.ArticleService;
import com.brocollic.newsapp.utils.ArticleSpecification;
import com.brocollic.newsapp.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final ArticleSpecification articleSpecification;

    @GetMapping()
    public ResponseEntity<?> all(@RequestParam("page") Integer pageNumber, @RequestParam("size") Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize, Sort.unsorted());
        return ResponseEntity.ok().body(articleService.findAll(page));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> allByCategory(@PathVariable Long categoryId,
                                           @RequestParam("page") Integer pageNumber,
                                           @RequestParam("size") Integer pageSize) {
        Category category = categoryService.findById(categoryId).orElseThrow();
        Pageable page = PageRequest.of(pageNumber, pageSize, Sort.unsorted());
        return ResponseEntity.ok().body(articleService.findArticlesByCategory(category, page));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchArticles(@RequestParam(value = "categoryId", required = false) Long categoryId,
                                            @RequestParam(value = "search", required = false) String search,
                                            @RequestParam("page") Integer pageNumber,
                                            @RequestParam("size") Integer pageSize) {
        Category category = categoryService.findById(categoryId).orElseThrow();
        Specification<Article> specification = articleSpecification.getArticleSpecification(category, search);
        Pageable page = PageRequest.of(pageNumber, pageSize, Sort.unsorted());
        return ResponseEntity.ok().body(articleService.searchArticles(specification, page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        Article article = articleService.findById(id).orElseThrow();
        return ResponseEntity.ok().body(article);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody ArticleRequest articleRequest,
                                    @AuthenticationPrincipal User currentUser) {
        if(currentUser.getRole().equals(Role.ADMIN) || currentUser.getRole().equals(Role.CONTENT_CREATOR)) {
            Category category = categoryService.findById(articleRequest.getCategoryId()).orElseThrow();
            Article newArticle = Article.builder()
                    .title(articleRequest.getTitle())
                    .content(articleRequest.getContent())
                    .creationTime(LocalDateTime.now())
                    .category(category)
                    .user(currentUser)
                    .build();
            newArticle = articleService.save(newArticle);
            return ResponseEntity.ok().body(newArticle);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping(value = "/{id}",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modify(@Valid @RequestBody ArticleRequest articleRequest,
                                    @PathVariable Long id,
                                    @AuthenticationPrincipal User currentUser) {
        if(currentUser.getRole().equals(Role.ADMIN) || currentUser.getRole().equals(Role.CONTENT_CREATOR)) {
            Category category =  categoryService.findById(articleRequest.getCategoryId()).orElseThrow();
            Article article = articleService.findById(id).orElseThrow();
            article.setTitle(articleRequest.getTitle());
            article.setContent(articleRequest.getContent());
            article.setCategory(category);
            return ResponseEntity.ok().body(article);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        if(currentUser.getRole().equals(Role.ADMIN) || currentUser.getRole().equals(Role.CONTENT_CREATOR)) {
            Article article = articleService.findById(id).orElseThrow();
            articleService.deleteById(article.getId());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
