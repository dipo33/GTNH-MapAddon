package io.github.dipo33.gtmapaddon.storage.serializable;

import net.minecraft.nbt.NBTBase;

public interface Serializable<T> {
    NBTBase serialize(T item);

    T deserialize(NBTBase tag);
}
