package org.example.interview.user.domain.user.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.interview.ddd.ValueObject;

@Getter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class UserGeneralInfo implements ValueObject<UserGeneralInfo> {
    @NotBlank
    @Size(max = 100)
    private String fullName;

    private String firstName;
    private String lastName;

    private String avatarUrl;

    @NotNull
    @Size(max = 100)
    private String email;

    @NotNull
    @Size(max = 20)
    private String phone;
}
