package io.github.dipo33.gtmapaddon.command.factory.argument;

import io.github.dipo33.gtmapaddon.command.factory.WithArguments;
import net.minecraft.command.ICommandSender;

import java.util.List;

public abstract class Argument<T> {

    private final String name;
    private T defaultValue = null;

    private T value;

    public Argument(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean isOptional() {
        return this.defaultValue != null;
    }

    public T get() {
        return this.value != null ? this.value : this.defaultValue;
    }

    public void set(T value) {
        this.value = value;
    }
    
    public void setDefault(T value) {
        this.defaultValue = value;
    }

    public abstract boolean fill(String value, ICommandSender sender);
    
    public abstract String getUsage();
    
    public abstract String getError();

    public abstract List<String> getTabCompletionOptions(ICommandSender sender, String arg);
    
    public abstract ArgumentFactory<T> getFactory(WithArguments commandFactory);
}
