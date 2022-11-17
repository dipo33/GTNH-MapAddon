package io.github.dipo33.gtmapaddon.command.factory.argument;

import io.github.dipo33.gtmapaddon.command.factory.exception.CommandException;
import io.github.dipo33.gtmapaddon.command.factory.exception.CommandParseException;
import io.github.dipo33.gtmapaddon.command.factory.subcommand.WithArguments;
import net.minecraft.command.ICommandSender;

import java.util.Collections;
import java.util.List;

public class ArgString extends Argument<String> {

    private Integer expectedLength = null;

    public ArgString(String name) {
        super(name);
    }

    @Override
    public String parse(String value, ICommandSender sender) throws CommandException {
        if (expectedLength != null && value.length() != expectedLength) {
            throw new CommandParseException("parseStringExpectedLength", getName(), expectedLength);
        }

        return value;
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
    public Factory getFactory(WithArguments commandFactory) {
        return new Factory(this, commandFactory);
    }

    public static class Factory extends ArgumentFactory<String> {

        public Factory(ArgString argument, WithArguments commandFactory) {
            super(argument, commandFactory);
        }

        public Factory setExpectedLength(int expectedLength) {
            ((ArgString) argument).expectedLength = expectedLength;

            return this;
        }

        @Override
        public WithArguments build() {
            return super.build();
        }
    }
}