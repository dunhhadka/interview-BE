package org.example.interview.user.infrastructure.data;

import lombok.Getter;
import lombok.Setter;
import microsoft.sql.DateTimeOffset;
import org.example.interview.user.domain.user.model.User;

import java.util.UUID;

@Getter
@Setter
public class UserDto {

    private UUID id;

    private String fullName;
    private String firstName;
    private String lastName;

    private String avatarUrl;

    private String email;
    private String phone;
    private String password;

    private User.Role role;

    private DateTimeOffset joinDate;

    private DateTimeOffset createdOn;

    private DateTimeOffset modifiedOn;

    private User.Status status;

    private User.Language language;

    private boolean isNotification;

    private User.Type type;

    private String national;

    private UUID createdByUserId;

    private Integer version;
}
