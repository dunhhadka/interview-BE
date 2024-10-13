package org.example.interview.ddd;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@MappedSuperclass
public abstract class AggregateRoot<R extends AggregateRoot<R>> extends DomainEntity<R> {

    @Id
    @Getter
    public UUID id;

    public AggregateRoot(UUID id) {
        this.id = id;
    }

    @Transient
    protected List<DomainEvent> domainEvents = new ArrayList<>();

    @JsonProperty("events")
    public List<DomainEvent> getDomainEvents() {
        if (this.domainEvents == null) return Collections.emptyList();
        return Collections.unmodifiableList(this.domainEvents);
    }

    protected void addDomainEvent(DomainEvent event) {
        if (domainEvents == null) domainEvents = new ArrayList<>();
        domainEvents.add(event);
    }
}
