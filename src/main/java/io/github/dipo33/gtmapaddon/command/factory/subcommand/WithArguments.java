package io.github.dipo33.gtmapaddon.command.factory.subcommand;

import io.github.dipo33.gtmapaddon.command.factory.argument.ArgString;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgumentFactory;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgumentList;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.function.BiConsumer;

public interface WithArguments {

    ArgumentFactory<Integer> addIntArgument(String name);

    <E extends Enum<E>> ArgumentFactory<Enum<E>> addEnumArgument(String name, Class<E> clazz);

    ArgumentFactory<EntityPlayerMP> addPlayerArgument(String name);

    ArgumentFactory<String> addOfflinePlayerArgument(String name);

    ArgString.Factory addStringArgument(String name);

    SubCommand build(BiConsumer<ArgumentList, ICommandSender> processor);

    ArgumentFactory<Integer> addDipoPriceArg(String name);
}
