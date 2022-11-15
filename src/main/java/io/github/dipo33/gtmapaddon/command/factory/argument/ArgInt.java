package io.github.dipo33.gtmapaddon.command.factory.argument;

import io.github.dipo33.gtmapaddon.command.factory.subcommand.WithArguments;
import net.minecraft.command.ICommandSender;

import java.util.Collections;
import java.util.List;

public class ArgInt extends Argument<Integer> {

    public ArgInt(String name) {
        super(name);
    }

    @Override
    public boolean fill(String value, ICommandSender sender) {
        try {
            super.set(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    @Override
    public String getUsage() {
        return String.format("<%s>", getName());
    }

    @Override
    public String getError() {
        return String.format("Can't parse argument <%s> to an integer", getName());
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