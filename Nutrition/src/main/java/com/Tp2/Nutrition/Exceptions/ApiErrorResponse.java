package com.Tp2.Nutrition.Exceptions;

import java.time.LocalDateTime;

public record ApiErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String message
) {
}
