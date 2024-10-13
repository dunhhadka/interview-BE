package org.example.interview.user.domain.user.event;

import org.example.interview.event.AbstractEventBuilder;
import org.example.interview.user.domain.user.model.User;

import java.util.List;

public class UserCreatedEventBuilder extends AbstractEventBuilder<User> {

    public UserCreatedEventBuilder(UserCreatedEvent event, User user, String authorId) {
        super("create", user,
                user.getCreatedByUserId() != null ? event.getCreatedBy() : user.getGeneralInfo().getFullName(),
                List.of(), authorId);
    }

    @Override
    protected String buildEventDescription(String verb, User subject) {
        if (this.authorId == null) {
            return String.format("%s đã tạo tài khoản", subject.getGeneralInfo().getFullName());
        }
        return String.format("%s đã tạo tài khoản %s", this.author, subject.getGeneralInfo().getFullName());
    }
}
