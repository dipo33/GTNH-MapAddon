package io.github.dipo33.gtmapaddon.command.factory.argument;

import io.github.dipo33.gtmapaddon.command.factory.exception.CommandException;
import io.github.dipo33.gtmapaddon.command.factory.exception.CommandParseException;
import io.github.dipo33.gtmapaddon.command.factory.subcommand.WithArguments;
import net.minecraft.command.ICommandSender;

import java.util.Collections;
import java.util.List;

public class ArgInt extends Argument<Integer> {

    private Integer minValue = null;
    private Integer maxValue = null;

    public ArgInt(String name) {
        super(name);
    }

    @Override
    public Integer parse(String stringValue, ICommandSender sender) throws CommandException {
        try {
            int value = Integer.parseInt(stringValue);
            if (minValue != null && value < minValue) {
                throw new CommandParseException("parseIntMinValue", getName(), minValue);
            } else if (maxValue != null && value > maxValue) {
                throw new CommandParseException("parseIntMaxValue", getName(), maxValue);
            }
            
            return value;
        } catch (NumberFormatException e) {
            throw new CommandParseException("parseInteger", stringValue, getName());
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
    public Factory getFactory(WithArguments commandFactory) {
        return new Factory(this, commandFactory);
    }

    public static class Factory extends ArgumentFactory<Integer> {

        public Factory(Argument<Integer> argument, WithArguments commandFactory) {
            super(argument, commandFactory);
        }

        public Factory setMinValue(int value) {
            ((ArgInt) argument).minValue = value;

            return this;
        }

        public Factory setMaxValue(int value) {
            ((ArgInt) argument).maxValue = value;

            return this;
        }
    }
}