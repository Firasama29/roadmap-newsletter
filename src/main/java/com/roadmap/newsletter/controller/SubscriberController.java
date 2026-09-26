package com.roadmap.newsletter.controller;

import com.roadmap.newsletter.model.subscriber.SubscriberRequest;
import com.roadmap.newsletter.model.subscriber.SubscriberResponse;
import com.roadmap.newsletter.service.SubscriberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/v1/subscribe")
@AllArgsConstructor
public class SubscriberController {

    private SubscriberService subscriberService;

    @PostMapping
    public ResponseEntity<SubscriberResponse> postSubscribe(@Valid @RequestBody SubscriberRequest subscriberRequest) {
        return ResponseEntity.status(OK).body(subscriberService.subscribe(subscriberRequest));
    }

    @DeleteMapping
    public ResponseEntity<SubscriberResponse> deleteSubscriber(@RequestParam(name = "email") String email, @RequestParam(name = "topics") String topics) {
        return ResponseEntity.status(OK).body(subscriberService.deleteSubscriptionByTopic(email, topics));
    }
}
