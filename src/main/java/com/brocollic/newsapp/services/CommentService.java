package com.brocollic.newsapp.services;

import com.brocollic.newsapp.entities.Article;
import com.brocollic.newsapp.entities.Comment;
import com.brocollic.newsapp.repositories.common.CommonDaoInterface;
import com.brocollic.newsapp.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService implements CommonDaoInterface<Comment, Long> {

    private final CommentRepository commentRepository;

    public Page<Comment> findCommentsByArticle(Article article, Pageable page) {
        return commentRepository.findCommentsByArticle(article, page);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public <S extends Comment> S save(S comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
