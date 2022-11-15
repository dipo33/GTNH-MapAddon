package io.github.dipo33.gtmapaddon.command.factory;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

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

    private void sendRedMessage(ICommandSender sender, String message) {
        sender.addChatMessage(new ChatComponentText(message).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
    }

    public void sendInvalidUsage(ICommandSender sender, List<String> args, String error) {
        sendRedMessage(sender, error);
        sendRedMessage(sender, "Usage:");
        for (String usage : command.getCommandUsages(args.toArray(new String[]{}))) {
            sendRedMessage(sender, "    /" + usage);
        }
    }

    public abstract List<String> getCommandUsages();

    public abstract List<String> getCommandUsages(String[] args);

    public abstract List<String> addTabCompletionOptions(ICommandSender sender, String[] args);

    public abstract boolean isUsernameIndex(String[] args, int index);

    public abstract void processCommand(ICommandSender sender, String[] args, List<String> processedArgs);
}
