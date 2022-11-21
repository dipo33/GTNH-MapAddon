package io.github.dipo33.gtmapaddon.command.factory.subcommand;

import io.github.dipo33.gtmapaddon.command.factory.argument.ArgInt;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgString;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgumentFactory;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgumentList;
import io.github.dipo33.gtmapaddon.command.factory.exception.CommandException;
import io.github.dipo33.gtmapaddon.utils.ThrowingBiConsumer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public interface WithArguments {

    ArgInt.Factory addIntArgument(String name);

    <E extends Enum<E>> ArgumentFactory<Enum<E>> addEnumArgument(String name, Class<E> clazz);

    ArgumentFactory<EntityPlayerMP> addPlayerArgument(String name);

    ArgumentFactory<String> addOfflinePlayerArgument(String name);

    ArgString.Factory addStringArgument(String name);

    SubCommand build(ThrowingBiConsumer<ArgumentList, ICommandSender, CommandException> processor);

    ArgumentFactory<Integer> addDipoPriceArg(String name);
}
