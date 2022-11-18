package io.github.dipo33.gtmapaddon.storage.serializable;

import net.minecraft.nbt.NBTTagCompound;

public interface Serializable<T> {
    NBTTagCompound serialize(T item);

    T deserialize(NBTTagCompound tag);
}
