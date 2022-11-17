package io.github.dipo33.gtmapaddon.command.factory.argument;

import io.github.dipo33.gtmapaddon.command.factory.exception.CommandException;
import io.github.dipo33.gtmapaddon.command.factory.exception.CommandParseException;
import io.github.dipo33.gtmapaddon.command.factory.subcommand.WithArguments;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import scala.actors.threadpool.Arrays;

import java.util.List;

public class ArgPlayer extends Argument<EntityPlayerMP> {

    public ArgPlayer(String name) {
        super(name);
    }

    @Override
    public EntityPlayerMP parse(String value, ICommandSender sender) throws CommandException {
        final EntityPlayerMP player = PlayerSelector.matchOnePlayer(sender, value);
        if (player == null) {
            throw new CommandParseException("parsePlayer", value);
        }

        return player;
    }

    @Override
    public String getUsageInternal() {
        return String.format("<%s>", getName());
    }

    @Override
    public List<String> getTabCompletionOptions(ICommandSender sender, String arg) {
        //noinspection unchecked
        return Arrays.asList(MinecraftServer.getServer().getAllUsernames());
    }

    @Override
    public ArgumentFactory<EntityPlayerMP> getFactory(WithArguments commandFactory) {
        return new ArgumentFactory<>(this, commandFactory);
    }
}