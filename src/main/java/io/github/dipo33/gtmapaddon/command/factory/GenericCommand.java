package io.github.dipo33.gtmapaddon.command.factory;

import io.github.dipo33.gtmapaddon.GeneralUtils;
import io.github.dipo33.gtmapaddon.command.factory.exception.CommandException;
import io.github.dipo33.gtmapaddon.command.factory.exception.CommandInvalidUsageException;
import io.github.dipo33.gtmapaddon.command.factory.subcommand.SubCommand;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class GenericCommand implements ICommand {

    private final List<String> aliases;
    private final SubCommand command;
    private final boolean onlyPlayer;

    public GenericCommand(SubCommand command, boolean onlyPlayer) {
        this.aliases = Collections.singletonList(command.getName());
        this.command = command;
        this.onlyPlayer = onlyPlayer;
    }

    @Override
    public String getCommandName() {
        return command.getName();
    }

    @Override
    public List<String> getCommandAliases() {
        return aliases;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return command.addTabCompletionOptions(sender, args);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        try {
            command.processCommand(sender, args);
        } catch (CommandInvalidUsageException e) {
            GeneralUtils.sendFormattedText(sender, e.getMessage());
            GeneralUtils.sendFormattedTranslation(sender, "dipogtmapaddon.command.usage");
            for (String usage : command.getCommandUsages(e.getProcessedArgs())) {
                GeneralUtils.sendFormattedTranslation(sender, "dipogtmapaddon.command.usageLine", usage);
            }
        } catch (CommandException e) {
            GeneralUtils.sendFormattedText(sender, e.getMessage());
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return !onlyPlayer || sender instanceof EntityPlayer;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        StringBuilder builder = new StringBuilder("Usage:");
        for (String usage : command.getCommandUsages()) {
            builder.append("\n  /").append(usage);
        }

        return builder.toString();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return command.isUsernameIndex(args, index);
    }

    public int compareTo(ICommand command) {
        return getCommandName().compareTo(command.getCommandName());
    }

    @Override
    public int compareTo(@Nonnull Object object) {
        return compareTo((ICommand) object);
    }
}
