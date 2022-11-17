package io.github.dipo33.gtmapaddon.command.factory.subcommand;

import io.github.dipo33.gtmapaddon.command.factory.exception.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.List;

public abstract class SubCommand {

    private final String name;
    protected SubCommand command;

    public SubCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void setMainCommand(SubCommand command);

    public abstract List<String> getCommandUsages();

    public abstract List<String> getCommandUsages(String[] args);

    public abstract List<String> addTabCompletionOptions(ICommandSender sender, String[] args);

    public abstract boolean isUsernameIndex(String[] args, int index);

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        try {
            processCommandInternal(sender, args);
        } catch (CommandException e) {
            e.insertArg(getName());
            throw e;
        }
    }

    public abstract void processCommandInternal(ICommandSender sender, String[] args) throws CommandException;
}
