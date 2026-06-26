package com.roadmap.newsletter.model.topic;

import com.roadmap.newsletter.model.article.Articles;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TopicData {
  private String name;

  private String category;

  private List<Articles> articles;
}
