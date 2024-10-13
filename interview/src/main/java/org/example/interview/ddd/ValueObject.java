package org.example.interview.ddd;

import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface ValueObject<T> {
    default boolean sameAs(T other) {
        return Objects.equals(this, other);
    }

    @SneakyThrows
    default List<Triple<String, Object, Object>> getDiffs(T other) {
        if (this.sameAs(other)) {
            return List.of();
        }
        var result = new ArrayList<Triple<String, Object, Object>>();

        for (var field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value1 = field.get(this);
            Object value2 = field.get(other);
            if (!Objects.equals(value1, value2)) {
                result.add(Triple.of(field.getName(), value1, value2));
            }
        }

        return result;
    }
}
