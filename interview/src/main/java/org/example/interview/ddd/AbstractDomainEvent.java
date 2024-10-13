package org.example.interview.ddd;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public abstract class AbstractDomainEvent implements DomainEvent {
    private final UUID eventId;
    private final Instant happenedAt;

    public AbstractDomainEvent() {
        this.eventId = UUID.randomUUID();
        this.happenedAt = Instant.now();
    }
}
