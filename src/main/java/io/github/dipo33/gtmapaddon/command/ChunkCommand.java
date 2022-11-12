package io.github.dipo33.gtmapaddon.command;

import com.sinthoras.visualprospecting.Utils;
import io.github.dipo33.gtmapaddon.GTMapAddonMod;
import io.github.dipo33.gtmapaddon.GeneralUtils;
import io.github.dipo33.gtmapaddon.command.factory.CommandFactory;
import io.github.dipo33.gtmapaddon.command.factory.SubCommandFactory;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgumentList;
import io.github.dipo33.gtmapaddon.network.AddMinedChunkMessage;
import io.github.dipo33.gtmapaddon.network.BoughtChunkMessage;
import io.github.dipo33.gtmapaddon.storage.DataCache;
import io.github.dipo33.gtmapaddon.storage.mined.MinedChunk;
import io.github.dipo33.gtmapaddon.storage.owned.OwnedChunk;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChunkCoordinates;

public class ChunkCommand {

    public static final ICommand command = CommandFactory.createCommand()
                .onlyPlayerUse()
                .addSubCommand(SubCommandFactory
                            .createSubCommand("chunk")
                            .addSubCommand(SubCommandFactory
                                        .createSubCommand("buy")
                                        .addEnumArgument("type", OwnedChunk.Status.class).build()
                                        .build(ChunkCommand::buyCommand)
                            ).addSubCommand(SubCommandFactory
                                        .createSubCommand("mark-as-mined")
                                        .addOfflinePlayerArgument("mined-by").defaultsToSender().build()
                                        .build(ChunkCommand::markAsMined)
                            ).build()
                );

    private static void buyCommand(ArgumentList arguments, ICommandSender sender) {
        final ChunkCoordinates coordinates = sender.getPlayerCoordinates();
        final int chunkX = Utils.coordBlockToChunk(coordinates.posX);
        final int chunkZ = Utils.coordBlockToChunk(coordinates.posZ);
        final int dimensionId = sender.getEntityWorld().provider.dimensionId;
        final OwnedChunk.Status status = arguments.getEnum(0);
        final String owner = sender.getCommandSenderName();
        
        final OwnedChunk ownedChunk = new OwnedChunk(chunkX, chunkZ, dimensionId, owner, status);
        DataCache.OWNED_CHUNKS_STORAGE.put(dimensionId, chunkX, chunkZ, ownedChunk);
        GTMapAddonMod.NETWORK_CHANNEL.sendToAll(new BoughtChunkMessage(ownedChunk));
        DataCache.OWNED_CHUNKS_SERIALIZER.save();

        GeneralUtils.sendFormattedText(sender, "dipogtmapaddon.command.chunkBought", status.name().toLowerCase(), chunkX, chunkZ, 100);
    }

    private static void markAsMined(ArgumentList arguments, ICommandSender sender) {
        final ChunkCoordinates coordinates = sender.getPlayerCoordinates();
        final int chunkX = Utils.coordBlockToChunk(coordinates.posX);
        final int chunkZ = Utils.coordBlockToChunk(coordinates.posZ);
        final int dimensionId = sender.getEntityWorld().provider.dimensionId;
        final String minedBy = arguments.getOfflinePlayer(0);

        final MinedChunk minedChunk = new MinedChunk(chunkX, chunkZ, dimensionId, minedBy);
        DataCache.MINED_CHUNKS_STORAGE.put(dimensionId, chunkX, chunkZ, minedChunk);
        GTMapAddonMod.NETWORK_CHANNEL.sendToAll(new AddMinedChunkMessage(minedChunk));
        DataCache.MINED_CHUNKS_SERIALIZER.save();
        
        GeneralUtils.sendFormattedText(sender, "dipogtmapaddon.command.chunkMarked", chunkX, chunkZ, minedBy);
    }
}
