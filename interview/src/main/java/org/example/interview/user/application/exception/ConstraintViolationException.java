package org.example.interview.user.application.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ConstraintViolationException extends RuntimeException {
    private List<String> fields;
    private String message;
    private String field;
    private Integer status;

    public ConstraintViolationException(List<String> fields, String message) {
        this.fields = fields;
        this.message = message;
    }

    public ConstraintViolationException(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public ConstraintViolationException(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
