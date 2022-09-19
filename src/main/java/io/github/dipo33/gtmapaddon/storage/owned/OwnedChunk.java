package io.github.dipo33.gtmapaddon.storage.owned;

import com.sinthoras.visualprospecting.Utils;

public class OwnedChunk {

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

    public int getChunkX() {
        return chunkX;
    }

    public int getBlockX() {
        return Utils.coordChunkToBlock(chunkX) + 8;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public int getBlockZ() {
        return Utils.coordChunkToBlock(chunkZ) + 8;
    }

    public int getDimensionId() {
        return dimensionId;
    }

    public String getOwner() {
        return owner;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        BUILD,
        FARM,
        TRANSPORT
    }
}
