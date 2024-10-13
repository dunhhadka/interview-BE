package org.example.interview.user.domain.user.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import org.example.interview.user.domain.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DomainKafkaModelEvent extends User {
    @JsonProperty("events")
    private List<JsonNode> jsonNodeEvents = new ArrayList<>();
}
