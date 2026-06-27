package com.roadmap.newsletter.repository;

import com.roadmap.newsletter.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
