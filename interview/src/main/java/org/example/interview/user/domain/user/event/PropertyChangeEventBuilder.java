package org.example.interview.user.domain.user.event;

import org.example.interview.event.AbstractEventBuilder;
import org.example.interview.user.domain.user.model.User;

import java.util.List;

public class PropertyChangeEventBuilder extends AbstractEventBuilder<User> {
    public PropertyChangeEventBuilder(String verb, User subject, String author, List<String> keywords, String data, String authorId) {
        super(verb, subject, author, keywords, authorId);
        this.additional = data;
    }

    @Override
    protected String buildEventDescription(String verb, User subject) {
        return String.format("%s đã cập nhật %s", author, subject.getGeneralInfo().getFullName());
    }
}
