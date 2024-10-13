package org.example.interview.event;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final EventRepository eventRepository;

    @Transactional
    public EventResponse create(EventCreatedRequest request) {
        var event = Event.builder()
                .author(request.getAuthor())
                .verb(request.getVerb())
                .description(request.getDescription())
                .keyword(request.getKeywords())
                .additional(request.getAdditional())
                .authorId(request.getAuthorId())
                .happenedAt(Instant.now())
                .build();
        var eventCreated = eventRepository.save(event);
        return toResponse(eventCreated);
    }

    private EventResponse toResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .author(event.getAuthor())
                .description(event.getDescription())
                .verb(event.getVerb())
                .happenedAt(event.getHappenedAt())
                .authorId(event.getAuthorId())
                .additional(event.getAdditional())
                .build();
    }

    public List<EventResponse> search(EventFilterRequest request) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM events e WHERE e.id IS NOT NULL \n");
        Map<String, Object> params = new HashMap<>();
//        if (StringUtils.isNotBlank(request.getAuthorId())) {
//            sqlBuilder.append(" AND e.author_id = :authorId \n");
//            params.put("authorId", request.getAuthorId());
//        }
        if (StringUtils.isNotBlank(request.getQuery())) {
            String query = '%' + request.getQuery() + '%';
            sqlBuilder.append(" AND (e.author LIKE :query OR e.description LIKE :query) \n");
            params.put("query", query);
        }
        sqlBuilder.append("ORDER BY e.happened_at desc");

        var result = entityManager.createNativeQuery(sqlBuilder.toString(), Event.class);
        if (!params.isEmpty()) {
            params.forEach(result::setParameter);
        }
        var events = (List<Event>) result.getResultList();
        return events.stream().map(this::toResponse).toList();
    }
}
