package com.roadmap.newsletter.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class ArticleRequest {

  private int id;

  @NotBlank(message = "Title is required")
  private String title;

  private String excerpt;

  @NotBlank(message = "Topic is required")
  private String topic;

  @NotBlank(message = "Category is required")
  private String category;

  @NotBlank(message = "source name is required")
  private String source;

  @NotBlank(message = "URL is required")
  @URL(message = "Invalid URL")
  private String link;
}
