package org.example.interview.user.domain.user.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.interview.ddd.AbstractDomainEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class UserCreatedEvent extends AbstractDomainEvent {
    private final String createdBy;
    private final UUID createdById;
    private final UUID userCreatedId;
    private final String userCreatedName;

    @Override
    public String getEventName() {
        return UserCreatedEvent.class.getSimpleName();
    }
}
