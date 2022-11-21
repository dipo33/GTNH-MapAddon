package io.github.dipo33.gtmapaddon.command.factory.argument;

import io.github.dipo33.gtmapaddon.command.factory.exception.CommandException;
import io.github.dipo33.gtmapaddon.command.factory.exception.CommandParseException;
import io.github.dipo33.gtmapaddon.command.factory.subcommand.WithArguments;
import io.github.dipo33.gtmapaddon.compat.MoneyModWrapper;
import net.minecraft.command.ICommandSender;

import java.util.Collections;
import java.util.List;

// TODO: Get rid of this in the future (replace by customizable ArgInt)
public class ArgDipoPrice extends Argument<Integer> {

    public ArgDipoPrice(String name) {
        super(name);
    }

    @Override
    public Integer parse(String value, ICommandSender sender) throws CommandException {
        try {
            return MoneyModWrapper.stringToPrice(value);
        } catch (NumberFormatException e) {
            throw new CommandParseException("parseDipoPrice", value);
        }
    }

    @Override
    protected String getUsageInternal() {
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
