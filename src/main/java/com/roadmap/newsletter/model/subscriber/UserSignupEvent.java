package com.roadmap.newsletter.model.subscriber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSignupEvent {
    private String email;
}
