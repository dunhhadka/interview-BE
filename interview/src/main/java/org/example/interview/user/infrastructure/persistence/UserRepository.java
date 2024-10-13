package org.example.interview.user.infrastructure.persistence;

import org.example.interview.user.domain.user.model.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    List<User> findByEmail(String email);

    void save(User user);

    User findById(UUID id);
}
