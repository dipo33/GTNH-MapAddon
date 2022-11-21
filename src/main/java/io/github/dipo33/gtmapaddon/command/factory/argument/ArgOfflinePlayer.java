package io.github.dipo33.gtmapaddon.command.factory.argument;

import io.github.dipo33.gtmapaddon.command.factory.subcommand.WithArguments;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import scala.actors.threadpool.Arrays;

import java.util.List;

public class ArgOfflinePlayer extends Argument<String> {

    public ArgOfflinePlayer(String name) {
        super(name);
    }

    @Override
    public String parse(String value, ICommandSender sender) {
        return value;
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
    public ArgumentFactory<String> getFactory(WithArguments commandFactory) {
        return new ArgumentFactory<>(this, commandFactory);
    }
}