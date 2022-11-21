package io.github.dipo33.gtmapaddon.command.factory.exception;

import net.minecraft.client.resources.I18n;

public class CommandProcessException extends CommandException {

    public CommandProcessException(String message, Object... args) {
        super(I18n.format("dipogtmapaddon.command.exception." + message, args));
    }
}
