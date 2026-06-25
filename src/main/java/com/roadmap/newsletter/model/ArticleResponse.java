package com.roadmap.newsletter.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticleResponse {

    private String message;

    private List<ArticleData> data;
}
