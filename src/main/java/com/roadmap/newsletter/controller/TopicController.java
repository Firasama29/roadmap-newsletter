package com.roadmap.newsletter.controller;

import com.roadmap.newsletter.model.TopicRequest;
import com.roadmap.newsletter.model.TopicResponse;
import com.roadmap.newsletter.service.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/api/v1/topics")
@AllArgsConstructor
public class TopicController {

  private TopicService topicService;

  @PostMapping
  public ResponseEntity<TopicResponse> postTopic(@Validated @RequestBody TopicRequest topicRequest) {
    return ResponseEntity.status(CREATED).body(topicService.addTopic(topicRequest));
  }
  @GetMapping
  public ResponseEntity<TopicResponse> getTopics() {
    return ResponseEntity.ok(topicService.getTopics());
  }
  @GetMapping("name/{name}")
  public ResponseEntity<TopicResponse> getTopicsByName(@PathVariable(name = "name") String name) {
    return ResponseEntity.status(OK).body(topicService.filterTopicsByName(name));
  }
  @GetMapping("category/{category}")
  public ResponseEntity<TopicResponse> getTopicsByCategory(@PathVariable(name = "category") String category) {
    return ResponseEntity.status(OK).body(topicService.filterTopicsByCategory(category));
  }
  @PutMapping
  public ResponseEntity<TopicResponse> modifyTopic(@RequestParam(name = "id") Long id, @Validated @RequestBody TopicRequest topicRequest) {
    return ResponseEntity.status(OK).body(topicService.modifyTopic(id, topicRequest));
  }
  @DeleteMapping("/{id}")
  public ResponseEntity<TopicResponse> deleteTopic(@PathVariable(name = "id") Long id) {
    return ResponseEntity.status(OK).body(topicService.deleteTopic(id));
  }
}
