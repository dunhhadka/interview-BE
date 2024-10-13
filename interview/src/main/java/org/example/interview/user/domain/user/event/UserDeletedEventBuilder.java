package org.example.interview.user.domain.user.event;

import org.example.interview.event.AbstractEventBuilder;
import org.example.interview.user.domain.user.model.User;

import java.util.List;

public class UserDeletedEventBuilder extends AbstractEventBuilder<User> {

    private final String deletedName;

    public UserDeletedEventBuilder(String author, String deletedName, User subject, String authorId) {
        super("delete", subject, author, List.of(), authorId);
        this.deletedName = deletedName;
    }

    @Override
    protected String buildEventDescription(String verb, User subject) {
        return String.format("%s đã xóa tài khoản %s", this.author, this.deletedName);
    }
}
