package io.github.dipo33.gtmapaddon.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.github.dipo33.gtmapaddon.storage.DataCache;
import io.github.dipo33.gtmapaddon.storage.mined.MinedChunk;
import io.netty.buffer.ByteBuf;

public class AddMinedChunkMessage implements IMessage {

    private MinedChunk minedChunk;

    public AddMinedChunkMessage() {
    }

    public AddMinedChunkMessage(MinedChunk minedChunk) {
        this.minedChunk = minedChunk;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        final int chunkX = buf.readInt();
        final int chunkZ = buf.readInt();
        final int dimensionId = buf.readInt();
        final String minedBy = ByteBufUtils.readUTF8String(buf);

        minedChunk = new MinedChunk(chunkX, chunkZ, dimensionId, minedBy);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(minedChunk.getChunkX());
        buf.writeInt(minedChunk.getChunkZ());
        buf.writeInt(minedChunk.getDimensionId());
        ByteBufUtils.writeUTF8String(buf, minedChunk.getMinedBy());
    }

    public static final class Handler implements IMessageHandler<AddMinedChunkMessage, IMessage> {

        @Override
        public IMessage onMessage(AddMinedChunkMessage message, MessageContext ctx) {
            final MinedChunk minedChunk = message.minedChunk;
            DataCache.MINED_CHUNKS_STORAGE.getDimension(minedChunk.getDimensionId())
                    .setElementAtChunk(minedChunk.getChunkX(), minedChunk.getChunkZ(), minedChunk);

            return null;
        }
    }
}
