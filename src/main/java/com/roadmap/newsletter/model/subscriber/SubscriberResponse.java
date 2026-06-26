package com.roadmap.newsletter.model.subscriber;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubscriberResponse {

    private String message;

    List<String> topics;
}
