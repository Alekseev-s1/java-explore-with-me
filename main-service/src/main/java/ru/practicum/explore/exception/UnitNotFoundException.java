package ru.practicum.explore.exception;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class UnitNotFoundException extends RuntimeException {
    public UnitNotFoundException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

    public static Supplier<UnitNotFoundException> unitNotFoundException(String message, Object... args) {
        return () -> new UnitNotFoundException(message, args);
    }
}
