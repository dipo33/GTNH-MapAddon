package io.github.dipo33.gtmapaddon.command;

import com.sinthoras.visualprospecting.Utils;
import io.github.dipo33.gtmapaddon.GTMapAddonMod;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class MinedCommand implements ICommand {

    private static final List<String> ALIASES = Collections.singletonList("mined");

    @Override
    public String getCommandName() {
        return "mined";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "mined <username?>";
    }

    @Override
    public List<String> getCommandAliases() {
        return ALIASES;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        final ChunkCoordinates coordinates = sender.getPlayerCoordinates();
        final int chunkX = Utils.coordBlockToChunk(coordinates.posX);
        final int chunkZ = Utils.coordBlockToChunk(coordinates.posZ);

        GTMapAddonMod.info(String.format("Adding chunk (X: %d, Z: %d), isClientSide: %s", chunkX, chunkZ, sender.getEntityWorld().isRemote));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender instanceof EntityPlayer;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }

    public int compareTo(ICommand command) {
        return getCommandName().compareTo(command.getCommandName());
    }

    @Override
    public int compareTo(@Nonnull Object object) {
        return compareTo((ICommand) object);
    }
}
