package io.github.dipo33.gtmapaddon.command.factory.argument;

import io.github.dipo33.gtmapaddon.command.factory.subcommand.WithArguments;
import net.minecraft.command.ICommandSender;

import java.util.function.Function;

public class ArgumentFactory<T> {
    protected final Argument<T> argument;
    private final WithArguments commandFactory;

    public ArgumentFactory(Argument<T> argument, WithArguments commandFactory) {
        this.argument = argument;
        this.commandFactory = commandFactory;
    }

    public ArgumentFactory<T> setDefault(T value) {
        argument.setDefault(value);

        return this;
    }

    public ArgumentFactory<T> setDefaultFactory(Function<ICommandSender, T> defaultFactory) {
        argument.setDefaultFactory(defaultFactory);

        return this;
    }

    public WithArguments build() {
        return commandFactory;
    }
}
