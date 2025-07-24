package ru.practicum.shareit.booking;

import ru.practicum.shareit.exceptions.InvalidStateException;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static State from(String state) {
        try {
            return State.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidStateException("Некорректный формат state: " + state);
        }
    }
}
