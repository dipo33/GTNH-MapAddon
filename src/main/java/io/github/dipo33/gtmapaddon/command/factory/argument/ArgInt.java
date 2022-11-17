package io.github.dipo33.gtmapaddon.command.factory.argument;

import io.github.dipo33.gtmapaddon.command.factory.exception.CommandException;
import io.github.dipo33.gtmapaddon.command.factory.exception.CommandParseException;
import io.github.dipo33.gtmapaddon.command.factory.subcommand.WithArguments;
import net.minecraft.command.ICommandSender;

import java.util.Collections;
import java.util.List;

public class ArgInt extends Argument<Integer> {

    public ArgInt(String name) {
        super(name);
    }

    @Override
    public Integer parse(String value, ICommandSender sender) throws CommandException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new CommandParseException("parseInteger", value, getName());
        }
    }

    @Override
    public String getUsageInternal() {
        return String.format("<%s>", getName());
    }

    @Override
    public List<String> getTabCompletionOptions(ICommandSender sender, String arg) {
        return Collections.emptyList();
    }

    @Override
    public ArgumentFactory<Integer> getFactory(WithArguments commandFactory) {
        return new ArgumentFactory<>(this, commandFactory);
    }
}