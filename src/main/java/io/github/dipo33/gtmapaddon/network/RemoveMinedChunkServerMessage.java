package io.github.dipo33.gtmapaddon.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.github.dipo33.gtmapaddon.GTMapAddonMod;
import io.github.dipo33.gtmapaddon.command.MinedCommand;
import io.github.dipo33.gtmapaddon.render.journeymap.mined.MinedChunkLocationProvider;
import io.netty.buffer.ByteBuf;

public class RemoveMinedChunkServerMessage implements IMessage {

    private int chunkX;
    private int chunkZ;
    private int dimensionId;

    public RemoveMinedChunkServerMessage() {
    }

    public RemoveMinedChunkServerMessage(MinedChunkLocationProvider minedChunk) {
        chunkX = minedChunk.getChunkX();
        chunkZ = minedChunk.getChunkZ();
        dimensionId = minedChunk.getDimensionId();
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public int getDimensionId() {
        return dimensionId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        chunkX = buf.readInt();
        chunkZ = buf.readInt();
        dimensionId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(chunkX);
        buf.writeInt(chunkZ);
        buf.writeInt(dimensionId);
    }

    public static final class Handler implements IMessageHandler<RemoveMinedChunkServerMessage, IMessage> {

        @Override
        public IMessage onMessage(RemoveMinedChunkServerMessage message, MessageContext ctx) {
//            MinedCommand.MINED_CHUNKS_STORAGE.getDimension(message.dimensionId)
//                    .removeElementAtChunk(message.chunkX, message.chunkZ);
            GTMapAddonMod.NETWORK_CHANNEL.sendToAll(new RemoveMinedChunkClientMessage(message));

            return null;
        }
    }
}
