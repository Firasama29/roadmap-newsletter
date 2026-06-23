package com.roadmap.newsletter.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TopicResponse {

  private String message;

  private List<TopicData> data;
}
