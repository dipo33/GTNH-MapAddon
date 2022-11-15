package io.github.dipo33.gtmapaddon.command.factory;

import net.minecraft.command.ICommand;

public class CommandFactory {
    private boolean onlyPlayer = false;

    private CommandFactory() {
    }

    public static CommandFactory createCommand() {
        return new CommandFactory();
    }

    public CommandFactory onlyPlayerUse() {
        this.onlyPlayer = true;
        return this;
    }

    public ICommand addSubCommand(SubCommand subCommand) {
        subCommand.setMainCommand(subCommand);
        return new GenericCommand(subCommand, onlyPlayer);
    }
}
