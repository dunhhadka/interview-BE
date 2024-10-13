package org.example.interview.event;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class EventResponse {
    private int id;

    private String author;

    private String verb;

    private String description;

    private String keyword;

    private Instant happenedAt;

    private String authorId;

    private String additional;
}
