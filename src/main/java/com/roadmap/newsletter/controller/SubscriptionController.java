package com.roadmap.newsletter.controller;

import com.roadmap.newsletter.service.NewsletterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/api/v1/newsletter")
@AllArgsConstructor
public class SubscriptionController {

    private NewsletterService newsletterService;

    @PostMapping
    public ResponseEntity<String> sendEmail(@RequestParam(name = "to") String to,
                                            @RequestParam(name = "subject") String subject,
                                            @RequestParam(name = "body") String body) {
        return ResponseEntity.status(OK).body(newsletterService.sendNewsletter(to, subject, body));
    }
}
