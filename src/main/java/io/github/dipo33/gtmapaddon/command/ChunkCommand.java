package io.github.dipo33.gtmapaddon.command;

import com.sinthoras.visualprospecting.Utils;
import io.github.dipo33.gtmapaddon.GTMapAddonMod;
import io.github.dipo33.gtmapaddon.network.BoughtChunkMessage;
import io.github.dipo33.gtmapaddon.storage.DataCache;
import io.github.dipo33.gtmapaddon.storage.mined.MinedChunkSerializer;
import io.github.dipo33.gtmapaddon.storage.owned.OwnedChunk;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class ChunkCommand implements ICommand {

    private static final List<String> ALIASES = Collections.singletonList("chunk");

    @Override
    public String getCommandName() {
        return "chunk";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/chunk buy <build|farm|transport>";
    }

    @Override
    public List<String> getCommandAliases() {
        return ALIASES;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 2 || !args[0].equalsIgnoreCase("buy")) {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            return;
        }
        
        final ChunkCoordinates coordinates = sender.getPlayerCoordinates();
        final int chunkX = Utils.coordBlockToChunk(coordinates.posX);
        final int chunkZ = Utils.coordBlockToChunk(coordinates.posZ);
        final int dimensionId = sender.getEntityWorld().provider.dimensionId;
        final String owner = sender.getCommandSenderName();
        final OwnedChunk.Status status;
        
        if (args[1].equalsIgnoreCase("build")) {
            status = OwnedChunk.Status.BUILD;
        } else if (args[1].equalsIgnoreCase("farm")) {
            status = OwnedChunk.Status.FARM;
        } else if (args[1].equalsIgnoreCase("transport")) {
            status = OwnedChunk.Status.TRANSPORT;
        } else {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            return;
        }

        final OwnedChunk ownedChunk = new OwnedChunk(chunkX, chunkZ, dimensionId, owner, status);
        DataCache.OWNED_CHUNKS_STORAGE.getDimension(dimensionId).setElementAtChunk(chunkX, chunkZ, ownedChunk);
        GTMapAddonMod.NETWORK_CHANNEL.sendToAll(new BoughtChunkMessage(ownedChunk));
        MinedChunkSerializer.save();
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
        return false;
    }

    public int compareTo(ICommand command) {
        return getCommandName().compareTo(command.getCommandName());
    }

    @Override
    public int compareTo(@Nonnull Object object) {
        return compareTo((ICommand) object);
    }
}
