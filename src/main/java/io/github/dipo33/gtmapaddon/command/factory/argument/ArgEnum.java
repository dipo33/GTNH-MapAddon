package io.github.dipo33.gtmapaddon.command.factory.argument;

import io.github.dipo33.gtmapaddon.command.factory.WithArguments;
import net.minecraft.command.ICommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArgEnum<E extends Enum<E>> extends Argument<Enum<E>> {

    private final Class<E> clazz;

    public ArgEnum(String name, Class<E> clazz) {
        super(name);
        this.clazz = clazz;
    }

    @Override
    public boolean fill(String value, ICommandSender sender) {
        for (E option : clazz.getEnumConstants()) {
            if (option.name().equalsIgnoreCase(value)) {
                super.set(option);
                return true;
            }
        }

        return false;
    }

    @Override
    public String getUsage() {
        final StringBuilder builder = new StringBuilder("<");

        E[] options = clazz.getEnumConstants();
        for (int i = 0; i < options.length; ++i) {
            if (i != 0) {
                builder.append("|");
            }
            builder.append(options[i].name().toLowerCase());
        }

        return builder.append(">").toString();
    }

    @Override
    public String getError() {
        return String.format("Not a valid option of <%s>", getName());
    }

    @Override
    public List<String> getTabCompletionOptions(ICommandSender sender, String arg) {
        return Arrays.stream(clazz.getEnumConstants())
                    .map(Enum::toString)
                    .map(String::toLowerCase)
                    .filter(option -> option.startsWith(arg))
                    .collect(Collectors.toList());
    }

    @Override
    public ArgumentFactory<Enum<E>> getFactory(WithArguments commandFactory) {
        return new ArgumentFactory<>(this, commandFactory);
    }
}