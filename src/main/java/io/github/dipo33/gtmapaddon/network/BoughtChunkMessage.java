package io.github.dipo33.gtmapaddon.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.github.dipo33.gtmapaddon.storage.DataCache;
import io.github.dipo33.gtmapaddon.storage.owned.OwnedChunk;
import io.netty.buffer.ByteBuf;

public class BoughtChunkMessage implements IMessage {

    private OwnedChunk ownedChunk;

    public BoughtChunkMessage() {
    }

    public BoughtChunkMessage(OwnedChunk ownedChunk) {
        this.ownedChunk = ownedChunk;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        final int chunkX = buf.readInt();
        final int chunkZ = buf.readInt();
        final int dimensionId = buf.readInt();
        final String owner = ByteBufUtils.readUTF8String(buf);
        final OwnedChunk.Status status = OwnedChunk.Status.values()[buf.readInt()];

        ownedChunk = new OwnedChunk(chunkX, chunkZ, dimensionId, owner, status);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(ownedChunk.getChunkX());
        buf.writeInt(ownedChunk.getChunkZ());
        buf.writeInt(ownedChunk.getDimensionId());
        ByteBufUtils.writeUTF8String(buf, ownedChunk.getOwner());
        buf.writeInt(ownedChunk.getStatus().ordinal());
    }

    public static final class Handler implements IMessageHandler<BoughtChunkMessage, IMessage> {

        @Override
        public IMessage onMessage(BoughtChunkMessage msg, MessageContext ctx) {
            final OwnedChunk chunk = msg.ownedChunk;
            DataCache.OWNED_CHUNKS_STORAGE.put(chunk.getDimensionId(), chunk.getChunkX(), chunk.getChunkZ(), chunk);

            return null;
        }
    }
}
