package org.example.interview.user.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.example.interview.user.application.exception.ConstraintViolationException;
import org.example.interview.user.infrastructure.persistence.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var users = userRepository.findByEmail(email);
        if (CollectionUtils.isNotEmpty(users)) {
            return new CustomUserDetail(users.get(0));
        }
        throw new ConstraintViolationException("email", "Authenticated fail");
    }
}
