package com.roadmap.newsletter.model.topic;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicRequest {

  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Category is required")
  private String category;
}
