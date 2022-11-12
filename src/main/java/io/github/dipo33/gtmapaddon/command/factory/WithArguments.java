package io.github.dipo33.gtmapaddon.command.factory;

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
    
    ArgumentFactory<String> addStringArgument(String name);

    AbstractSubCommand build(BiConsumer<ArgumentList, ICommandSender> processor);
}
