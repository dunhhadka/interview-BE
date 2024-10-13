package org.example.interview.ddd;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Transient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@MappedSuperclass
public abstract class DomainEntity<R extends AggregateRoot<R>> {

    @Transient
    private boolean isNew = true;

    @JsonIgnore
    public boolean isNew() {
        return this.isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }
}
