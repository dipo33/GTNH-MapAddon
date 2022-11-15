package io.github.dipo33.gtmapaddon.command.factory.argument;

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
    public boolean fill(String value, ICommandSender sender) {
        final EntityPlayerMP player = PlayerSelector.matchOnePlayer(sender, value);
        super.set(player);

        return player != null;
    }

    @Override
    public String getUsage() {
        return String.format("<%s>", getName());
    }

    @Override
    public String getError() {
        return "Specified player doesn't exist";
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