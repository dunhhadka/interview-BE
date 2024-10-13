package org.example.interview.user.domain.user.event;

import org.example.interview.event.AbstractEventBuilder;
import org.example.interview.user.domain.user.model.User;

import java.util.List;

public class UserActivatedEventBuilder extends AbstractEventBuilder<User> {

    private final String activatedUserName;

    public UserActivatedEventBuilder(String author, String activatedUserName, User subject, String authorId) {
        super("update", subject, author, List.of(), authorId);
        this.activatedUserName = activatedUserName;
    }

    @Override
    protected String buildEventDescription(String verb, User subject) {
        return String.format("%s đã kích hoạt tài khoản %s", this.author, this.activatedUserName);
    }
}
