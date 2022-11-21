package io.github.dipo33.gtmapaddon.command.factory.exception;

import net.minecraft.util.StatCollector;

public class CommandProcessException extends CommandException {

    public CommandProcessException(String message, Object... args) {
        super(StatCollector.translateToLocalFormatted("dipogtmapaddon.command.exception." + message, args));
    }
}
