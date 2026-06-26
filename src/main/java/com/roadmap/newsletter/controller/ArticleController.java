package com.roadmap.newsletter.controller;

import com.roadmap.newsletter.model.article.ArticleRequest;
import com.roadmap.newsletter.model.article.ArticleResponse;
import com.roadmap.newsletter.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/api/v1/articles")
@AllArgsConstructor
public class ArticleController {

  private ArticleService articleService;

  @PostMapping
  public ResponseEntity<ArticleResponse> postTopic(@Validated @RequestBody ArticleRequest articleRequest) {
    return ResponseEntity.status(CREATED).body(articleService.addArticle(articleRequest));
  }
  @GetMapping
  public ResponseEntity<ArticleResponse> getTopics() {
    return ResponseEntity.ok(articleService.getArticles());
  }
  @GetMapping("category/{category}")
  public ResponseEntity<ArticleResponse> getTopicsByCategory(@PathVariable(name = "category") String category) {
    return ResponseEntity.status(OK).body(articleService.filterTopicsByCategory(category));
  }
//  @GetMapping("category/{category}")
//  public ResponseEntity<ArticleResponse> getTopicsByCategory(@PathVariable(name = "category") String category) {
//    return ResponseEntity.status(OK).body(articleService.filterTopicsByCategory(category));
//  }
//  @PutMapping
//  public ResponseEntity<ArticleResponse> modifyTopic(@RequestParam(name = "id") Long id, @Validated @RequestBody ArticleRequest articleRequest) {
//    return ResponseEntity.status(OK).body(articleService.modifyTopic(id, topicRequest));
//  }
//  @DeleteMapping("/{id}")
//  public ResponseEntity<ArticleResponse> deleteTopic(@PathVariable(name = "id") Long id) {
//    return ResponseEntity.status(OK).body(articleService.deleteTopic(id));
//  }
}
