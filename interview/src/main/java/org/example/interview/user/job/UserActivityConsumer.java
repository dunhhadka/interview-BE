package org.example.interview.user.job;

import lombok.RequiredArgsConstructor;
import org.example.interview.event.EventService;
import org.example.interview.user.application.utils.ChangeLogUtils;
import org.example.interview.user.application.utils.JsonUtils;
import org.example.interview.user.domain.user.event.*;
import org.example.interview.user.domain.user.logs.AppEventType;
import org.example.interview.user.domain.user.logs.UserLog;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserActivityConsumer {

    private final EventService eventService;

    public void listen(UserLog message) {
        UserLog userLog = message;

        handleUserEvent(userLog);

        handleUserUpdateProperty(userLog);
    }

    private void handleUserUpdateProperty(UserLog userLog) {
        if (userLog.getData() == null || userLog.getVerb() != AppEventType.update) return;
        var domainEvents = JsonUtils.unmarshal(userLog.getData(), DomainKafkaModelEvent.class);

        var node = ChangeLogUtils.filteredStream(domainEvents.getJsonNodeEvents(), PropertyChangedEvent.class);

        var event = JsonUtils.unmarshal(node, PropertyChangedEvent.class);
        if (event == null) return;

        var eventBuilder = new PropertyChangeEventBuilder(
                "update",
                domainEvents,
                event.getAuthor(),
                List.of(),
                JsonUtils.marshal(event.getChanges()),
                event.getAuthorId()
        );

        eventService.create(eventBuilder.buildEvent());
    }

    private void handleUserEvent(UserLog userLog) {
        if (userLog.getData() == null) return;
        var domainEvents = JsonUtils.unmarshal(userLog.getData(), DomainKafkaModelEvent.class);
        switch (userLog.getVerb()) {
            case add -> handleUserCreatedEvent(domainEvents);
            case update -> handlerUserUpdateEvent(domainEvents);
            case delete -> handleUserDeletedEvent(domainEvents);
        }
    }

    private void handleUserDeletedEvent(DomainKafkaModelEvent domainEvents) {
        var node = ChangeLogUtils.filteredStream(domainEvents.getJsonNodeEvents(), UserDeletedEvent.class);
        var event = JsonUtils.unmarshal(node, UserDeletedEvent.class);
        if (event == null) return;

        var eventBuilder = new UserDeletedEventBuilder(event.getDeletedBy(), event.getUserDeletedName(), domainEvents, event.getAuthorId());

        eventService.create(eventBuilder.buildEvent());
    }

    private void handlerUserUpdateEvent(DomainKafkaModelEvent domainEvents) {
        var node = ChangeLogUtils.filteredStream(domainEvents.getJsonNodeEvents(), UserActivatedEvent.class);

        var event = JsonUtils.unmarshal(node, UserActivatedEvent.class);
        if (event == null) return;

        var eventBuilder = new UserActivatedEventBuilder(event.getActivatedBy(), event.getUserActivated(), domainEvents, event.getAuthorId());

        eventService.create(eventBuilder.buildEvent());
    }

    private void handleUserCreatedEvent(DomainKafkaModelEvent domainEvents) {
        var node = ChangeLogUtils.filteredStream(domainEvents.getJsonNodeEvents(), UserCreatedEvent.class);

        var event = JsonUtils.unmarshal(node, UserCreatedEvent.class);

        var eventBuilder = new UserCreatedEventBuilder(event, domainEvents,
                Optional.ofNullable(event.getCreatedById()).map(UUID::toString).orElse(null));

        eventService.create(eventBuilder.buildEvent());
    }
}
