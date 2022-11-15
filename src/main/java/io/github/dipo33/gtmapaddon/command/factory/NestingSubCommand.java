package io.github.dipo33.gtmapaddon.command.factory;

import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NestingSubCommand extends SubCommand {

    private final List<SubCommand> subCommands;

    public NestingSubCommand(String name, List<SubCommand> subCommands) {
        super(name);
        this.subCommands = new ArrayList<>(subCommands);
    }

    @Override
    public void setMainCommand(SubCommand command) {
        this.command = command;
        for (SubCommand subCommand : subCommands) {
            subCommand.setMainCommand(command);
        }
    }

    @Override
    public List<String> getCommandUsages() {
        List<String> usages = new ArrayList<>();
        for (SubCommand subCommand : subCommands) {
            for (String usage : subCommand.getCommandUsages()) {
                usages.add(getName() + " " + usage);
            }
        }

        return usages;
    }

    @Override
    public List<String> getCommandUsages(String[] args) {
        if (args.length == 0) {
            return getCommandUsages();
        }

        List<String> usages = new ArrayList<>();
        for (SubCommand subCommand : subCommands) {
            if (subCommand.getName().equalsIgnoreCase(args[0])) {
                final List<String> subUsages = subCommand.getCommandUsages(Arrays.copyOfRange(args, 1, args.length));
                for (String usage : subUsages) {
                    usages.add(getName() + " " + usage);
                }
            }
        }

        return usages;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length > 1) {
            for (SubCommand subCommand : subCommands) {
                if (subCommand.getName().equalsIgnoreCase(args[0])) {
                    return subCommand.addTabCompletionOptions(sender, Arrays.copyOfRange(args, 1, args.length));
                }
            }
        } else  {
            String cmd = args.length == 0 ? "" : args[0].toLowerCase();
            List<String> completions = new ArrayList<>();

            for (SubCommand subCommand : subCommands) {
                if (subCommand.getName().startsWith(cmd)) {
                    completions.add(subCommand.getName());
                }
            }

            return completions;
        }
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        if (index == 0) {
            return false;
        }

        for (SubCommand subCommand : subCommands) {
            if (subCommand.getName().equalsIgnoreCase(args[0])) {
                return isUsernameIndex(Arrays.copyOfRange(args, 1, args.length), index - 1);
            }
        }

        return false;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args, List<String> processedArgs) {
        if (args.length < 1) {
            sendInvalidUsage(sender, processedArgs, "Error: Not enough arguments");
            return;
        }

        for (SubCommand subCommand : subCommands) {
            if (subCommand.getName().equalsIgnoreCase(args[0])) {
                processedArgs.add(args[0]);
                subCommand.processCommand(sender, Arrays.copyOfRange(args, 1, args.length), processedArgs);
                return;
            }
        }

        sendInvalidUsage(sender, processedArgs, "Error: Invalid sub-command");
    }
}
