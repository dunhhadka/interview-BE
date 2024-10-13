package org.example.interview.user.domain.user.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.interview.ddd.AggregateRoot;
import org.example.interview.user.application.utils.AbstractEnumConverter;
import org.example.interview.user.application.utils.CustomValueEnum;
import org.example.interview.user.domain.user.event.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends AggregateRoot<User> { //must init id for entity

    @Embedded
    @JsonUnwrapped
    private UserGeneralInfo generalInfo;

    @NotNull
    private String password;

    @NotNull
    @Convert(converter = Role.ValueConverter.class)
    private Role role; // mới chỉ support cho 1 role => nhiều role để sau

    private Instant joinDate;

    @NotNull
    private Instant createdOn;

    private Instant modifiedOn;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Enumerated(value = EnumType.STRING)
    private Language language;

    // optional
    private boolean isNotification;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Type type;

    @Size(max = 100)
    @Column(name = "[national]")
    private String national;

    private UUID createdByUserId;

    @Version
    private Integer version;

    @Transient
    private boolean isMarkDelete;

    @Lob
    private String token;

    public User(
            User author,
            UserGeneralInfo generalInfo,
            String password,
            Role role,
            Language language,
            Type type,
            String national
    ) {
        super(UUID.randomUUID());

        this.generalInfo = generalInfo;

        this.password = password;

        this.role = role;
        this.language = language;
        this.type = type;
        this.national = national;

        this.createdOn = Instant.now();
        this.modifiedOn = Instant.now();

        this.status = Status.waiting;

        if (author != null) {
            this.createdByUserId = author.getId();
            this.addEventCreated(author);
        } else {
            this.addEventCreated(null);
        }
    }

    public User() {
        super(UUID.randomUUID());
    }

    public UUID getId() {
        return this.id;
    }

    private void addEventCreated(User author) {
        this.addDomainEvent(new UserCreatedEvent(author != null ? author.getGeneralInfo().getFullName() :  null,
                this.createdByUserId, this.getId(), this.generalInfo.getFullName()));
    }

    public void active(User author) {
        if (this.status == Status.activated) return;
        this.status = Status.activated;
        this.joinDate = Instant.now();

        this.addDomainEvent(new UserActivatedEvent(author.getGeneralInfo().getFullName(),
                this.getGeneralInfo().getFullName(), author.getId().toString()));
    }

    public void markDelete(User author) {
        if (this.status == Status.deleted) return;
        this.status = Status.deleted;
        this.isMarkDelete = true;

        this.addDomainEvent(new UserDeletedEvent(author.getGeneralInfo().getFullName(),
                this.getGeneralInfo().getFullName(), author.getId().toString()));
    }

    public void setGeneralInfo(UserGeneralInfo generalInfo, User author) {
        if (generalInfo == null || generalInfo.sameAs(this.generalInfo)) return;

        this.addDomainEvent(new PropertyChangedEvent(author.getGeneralInfo().getFullName(), author.getId().toString(),
                this.getGeneralInfo().getDiffs(generalInfo).stream().map(ObjectPropertyChange::new).toList()));

        this.generalInfo = generalInfo;
        this.modifiedOn = Instant.now();
    }

    public UserGeneralInfo getGeneralInfo() {
        if (this.generalInfo == null) return new UserGeneralInfo();
        return this.generalInfo;
    }

    // region enum
    public enum Type {
        normal, premium
    }

    public enum Language {
        english, viet_nam
    }

    public enum Status {
        activated, waiting, deleted
    }

    public enum Role implements CustomValueEnum<String> {
        super_admin("super_admin", "Super admin"),
        trainer("trainer", "Trainer"),
        member("member", "Member");

        private String field;
        private String name;

        Role(String field, String name) {
            this.field = field;
            this.name = name;
        }

        public static class ValueConverter
                extends AbstractEnumConverter<Role>
                implements AttributeConverter<Role, String> {
            public ValueConverter() {
                super(Role.class);
            }
        }

        @Override
        public String getValue() {
            return this.field;
        }
    }

    // endregion enum
}
