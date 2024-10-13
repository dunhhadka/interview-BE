package org.example.interview.user.domain.user.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.interview.ddd.AbstractDomainEvent;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyChangedEvent extends AbstractDomainEvent {

    private String author;
    private String authorId;
    private List<ObjectPropertyChange> changes = new ArrayList<>();

    @Override
    public String getEventName() {
        return PropertyChangedEvent.class.getSimpleName();
    }
}
