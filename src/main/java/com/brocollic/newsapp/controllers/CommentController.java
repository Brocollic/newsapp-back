package com.brocollic.newsapp.controllers;

import com.brocollic.newsapp.entities.*;
import com.brocollic.newsapp.services.ArticleService;
import com.brocollic.newsapp.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final ArticleService articleService;

    @GetMapping("/article/{articleId}")
    public ResponseEntity<?> allByArticle(@PathVariable Long articleId,
                                          @RequestParam("page") Integer pageNumber,
                                          @RequestParam("size") Integer pageSize) {
        Article article = articleService.findById(articleId).orElseThrow();
        Pageable page =  PageRequest.of(pageNumber, pageSize, Sort.unsorted());
        return ResponseEntity.ok().body(commentService.findCommentsByArticle(article, page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        Comment comment = commentService.findById(id).orElseThrow();
        return ResponseEntity.ok().body(comment);
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody Comment commentRequest,
                                    @RequestParam Long articleId,
                                    @AuthenticationPrincipal User currentUser) {
        Article article = articleService.findById(articleId).orElseThrow();
        Comment newComment = Comment.builder()
                .content(commentRequest.getContent())
                .creationTime(LocalDateTime.now())
                .user(currentUser)
                .article(article)
                .build();
        newComment = commentService.save(newComment);
        return ResponseEntity.ok().body(newComment);
    }

    @PutMapping(value = "/{id}",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modify(@Valid @RequestBody Comment commentRequest,
                                    @PathVariable Long id,
                                    @AuthenticationPrincipal User currentUser) {
        Comment comment = commentService.findById(id).orElseThrow();
        if (currentUser.getRole().equals(Role.ADMIN) || comment.getUser().getId().equals(currentUser.getId())) {
            comment.setContent(commentRequest.getContent());
            comment = commentService.save(comment);
            return ResponseEntity.ok(comment);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        Comment comment = commentService.findById(id).orElseThrow();
        if(currentUser.getRole().equals(Role.ADMIN) || comment.getUser().getId().equals(currentUser.getId())) {
            commentService.deleteById(comment.getId());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
