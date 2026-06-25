package com.roadmap.newsletter.repository;

import com.roadmap.newsletter.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Optional<Topic> findByName(String topic);

    Optional<Topic> findByCategoryContaining(String category);
}
