package io.github.dipo33.gtmapaddon;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class GeneralUtils {
    public static void sendFormattedText(ICommandSender sender, String key, Object... args) {
        sender.addChatMessage(new ChatComponentText(I18n.format(key, args)));
    }
}
