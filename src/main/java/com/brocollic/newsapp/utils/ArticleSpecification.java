package com.brocollic.newsapp.utils;

import com.brocollic.newsapp.entities.Article;
import com.brocollic.newsapp.entities.Category;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ArticleSpecification {
    public Specification<Article> getArticleSpecification(Category category, String search){
        return (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if(category != null) {
                predicateList.add(cb.equal(root.get("category"), category));
            }
            if(search != null) {
                predicateList.add(
                        cb.or(
                            cb.like( cb.lower(root.get("title")), '%' + search.toLowerCase() + '%'),
                            cb.like( cb.lower(root.get("content")), '%' + search.toLowerCase() + '%')
                            )
                );
            }

            return cb.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
