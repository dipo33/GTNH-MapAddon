package io.github.dipo33.gtmapaddon.command.factory.exception;

public class CommandNotEnoughArgsException extends CommandInvalidUsageException {

    public CommandNotEnoughArgsException() {
        super("notEnoughArguments");
    }
}
