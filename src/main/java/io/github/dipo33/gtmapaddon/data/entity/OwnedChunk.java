package io.github.dipo33.gtmapaddon.data.entity;

import com.sinthoras.visualprospecting.Utils;
import io.github.dipo33.gtmapaddon.storage.serializable.SerializableChunkEntry;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class OwnedChunk implements SerializableChunkEntry<OwnedChunk> {

    public static final OwnedChunk INSTANCE = new OwnedChunk(0, 0, 0, null, null);

    private final int chunkX;
    private final int chunkZ;
    private final int dimensionId;
    private final String owner;
    private final Status status;

    public OwnedChunk(int chunkX, int chunkZ, int dimensionId, String owner, Status status) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.dimensionId = dimensionId;
        this.owner = owner;
        this.status = status;
    }

    public int getBlockX() {
        return Utils.coordChunkToBlock(chunkX) + 8;
    }

    public int getBlockZ() {
        return Utils.coordChunkToBlock(chunkZ) + 8;
    }

    public String getOwner() {
        return owner;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public int getChunkX() {
        return chunkX;
    }

    @Override
    public int getChunkZ() {
        return chunkZ;
    }

    @Override
    public int getDimensionId() {
        return dimensionId;
    }

    public enum Status {
        BUILD,
        FARM,
        TRANSPORT
    }

    private static class Keys {
        public static final String OWNER = "Owner";
        public static final String STATUS = "Status";
    }

    @Override
    public NBTBase serialize(OwnedChunk chunk) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger(Keys.STATUS, chunk.getStatus().ordinal());
        tag.setString(Keys.OWNER, chunk.getOwner());

        return tag;
    }

    @Override
    public OwnedChunk deserialize(NBTBase tagBase, int dimensionId, int chunkX, int chunkZ) {
        NBTTagCompound tag = (NBTTagCompound) tagBase;
        String owner = tag.getString(Keys.OWNER);
        OwnedChunk.Status status = OwnedChunk.Status.values()[tag.getInteger(Keys.STATUS)];

        return new OwnedChunk(chunkX, chunkZ, dimensionId, owner, status);
    }
}
