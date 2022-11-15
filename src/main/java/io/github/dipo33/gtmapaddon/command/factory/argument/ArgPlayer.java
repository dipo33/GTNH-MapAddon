package io.github.dipo33.gtmapaddon.command.factory.argument;

import io.github.dipo33.gtmapaddon.command.factory.subcommand.WithArguments;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import scala.actors.threadpool.Arrays;

import java.util.List;

public class ArgPlayer extends Argument<EntityPlayerMP> {

    private boolean defaultsToSender = false;

    public ArgPlayer(String name) {
        super(name);
    }

    @Override
    public boolean isOptional() {
        return super.isOptional() || defaultsToSender;
    }

    public void setDefaultsToSender(boolean defaultsToSender) {
        this.defaultsToSender = defaultsToSender;
    }

    @Override
    public boolean fill(String value, ICommandSender sender) {
        final EntityPlayerMP player = defaultsToSender ?
                    CommandBase.getPlayer(sender, value) : PlayerSelector.matchOnePlayer(sender, value);
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
    public Factory getFactory(WithArguments commandFactory) {
        return new Factory(this, commandFactory);
    }

    public static class Factory extends ArgumentFactory<EntityPlayerMP> {

        public Factory(ArgPlayer argument, WithArguments commandFactory) {
            super(argument, commandFactory);
        }

        public Factory defaultsToSender() {
            ((ArgPlayer) super.argument).setDefaultsToSender(true);

            return this;
        }

        @Override
        public WithArguments build() {
            return super.build();
        }
    }
}