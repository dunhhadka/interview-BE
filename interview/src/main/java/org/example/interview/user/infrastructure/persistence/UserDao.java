package org.example.interview.user.infrastructure.persistence;

import org.example.interview.user.application.model.UserSearchRequest;
import org.example.interview.user.infrastructure.data.UserDto;

import java.util.List;

public interface UserDao {
    List<UserDto> search(UserSearchRequest request);

    int searchCount(UserSearchRequest request);
}
