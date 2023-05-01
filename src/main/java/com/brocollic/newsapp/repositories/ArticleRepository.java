package com.brocollic.newsapp.repositories;

import com.brocollic.newsapp.entities.Article;
import com.brocollic.newsapp.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {
    Page<Article> findArticlesByCategory(Category category, Pageable page);
}
