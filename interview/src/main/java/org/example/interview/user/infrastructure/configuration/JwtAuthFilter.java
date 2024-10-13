package org.example.interview.user.infrastructure.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.interview.user.infrastructure.persistence.JpaUserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JpaUserRepository jpaUserRepository;
    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = JwtService.extractTokenFromHeader(request);
        if (StringUtils.isNotBlank(token) && JwtService.validate(token, request)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            var claims = JwtService.extractClaims(token);
            var id = claims.getSecond();
            var user = jpaUserRepository.findById(id);
            if (user.isEmpty()) {
                request.setAttribute("not_found_by_id", "User not found by id is " + id + "!");
            } else if (!Objects.equals(user.get().getToken(), token)) {
                request.setAttribute("closed_session", "closed_session");
            } else {
                UserDetails userDetails = customUserDetailService.loadUserByUsername(claims.getFirst());
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
