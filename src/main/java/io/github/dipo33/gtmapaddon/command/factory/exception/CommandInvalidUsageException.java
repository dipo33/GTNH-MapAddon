package io.github.dipo33.gtmapaddon.command.factory.exception;

import net.minecraft.util.StatCollector;

public class CommandInvalidUsageException extends CommandException {

    public CommandInvalidUsageException(String message, Object... args) {
        super(StatCollector.translateToLocalFormatted("dipogtmapaddon.command.exception.error") + ": " +
                StatCollector.translateToLocalFormatted("dipogtmapaddon.command.exception." + message, args));
    }
}
