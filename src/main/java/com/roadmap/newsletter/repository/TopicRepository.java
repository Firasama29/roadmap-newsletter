package com.roadmap.newsletter.repository;

import com.roadmap.newsletter.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
