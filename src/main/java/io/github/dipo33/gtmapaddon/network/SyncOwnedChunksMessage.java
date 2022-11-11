package io.github.dipo33.gtmapaddon.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.github.dipo33.gtmapaddon.storage.ChunkStorage;
import io.github.dipo33.gtmapaddon.storage.DataCache;
import io.github.dipo33.gtmapaddon.storage.DimensionStorage;
import io.github.dipo33.gtmapaddon.storage.owned.OwnedChunk;
import io.netty.buffer.ByteBuf;

public class SyncOwnedChunksMessage implements IMessage {

    private DimensionStorage<OwnedChunk> dimensionStorage;

    public SyncOwnedChunksMessage() {
    }

    public SyncOwnedChunksMessage(DimensionStorage<OwnedChunk> dimensionStorage) {
        this.dimensionStorage = dimensionStorage;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimensionStorage = new DimensionStorage<>();
        while (buf.readBoolean()) {
            final int chunkX = buf.readInt();
            final int chunkZ = buf.readInt();
            final int dimensionId = buf.readInt();
            final OwnedChunk.Status status = OwnedChunk.Status.values()[buf.readInt()];
            final String owner = ByteBufUtils.readUTF8String(buf);
            
            final OwnedChunk ownedChunk = new OwnedChunk(chunkX, chunkZ, dimensionId, owner, status);
            dimensionStorage.getDimension(dimensionId).setElementAtChunk(chunkX, chunkZ, ownedChunk);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        for (ChunkStorage<OwnedChunk> chunkStorage : dimensionStorage.getAll()) {
            for (OwnedChunk ownedChunk : chunkStorage.getAll()) {
                buf.writeBoolean(true);
                buf.writeInt(ownedChunk.getChunkX());
                buf.writeInt(ownedChunk.getChunkZ());
                buf.writeInt(ownedChunk.getDimensionId());
                buf.writeInt(ownedChunk.getStatus().ordinal());
                ByteBufUtils.writeUTF8String(buf, ownedChunk.getOwner());
            }
        }

        buf.writeBoolean(false);
    }

    public static final class Handler implements IMessageHandler<SyncOwnedChunksMessage, IMessage> {

        @Override
        public IMessage onMessage(SyncOwnedChunksMessage message, MessageContext ctx) {
            DataCache.OWNED_CHUNKS_STORAGE = message.dimensionStorage;

            return null;
        }
    }
}
