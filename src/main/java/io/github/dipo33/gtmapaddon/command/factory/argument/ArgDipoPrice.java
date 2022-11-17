package io.github.dipo33.gtmapaddon.command.factory.argument;

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
    public boolean fill(String value, ICommandSender sender) {
        try {
            super.set(MoneyModWrapper.stringToPrice(value));

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    protected String getUsageInternal() {
        return String.format("<%s>", getName());
    }

    @Override
    public String getError() {
        return String.format("Can't parse argument <%s> to a price (two decimal points separated by dot)", getName());
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
