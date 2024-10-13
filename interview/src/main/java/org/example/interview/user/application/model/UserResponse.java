package org.example.interview.user.application.model;

import lombok.Getter;
import lombok.Setter;
import org.example.interview.user.domain.user.model.User;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class UserResponse {
    private UUID id;
    private String fullName;

    private String firstName;
    private String lastName;

    private String avatarUrl;

    private String email;

    private String phone;

    private String password;

    private User.Role role;

    private Instant joinDate;

    private Instant createdOn;

    private Instant modifiedOn;

    private User.Status status;

    private User.Language language;

    private boolean isNotification;

    private User.Type type;

    private String national;

    private UUID createdByUserId;
}
