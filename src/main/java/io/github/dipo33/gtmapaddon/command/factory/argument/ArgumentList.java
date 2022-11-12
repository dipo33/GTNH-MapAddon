package io.github.dipo33.gtmapaddon.command.factory.argument;

import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.List;

public class ArgumentList {
    private final List<Argument<?>> arguments;

    public ArgumentList(List<Argument<?>> arguments) {
        this.arguments = new ArrayList<>(arguments);
    }

    public int getInt(int index) {
        return ((ArgInt) arguments.get(index)).get();
    }

    public <E extends Enum<E>> E getEnum(int index) {
        //noinspection unchecked
        return (E) ((ArgEnum<E>) arguments.get(index)).get();
    }

    public EntityPlayerMP getPlayer(int index) {
        return ((ArgPlayer) arguments.get(index)).get();
    }
    
    public String getOfflinePlayer(int index) {
        return ((ArgOfflinePlayer) arguments.get(index)).get();
    }
    
    public String getString(int index) {
        return ((ArgString) arguments.get(index)).get();
    }
}
