package org.example.interview.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventResponse> search(EventFilterRequest request) {
        return eventService.search(request);
    }
}
