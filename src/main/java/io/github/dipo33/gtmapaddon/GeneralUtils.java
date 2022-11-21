package io.github.dipo33.gtmapaddon;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class GeneralUtils {
    public static void sendFormattedText(ICommandSender sender, String message) {
        sender.addChatMessage(formatText(message));
    }

    public static void sendFormattedTranslation(ICommandSender sender, String key, Object... args) {
        sender.addChatMessage(formatText(I18n.format(key, args)));
    }

    public static void sendCommandMessage(ICommandSender sender, String key, Object... args) {
        sender.addChatMessage(formatText(I18n.format("dipogtmapaddon.command." + key, args)));
    }

    public static IChatComponent formatText(String message) {
        ChatComponentText text = new ChatComponentText("");
        String[] chunks = message.split("(?=\u00a7)");
        for (String chunk : chunks) {
            if (chunk.startsWith("\u00a7")) {
                ChatStyle style = new ChatStyle().setColor(getColorFromChar(chunk.charAt(1)));
                text.appendSibling(new ChatComponentText(chunk.substring(2)).setChatStyle(style));
            } else {
                text.appendSibling(new ChatComponentText(chunk));
            }
        }

        return text;
    }

    public static EnumChatFormatting getColorFromChar(char c) {
        switch (c) {
            case '0':
                return EnumChatFormatting.BLACK;
            case '1':
                return EnumChatFormatting.DARK_BLUE;
            case '2':
                return EnumChatFormatting.DARK_GREEN;
            case '3':
                return EnumChatFormatting.DARK_AQUA;
            case '4':
                return EnumChatFormatting.DARK_RED;
            case '5':
                return EnumChatFormatting.DARK_PURPLE;
            case '6':
                return EnumChatFormatting.GOLD;
            case '7':
                return EnumChatFormatting.GRAY;
            case '8':
                return EnumChatFormatting.DARK_GRAY;
            case '9':
                return EnumChatFormatting.BLUE;
            case 'a':
                return EnumChatFormatting.GREEN;
            case 'b':
                return EnumChatFormatting.AQUA;
            case 'c':
                return EnumChatFormatting.RED;
            case 'd':
                return EnumChatFormatting.LIGHT_PURPLE;
            case 'e':
                return EnumChatFormatting.YELLOW;
            case 'f':
                return EnumChatFormatting.WHITE;
            case 'k':
                return EnumChatFormatting.OBFUSCATED;
            case 'l':
                return EnumChatFormatting.BOLD;
            case 'm':
                return EnumChatFormatting.STRIKETHROUGH;
            case 'n':
                return EnumChatFormatting.UNDERLINE;
            case 'o':
                return EnumChatFormatting.ITALIC;
            case 'r':
                return EnumChatFormatting.RESET;
            default:
                return null;
        }
    }
}
