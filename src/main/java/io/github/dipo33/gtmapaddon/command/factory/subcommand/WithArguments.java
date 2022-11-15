package io.github.dipo33.gtmapaddon.command.factory.subcommand;

import io.github.dipo33.gtmapaddon.command.factory.argument.ArgOfflinePlayer;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgPlayer;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgumentFactory;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgumentList;
import net.minecraft.command.ICommandSender;

import java.util.function.BiConsumer;

public interface WithArguments {

    ArgumentFactory<Integer> addIntArgument(String name);

    <E extends Enum<E>> ArgumentFactory<Enum<E>> addEnumArgument(String name, Class<E> clazz);

    ArgPlayer.Factory addPlayerArgument(String name);

    ArgOfflinePlayer.Factory addOfflinePlayerArgument(String name);

    ArgumentFactory<String> addStringArgument(String name);

    SubCommand build(BiConsumer<ArgumentList, ICommandSender> processor);
}
