package org.example.interview.user.application.mapper;

import org.example.interview.user.application.model.UserResponse;
import org.example.interview.user.domain.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "generalInfo.fullName", target = "fullName")
    @Mapping(source = "generalInfo.firstName", target = "firstName")
    @Mapping(source = "generalInfo.lastName", target = "lastName")
    @Mapping(source = "generalInfo.avatarUrl", target = "avatarUrl")
    @Mapping(source = "generalInfo.email", target = "email")
    @Mapping(source = "generalInfo.phone", target = "phone")
    public abstract UserResponse fromEntityToResponse(User user);
}
