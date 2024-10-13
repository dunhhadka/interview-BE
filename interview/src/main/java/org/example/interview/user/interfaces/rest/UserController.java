package org.example.interview.user.interfaces.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.interview.user.application.model.*;
import org.example.interview.user.application.service.UserService;
import org.example.interview.user.application.utils.SecurityHelper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponse create(@RequestBody @Valid UserRequest request) {
        var author = SecurityHelper.getAuthorDetail();
        return userService.create(request, author);
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody @Valid UserRequest request) {
        return userService.register(request);
    }

    @PostMapping("/{id}/active")
    public UserResponse active(@PathVariable UUID id) {
        return userService.active(id);
    }

    @DeleteMapping("/{id}/delete")
    public UserResponse delete(@PathVariable UUID id) {
        return userService.markDelete(id);
    }

    @GetMapping("/search")
    public List<UserResponse> search(UserSearchRequest request) {
        return userService.search(request);
    }

    @GetMapping("/search/count")
    public int searchCount(UserSearchRequest request) {
        return userService.searchCount(request);
    }

    @PutMapping("/{id}")
    public UserResponse update(
            @PathVariable UUID id,
            @RequestBody @Valid UserRequest request
    ) {
        return userService.update(id, request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return userService.login(request);
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable UUID id) {
        return userService.getById(id);
    }
}
