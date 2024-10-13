package org.example.interview.user.infrastructure.persistence;

import org.example.interview.user.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<User, UUID> {
    @Query(value = "SELECT * FROM users u WHERE u.email = :email AND u.password = :password",nativeQuery = true)
    User findByEmailAndPassword(String email, String password);
}
