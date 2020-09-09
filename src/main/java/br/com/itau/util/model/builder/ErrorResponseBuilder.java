package br.com.itau.util.model.builder;

import br.com.itau.util.model.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;

public final class ErrorResponseBuilder {
    private List<String> messages;
    private LocalDateTime timestamp;

    private ErrorResponseBuilder() {
    }

    public static ErrorResponseBuilder anErrorResponse() {
        return new ErrorResponseBuilder();
    }

    public ErrorResponseBuilder messages(List<String> messages) {
        this.messages = messages;
        return this;
    }

    public ErrorResponseBuilder timestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public ErrorResponse build() {
        return new ErrorResponse(messages, timestamp);
    }
}
