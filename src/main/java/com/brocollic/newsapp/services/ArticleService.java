package com.brocollic.newsapp.services;

import com.brocollic.newsapp.entities.Article;
import com.brocollic.newsapp.entities.Category;
import com.brocollic.newsapp.repositories.common.CommonDaoInterface;
import com.brocollic.newsapp.repositories.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService implements CommonDaoInterface<Article, Long> {

    private final ArticleRepository articleRepository;

    public Page<Article> findAll(Pageable page) {
        return articleRepository.findAll(page);
    }

    public Page<Article> findArticlesByCategory(Category category, Pageable page) {
        return articleRepository.findArticlesByCategory(category, page);
    }

    public Page<Article> searchArticles(Specification<Article> specification, Pageable page) {
        return articleRepository.findAll(specification, page);
    }
    @Override
    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    @Override
    public <S extends Article> S save(S article) {
        return articleRepository.save(article);
    }

    @Override
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

}
