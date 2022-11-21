package io.github.dipo33.gtmapaddon.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.github.dipo33.gtmapaddon.GTMapAddonMod;
import io.github.dipo33.gtmapaddon.render.journeymap.mined.MinedChunkLocationProvider;
import io.github.dipo33.gtmapaddon.storage.DataCache;
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
        public IMessage onMessage(RemoveMinedChunkServerMessage msg, MessageContext ctx) {
            DataCache.MINED_CHUNKS_STORAGE.remove(msg.dimensionId, msg.chunkX, msg.chunkZ);
            DataCache.MINED_CHUNKS_SERIALIZER.save();
            GTMapAddonMod.NETWORK_CHANNEL.sendToAll(new RemoveMinedChunkClientMessage(msg));

            return null;
        }
    }
}
