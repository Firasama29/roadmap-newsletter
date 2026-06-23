package com.roadmap.newsletter.repository;

import com.roadmap.newsletter.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findByNameContaining(String name);

    List<Topic> findByCategoryContaining(String category);
}
