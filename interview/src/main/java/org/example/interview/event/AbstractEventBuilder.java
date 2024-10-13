package org.example.interview.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Slf4j
public abstract class AbstractEventBuilder<T> {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRAP_ROOT_VALUE);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new ISO8601DateFormat());
    }

    protected final String verb;
    protected final T subject;
    protected final String author;

    protected final String authorId;

    private final List<String> keywords;

    protected String additional;

    protected AbstractEventBuilder(String verb, T subject, String author, List<String> keywords, String authorId) {
        this.verb = verb;
        this.subject = subject;
        this.author = author;
        this.keywords = keywords;
        this.authorId = authorId;
    }

    public EventCreatedRequest buildEvent() {
        var requestBuilder = EventCreatedRequest.builder()
                .verb(verb)
                .author(author)
                .description(buildEventDescription(verb, subject))
                .additional(additional);

        if (CollectionUtils.isNotEmpty(keywords)) {
            requestBuilder.keywords(buildKeyword(keywords));
        } else if (StringUtils.isNotBlank(author)) {
            requestBuilder.keywords(buildKeyword(List.of(author)));
        }

        return requestBuilder.build();
    }

    private String buildKeyword(List<String> keywords) {
        try {
            return objectMapper.writeValueAsString(keywords);
        } catch (JsonProcessingException e) {
            log.warn("", e);
        }
        return null;
    }

    protected abstract String buildEventDescription(String verb, T subject);
}
