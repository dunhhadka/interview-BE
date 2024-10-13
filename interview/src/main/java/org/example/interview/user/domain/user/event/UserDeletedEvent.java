package org.example.interview.user.domain.user.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.interview.ddd.AbstractDomainEvent;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDeletedEvent extends AbstractDomainEvent {

    private String deletedBy;
    private String userDeletedName;
    private String authorId;

    @Override
    public String getEventName() {
        return UserDeletedEvent.class.getSimpleName();
    }
}
