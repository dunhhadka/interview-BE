package org.example.interview.user.infrastructure.configuration.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorMessage {
    private String message;
}
