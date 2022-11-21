package io.github.dipo33.gtmapaddon.command.factory.exception;

import net.minecraft.client.resources.I18n;

public class CommandInvalidUsageException extends CommandException {

    public CommandInvalidUsageException(String message, Object... args) {
        super(I18n.format("dipogtmapaddon.command.exception.error") + ": " +
                I18n.format("dipogtmapaddon.command.exception." + message, args));
    }
}
