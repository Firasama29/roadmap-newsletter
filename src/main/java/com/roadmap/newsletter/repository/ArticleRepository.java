package com.roadmap.newsletter.repository;

import com.roadmap.newsletter.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findByTitle(String title);

    List<Article> findByTopicCategory(String category);

    @Query("select a from Article a \n" +
      "Join a.topic t \n" +
      "Join t.subscriptions s \n" +
      " where t.name in :topics \n" +
      " and s.day = :currentDay")
    List<Article> findByTopicNameAndDate(@Param("topics") List<String> topics, @Param("currentDay") String currentDay);
}
