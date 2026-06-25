package com.roadmap.newsletter.repository;

import com.roadmap.newsletter.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Optional<Topic> findByNameContaining(String name);

    Optional<Topic> findByCategoryContaining(String category);
}
