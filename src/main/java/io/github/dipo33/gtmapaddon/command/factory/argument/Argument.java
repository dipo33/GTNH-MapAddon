package io.github.dipo33.gtmapaddon.command.factory.argument;

import io.github.dipo33.gtmapaddon.command.factory.exception.CommandException;
import io.github.dipo33.gtmapaddon.command.factory.subcommand.WithArguments;
import net.minecraft.command.ICommandSender;

import java.util.List;
import java.util.function.Function;

public abstract class Argument<T> {

    private final String name;
    private T defaultValue = null;
    private Function<ICommandSender, T> defaultFactory = null;

    public Argument(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean isRequired() {
        return this.defaultValue == null && this.defaultFactory == null;
    }

    public void setDefault(T value) {
        this.defaultValue = value;
    }

    public void setDefaultFactory(Function<ICommandSender, T> defaultFactory) {
        this.defaultFactory = defaultFactory;
    }

    public abstract T parse(String value, ICommandSender sender) throws CommandException;

    public final T fromDefaults(ICommandSender sender) {
        if (this.defaultFactory != null) {
            return this.defaultFactory.apply(sender);
        } else if (this.defaultValue != null) {
            return this.defaultValue;
        } else {
            return null; // TODO: Maybe an exception throw?
        }
    }

    public final String getUsage() {
        String usage = this.getUsageInternal();
        return this.isRequired() ? usage : "[" + usage + "]";
    }

    protected abstract String getUsageInternal();

    public abstract List<String> getTabCompletionOptions(ICommandSender sender, String arg);

    public abstract ArgumentFactory<T> getFactory(WithArguments commandFactory);
}
