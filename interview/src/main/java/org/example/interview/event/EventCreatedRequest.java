package org.example.interview.event;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventCreatedRequest {
    private String verb;
    private String author;
    private String description;
    private String keywords;
    private String additional;
    private String authorId;
}
