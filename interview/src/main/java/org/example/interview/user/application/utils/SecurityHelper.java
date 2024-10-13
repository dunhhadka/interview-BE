package org.example.interview.user.application.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.interview.user.domain.user.model.User;
import org.example.interview.user.infrastructure.configuration.CustomUserDetail;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityHelper {
    public static User getAuthorDetail() {
        var context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        var authentication = context.getAuthentication();
        User user = null;
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetail userDetail) {
            user = userDetail.getUser();
        }
        return user;
    }
}
