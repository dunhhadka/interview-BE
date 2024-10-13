package org.example.interview.user.application.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.interview.user.application.utils.Email;
import org.example.interview.user.domain.user.model.User;

import java.util.UUID;


@Getter
@Setter
public class UserRequest {
    private UUID id;
    private String avatarUrl; // mới đơn giản là nhập url, chưa chuyển thành dạng byte đẩy lên clound
    @NotNull
    @Size(max = 100)
    @Email
    private String email;
    @NotNull
    private String password;
    @NotNull
    @Size(max = 20)
    private String phone;
    @Size(max = 50)
    private String fullName;
    private String firstName;
    private String lastName;

    private String national;

    private User.Role role;

    private User.Type type;

    private User.Language language;
}
