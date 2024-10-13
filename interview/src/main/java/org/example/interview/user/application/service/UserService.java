package org.example.interview.user.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.interview.user.application.exception.ConstraintViolationException;
import org.example.interview.user.application.mapper.UserMapper;
import org.example.interview.user.application.model.*;
import org.example.interview.user.application.utils.PhoneUtils;
import org.example.interview.user.application.utils.SecurityHelper;
import org.example.interview.user.domain.user.model.User;
import org.example.interview.user.domain.user.model.UserGeneralInfo;
import org.example.interview.user.infrastructure.configuration.JwtService;
import org.example.interview.user.infrastructure.data.UserDto;
import org.example.interview.user.infrastructure.persistence.JpaUserRepository;
import org.example.interview.user.infrastructure.persistence.UserDao;
import org.example.interview.user.infrastructure.persistence.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    private final UserRepository userRepository;
    private final JpaUserRepository jpaUserRepository;

    private final UserMapper userMapper;

    @Transactional
    public UserResponse create(UserRequest request, User author) {
        var userGeneralInfo = buildGeneralInfo(request);

        var user = new User(
                author,
                userGeneralInfo,
                request.getPassword(),
                request.getRole(),
                request.getLanguage(),
                request.getType(),
                request.getNational());

        userRepository.save(user);

        return userMapper.fromEntityToResponse(user);
    }

    private UserGeneralInfo buildGeneralInfo(UserRequest request) {
        var email = validate(request);

        var phone = extractPhone(request.getPhone());

        return UserGeneralInfo.builder()
                .fullName(request.getFullName())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .avatarUrl(request.getAvatarUrl())
                .email(email)
                .phone(phone)
                .build();
    }

    private String extractPhone(String phoneRequest) {
        if (StringUtils.isBlank(phoneRequest) || !PhoneUtils.isValid(phoneRequest)) {
            throw new ConstraintViolationException(HttpStatus.BAD_REQUEST.value(), "Phone is invalid");
        }
        return PhoneUtils.normalize(phoneRequest);
    }

    private String validate(UserRequest request) {
        var email = request.getEmail();
        var users = userRepository.findByEmail(email);
        if (request.getId() != null && CollectionUtils.isNotEmpty(users)) {
            var user = users.get(0);
            if (Objects.equals(request.getId(), user.getId())) {
                return email;
            }
        }
        if (CollectionUtils.isNotEmpty(users)) {
            throw new ConstraintViolationException(HttpStatus.BAD_REQUEST.value(), "Email have already existed");
        }
        return email;
    }

    @Transactional
    public UserResponse active(UUID id) {
        var author = SecurityHelper.getAuthorDetail();
        var user = findById(id);
        user.active(author);
        userRepository.save(user);
        return userMapper.fromEntityToResponse(user);
    }

    private User findById(UUID id) {
        var user = userRepository.findById(id);
        if (user == null) {
            throw new ConstraintViolationException("id", "user not found by = " + id);
        }
        return user;
    }

    public List<UserResponse> search(UserSearchRequest request) {
        List<UserDto> userDtos = userDao.search(request);
        var userIds = userDtos.stream().map(UserDto::getId).toList();
        var users = jpaUserRepository.findAllById(userIds);
        return users.stream().map(userMapper::fromEntityToResponse).toList();
    }

    @Transactional
    public UserResponse markDelete(UUID id) {
        var author = SecurityHelper.getAuthorDetail();
        var user = this.findById(id);
        user.markDelete(author);
        userRepository.save(user);
        return userMapper.fromEntityToResponse(user);
    }

    @Transactional
    public UserResponse update(UUID id, UserRequest request) {
        var author = SecurityHelper.getAuthorDetail();
        Objects.requireNonNull(author);

        var user = findById(id);
        request.setId(id);

        user.setGeneralInfo(buildGeneralInfo(request), author);

        user.setPassword(request.getPassword());
        user.setNational(request.getNational());
        user.setRole(request.getRole());
        user.setType(user.getType());
        user.setLanguage(request.getLanguage());

        userRepository.save(user);

        return userMapper.fromEntityToResponse(user);
    }

    public int searchCount(UserSearchRequest request) {
        return userDao.searchCount(request);
    }

    public UserResponse getById(UUID id) {
        var user = this.findById(id);
        return userMapper.fromEntityToResponse(user);
    }

    public LoginResponse login(LoginRequest request) {
        var user = jpaUserRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if (user == null) {
            throw new ConstraintViolationException(HttpStatus.UNAUTHORIZED.value(), "Email or Password is invalid");
        }
        var token = JwtService.generateToken(user);
        user.setToken(token);

        jpaUserRepository.save(user);

        return LoginResponse.builder()
                .user(userMapper.fromEntityToResponse(user))
                .token(token)
                .build();
    }

    @Transactional
    public UserResponse register(UserRequest request) {
        var userGeneralInfo = buildGeneralInfo(request);

        var user = new User(
                null,
                userGeneralInfo,
                request.getPassword(),
                User.Role.member,
                User.Language.viet_nam,
                User.Type.normal,
                "viet_name");

        userRepository.save(user);

        return userMapper.fromEntityToResponse(user);
    }
}
