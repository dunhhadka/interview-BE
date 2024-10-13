package org.example.interview.user.application.utils;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChangeLogUtils {

    private static final String EVENT_NAME = "event_name";

    public static <T> JsonNode filteredStream(Collection<JsonNode> eventNodes, Class<T> clazz) {
        var simpleClassName = clazz.getSimpleName();
        return eventNodes.stream().filter(node -> simpleClassName.equals(node.get(EVENT_NAME).textValue())).findFirst().orElse(null);
    }
}
