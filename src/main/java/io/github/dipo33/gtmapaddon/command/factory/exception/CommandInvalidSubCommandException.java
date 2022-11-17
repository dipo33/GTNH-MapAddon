package io.github.dipo33.gtmapaddon.command.factory.exception;

public class CommandInvalidSubCommandException extends CommandInvalidUsageException {

    public CommandInvalidSubCommandException(String subCommandName) {
        super("invalidSubCommand", subCommandName);
    }
}
