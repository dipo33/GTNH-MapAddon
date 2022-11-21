package io.github.dipo33.gtmapaddon.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.github.dipo33.gtmapaddon.storage.DataCache;
import io.netty.buffer.ByteBuf;

public class RemoveMinedChunkClientMessage implements IMessage {

    private int chunkX;
    private int chunkZ;
    private int dimensionId;

    public RemoveMinedChunkClientMessage() {
    }

    public RemoveMinedChunkClientMessage(RemoveMinedChunkServerMessage message) {
        chunkX = message.getChunkX();
        chunkZ = message.getChunkZ();
        dimensionId = message.getDimensionId();
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

    public static final class Handler implements IMessageHandler<RemoveMinedChunkClientMessage, IMessage> {

        @Override
        public IMessage onMessage(RemoveMinedChunkClientMessage msg, MessageContext ctx) {
            DataCache.MINED_CHUNKS_STORAGE.remove(msg.dimensionId, msg.chunkX, msg.chunkZ);

            return null;
        }
    }
}
