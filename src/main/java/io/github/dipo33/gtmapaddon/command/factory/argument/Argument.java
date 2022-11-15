package io.github.dipo33.gtmapaddon.command.factory.argument;

import io.github.dipo33.gtmapaddon.command.factory.subcommand.WithArguments;
import net.minecraft.command.ICommandSender;

import java.util.List;
import java.util.function.Function;

public abstract class Argument<T> {

    private final String name;
    private T defaultValue = null;
    private Function<ICommandSender, T> defaultFactory = null;

    private T value;

    public Argument(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean isRequired() {
        return this.defaultValue == null && this.defaultFactory == null;
    }

    public T get() {
        return this.value;
    }

    public void set(T value) {
        this.value = value;
    }

    public void setDefault(T value) {
        this.defaultValue = value;
    }

    public void setDefaultFactory(Function<ICommandSender, T> defaultFactory) {
        this.defaultFactory = defaultFactory;
    }

    public abstract boolean fill(String value, ICommandSender sender);

    public final boolean fillDefaults(ICommandSender sender) {
        if (this.defaultFactory != null) {
            this.value = this.defaultFactory.apply(sender);
        } else if (this.defaultValue != null) {
            this.value = this.defaultValue;
        } else {
            return false;
        }

        return true;
    }

    public abstract String getUsage();

    public abstract String getError();

    public abstract List<String> getTabCompletionOptions(ICommandSender sender, String arg);

    public abstract ArgumentFactory<T> getFactory(WithArguments commandFactory);
}
