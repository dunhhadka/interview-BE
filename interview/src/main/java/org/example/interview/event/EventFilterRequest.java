package org.example.interview.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventFilterRequest {
    private String query;
    private String authorId;
}
