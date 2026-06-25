package com.roadmap.newsletter.repository;

import com.roadmap.newsletter.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findByTitleContaining(String title);

    List<Article> findByTopicCategory(String category);
}
