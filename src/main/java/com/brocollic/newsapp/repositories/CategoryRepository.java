package com.brocollic.newsapp.repositories;

import com.brocollic.newsapp.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
