package io.github.dipo33.gtmapaddon.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.github.dipo33.gtmapaddon.storage.ChunkStorage;
import io.github.dipo33.gtmapaddon.storage.DataCache;
import io.github.dipo33.gtmapaddon.storage.DimensionStorage;
import io.github.dipo33.gtmapaddon.storage.mined.MinedChunk;
import io.netty.buffer.ByteBuf;

public class SyncMinedChunksMessage implements IMessage {

    private DimensionStorage<MinedChunk> dimensionStorage;

    public SyncMinedChunksMessage() {
    }

    public SyncMinedChunksMessage(DimensionStorage<MinedChunk> dimensionStorage) {
        this.dimensionStorage = dimensionStorage;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimensionStorage = new DimensionStorage<>();
        while (buf.readBoolean()) {
            final int chunkX = buf.readInt();
            final int chunkZ = buf.readInt();
            final int dimensionId = buf.readInt();
            final String minedBy = ByteBufUtils.readUTF8String(buf);
            final MinedChunk minedChunk = new MinedChunk(chunkX, chunkZ, dimensionId, minedBy);

            dimensionStorage.getDimension(dimensionId).setElementAtChunk(chunkX, chunkZ, minedChunk);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        for (ChunkStorage<MinedChunk> chunkStorage : dimensionStorage.getAll()) {
            for (MinedChunk minedChunk : chunkStorage.getAll()) {
                buf.writeBoolean(true);
                buf.writeInt(minedChunk.getChunkX());
                buf.writeInt(minedChunk.getChunkZ());
                buf.writeInt(minedChunk.getDimensionId());
                ByteBufUtils.writeUTF8String(buf, minedChunk.getMinedBy());
            }
        }

        buf.writeBoolean(false);
    }

    public static final class Handler implements IMessageHandler<SyncMinedChunksMessage, IMessage> {

        @Override
        public IMessage onMessage(SyncMinedChunksMessage message, MessageContext ctx) {
            DataCache.MINED_CHUNKS_STORAGE = message.dimensionStorage;

            return null;
        }
    }
}
