package io.github.dipo33.gtmapaddon.command.factory.subcommand;

import io.github.dipo33.gtmapaddon.command.factory.argument.ArgPlayer;
import io.github.dipo33.gtmapaddon.command.factory.argument.Argument;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgumentList;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class ArgumentalSubcommand extends SubCommand {

    private final List<Argument<?>> arguments;
    private final BiConsumer<ArgumentList, ICommandSender> processor;

    public ArgumentalSubcommand(String name, List<Argument<?>> arguments, BiConsumer<ArgumentList, ICommandSender> processor) {
        super(name);
        this.arguments = new ArrayList<>(arguments);
        this.processor = processor;
    }

    @Override
    public void setMainCommand(SubCommand command) {
        this.command = command;
    }

    @Override
    public List<String> getCommandUsages() {
        StringBuilder builder = new StringBuilder(getName());
        for (Argument<?> argument : arguments) {
            builder.append(" ").append(argument.getUsage());
        }

        return Collections.singletonList(builder.toString());
    }

    @Override
    public List<String> getCommandUsages(String[] args) {
        return getCommandUsages();
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length > arguments.size()) {
            return Collections.emptyList();
        }

        Argument<?> arg = arguments.get(args.length - 1);
        return arg.getTabCompletionOptions(sender, args[args.length - 1]);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        if (arguments.size() <= index) {
            return false;
        }

        return arguments.get(index) instanceof ArgPlayer;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args, List<String> processedArgs) {
        ArgumentList argumentList = new ArgumentList();

        for (int i = 0; i < arguments.size(); ++i) {
            Argument<?> argument = arguments.get(i);
            if (args.length <= i && argument.isRequired()) {
                sendInvalidUsage(sender, processedArgs, "Error: Not enough arguments");
                return;
            } else {
                boolean success = args.length <= i ?
                            argument.fillDefaults(sender) : argument.fill(args[i], sender);
                if (!success) {
                    sendInvalidUsage(sender, processedArgs, "Error: " + argument.getError());
                    return;
                }

                argumentList.append(argument.get());
            }
        }

        processor.accept(argumentList, sender);
    }
}
