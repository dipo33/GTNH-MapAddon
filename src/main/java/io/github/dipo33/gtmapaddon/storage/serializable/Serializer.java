package io.github.dipo33.gtmapaddon.storage.serializable;

import io.github.dipo33.gtmapaddon.GTMapAddonMod;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Serializer<T extends Serializable<T>> {
    private final String name;
    private final Supplier<T> constructor;
    private final Supplier<T> getter;
    private final Consumer<T> setter;

    public Serializer(String name, Supplier<T> constructor, Supplier<T> getter, Consumer<T> setter) {
        this.name = name;
        this.constructor = constructor;
        this.getter = getter;
        this.setter = setter;
    }

    public void save() {
        final File file = GTMapAddonMod.getCurrentSaveModFile(name + ".dat");

        try {
            T instance = getter.get();
            CompressedStreamTools.safeWrite((NBTTagCompound) instance.serialize(instance), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read() {
        final File file = GTMapAddonMod.getCurrentSaveModFile(name + ".dat");
        T instance = constructor.get();

        try {
            if (file.exists()) {
                setter.accept(instance.deserialize(CompressedStreamTools.read(file)));
            } else {
                setter.accept(instance);
            }
        } catch (IOException e) {
            setter.accept(instance);
        }
    }
}
