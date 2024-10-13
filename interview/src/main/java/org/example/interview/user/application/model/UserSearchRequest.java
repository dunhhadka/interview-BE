package org.example.interview.user.application.model;

import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Setter
public class UserSearchRequest extends PagingRequest {
    private String query;
    private List<UUID> ids;
    private String email;
    private String phone;
    private String fullName;
    private String firstName;
    private String lastName;
    private String national;
    private String role;
    private String type;
    private String language;

    private Instant createdAtMin;
    private Instant createdAtMax;

    private Instant activatedAtMin;
    private Instant activatedAtMax;

    public String getQuery() {
        return query;
    }

    public String getRole() {
        return role;
    }

    public String getType() {
        return type;
    }

    public String getLanguage() {
        return language;
    }

    public Instant getCreatedAtMin() {
        return createdAtMin;
    }

    public Instant getCreatedAtMax() {
        return createdAtMax;
    }

    public Instant getActivatedAtMin() {
        return activatedAtMin;
    }

    public Instant getActivatedAtMax() {
        return activatedAtMax;
    }

    public List<UUID> getIds() {
        return ids;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getFullName() {
        return fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNational() {
        return national;
    }
}
