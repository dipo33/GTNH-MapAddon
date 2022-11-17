package io.github.dipo33.gtmapaddon.command.factory.exception;

public class CommandParseException extends CommandInvalidUsageException {

    public CommandParseException(String message, Object... args) {
        super(message, args);
    }
}
