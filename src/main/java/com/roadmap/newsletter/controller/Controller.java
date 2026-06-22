package com.roadmap.newsletter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/test")
public class Controller {

  @GetMapping
  public String getTestMessage() {
    return "API works fine";
  }
}
