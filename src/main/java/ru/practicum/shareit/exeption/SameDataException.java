package ru.practicum.shareit.exeption;

public class SameDataException extends RuntimeException {
    public SameDataException(String message) {
        super(message);
    }
}
