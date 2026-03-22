package com.Tp2.Nutrition.Exceptions;

public class InvalidBasketRequestException extends RuntimeException {
    public InvalidBasketRequestException(String message) {
        super(message);
    }
}
