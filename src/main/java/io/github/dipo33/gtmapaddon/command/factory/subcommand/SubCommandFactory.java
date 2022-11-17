package io.github.dipo33.gtmapaddon.command.factory.subcommand;

import io.github.dipo33.gtmapaddon.command.factory.argument.ArgEnum;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgInt;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgOfflinePlayer;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgPlayer;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgString;
import io.github.dipo33.gtmapaddon.command.factory.argument.Argument;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgumentFactory;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgumentList;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class SubCommandFactory implements ISubCommandFactory, WithSubCommandsOnly {

    private final String name;
    private final List<SubCommand> subCommands = new ArrayList<>();
    private final List<Argument<?>> arguments = new ArrayList<>();

    private SubCommandFactory(String name) {
        this.name = name;
    }

    public static ISubCommandFactory createSubCommand(String name) {
        return new SubCommandFactory(name);
    }

    @Override
    public WithSubCommandsOnly addSubCommand(SubCommand subCommand) {
        subCommands.add(subCommand);
        return this;
    }

    @Override
    public ArgumentFactory<Integer> addIntArgument(String name) {
        ArgInt arg = new ArgInt(name);
        arguments.add(arg);

        return arg.getFactory(this);
    }

    @Override
    public <E extends Enum<E>> ArgumentFactory<Enum<E>> addEnumArgument(String name, Class<E> clazz) {
        ArgEnum<E> arg = new ArgEnum<>(name, clazz);
        arguments.add(arg);

        return arg.getFactory(this);
    }

    @Override
    public ArgumentFactory<EntityPlayerMP> addPlayerArgument(String name) {
        ArgPlayer arg = new ArgPlayer(name);
        arguments.add(arg);

        return arg.getFactory(this);
    }

    @Override
    public ArgumentFactory<String> addOfflinePlayerArgument(String name) {
        ArgOfflinePlayer arg = new ArgOfflinePlayer(name);
        arguments.add(arg);

        return arg.getFactory(this);
    }

    @Override
    public ArgString.Factory addStringArgument(String name) {
        ArgString arg = new ArgString(name);
        arguments.add(arg);

        return arg.getFactory(this);
    }

    @Override
    public SubCommand build(BiConsumer<ArgumentList, ICommandSender> processor) {
        return new ArgumentalSubcommand(name, arguments, processor);
    }

    @Override
    public SubCommand build() {
        return new NestingSubCommand(name, subCommands);
    }
}
