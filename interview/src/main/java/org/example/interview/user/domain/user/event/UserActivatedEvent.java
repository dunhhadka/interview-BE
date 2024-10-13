package org.example.interview.user.domain.user.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.interview.ddd.AbstractDomainEvent;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserActivatedEvent extends AbstractDomainEvent {
    private String activatedBy;
    private String userActivated;
    private String authorId;

    @Override
    public String getEventName() {
        return UserActivatedEvent.class.getSimpleName();
    }
}
