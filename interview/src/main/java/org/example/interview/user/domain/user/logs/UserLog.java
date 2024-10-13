package org.example.interview.user.domain.user.logs;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_logs")
public class UserLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private UUID userId;

    @Lob
    private String data;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private AppEventType verb;

    @NotNull
    private Instant createdOn;

    public UserLog(
            UUID userId,
            AppEventType appEventType,
            String data
    ) {
        this.userId = userId;
        this.verb = appEventType;
        this.data = data;
        this.createdOn = Instant.now();
    }
}
