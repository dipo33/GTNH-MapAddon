package io.github.dipo33.gtmapaddon.command.factory.argument;

import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.List;

public class ArgumentList {
    private final List<Object> arguments;

    public ArgumentList() {
        this.arguments = new ArrayList<>();
    }

    public void append(Object argument) {
        arguments.add(argument);
    }

    public int getInt(int index) {
        return (int) arguments.get(index);
    }

    public <E extends Enum<E>> E getEnum(int index) {
        //noinspection unchecked
        return (E) arguments.get(index);
    }

    public EntityPlayerMP getPlayer(int index) {
        return (EntityPlayerMP) arguments.get(index);
    }

    public String getOfflinePlayer(int index) {
        return (String) arguments.get(index);
    }

    public String getString(int index) {
        return (String) arguments.get(index);
    }
}
