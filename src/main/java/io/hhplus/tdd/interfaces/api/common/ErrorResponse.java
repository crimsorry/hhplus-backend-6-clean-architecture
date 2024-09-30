package io.hhplus.tdd.interfaces.api.common;

public record ErrorResponse (
        String code,
        String message
) {
}
