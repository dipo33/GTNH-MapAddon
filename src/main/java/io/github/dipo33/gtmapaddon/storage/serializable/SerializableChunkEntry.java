package io.github.dipo33.gtmapaddon.storage.serializable;

import net.minecraft.nbt.NBTBase;

public interface SerializableChunkEntry<T> {
    NBTBase serialize(T item);

    T deserialize(NBTBase tag, int dimensionId, int chunkX, int chunkZ);
    
    int getChunkX();
    
    int getChunkZ();
    
    int getDimensionId();
}
