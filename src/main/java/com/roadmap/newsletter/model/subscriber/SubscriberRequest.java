package com.roadmap.newsletter.model.subscriber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SubscriberRequest {

    private String name;

    private String email;

    private List<String> topics;

    private String category;
}
