package io.github.dipo33.gtmapaddon.command.factory.argument;

import io.github.dipo33.gtmapaddon.command.factory.subcommand.WithArguments;
import net.minecraft.command.ICommandSender;

import java.util.Collections;
import java.util.List;

public class ArgString extends Argument<String> {

    public ArgString(String name) {
        super(name);
    }

    @Override
    public boolean fill(String value, ICommandSender sender) {
        super.set(value);

        return true;
    }

    @Override
    public String getUsage() {
        return String.format("<%s>", getName());
    }

    @Override
    public String getError() {
        return "None";
    }

    @Override
    public List<String> getTabCompletionOptions(ICommandSender sender, String arg) {
        return Collections.emptyList();
    }

    @Override
    public ArgumentFactory<String> getFactory(WithArguments commandFactory) {
        return new ArgumentFactory<>(this, commandFactory);
    }
}