package io.github.dipo33.gtmapaddon.command.factory.argument;

import io.github.dipo33.gtmapaddon.command.factory.subcommand.WithArguments;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import scala.actors.threadpool.Arrays;

import java.util.List;

public class ArgOfflinePlayer extends Argument<String> {

    private boolean defaultsToSender = false;

    public ArgOfflinePlayer(String name) {
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
        super.set(value);
        if (this.defaultsToSender) {
            super.setDefault(sender.getCommandSenderName());
        }

        return true;
    }

    @Override
    public String getUsage() {
        return String.format("[<%s>]", getName());
    }

    @Override
    public String getError() {
        return "None";
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

    public static class Factory extends ArgumentFactory<String> {

        public Factory(ArgOfflinePlayer argument, WithArguments commandFactory) {
            super(argument, commandFactory);
        }

        public Factory defaultsToSender() {
            ((ArgOfflinePlayer) super.argument).setDefaultsToSender(true);

            return this;
        }

        @Override
        public WithArguments build() {
            return super.build();
        }
    }
}