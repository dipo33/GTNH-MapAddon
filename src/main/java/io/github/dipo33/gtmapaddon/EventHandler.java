package io.github.dipo33.gtmapaddon;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import io.github.dipo33.gtmapaddon.network.SyncMinedChunksMessage;
import io.github.dipo33.gtmapaddon.network.SyncOwnedChunksMessage;
import io.github.dipo33.gtmapaddon.storage.DataCache;
import net.minecraft.entity.player.EntityPlayerMP;

public class EventHandler {

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        final SyncMinedChunksMessage minedChunksMessage = new SyncMinedChunksMessage(DataCache.MINED_CHUNKS_STORAGE);
        final SyncOwnedChunksMessage ownedChunksMessage = new SyncOwnedChunksMessage(DataCache.OWNED_CHUNKS_STORAGE);
        
        GTMapAddonMod.NETWORK_CHANNEL.sendTo(minedChunksMessage, (EntityPlayerMP) event.player);
        GTMapAddonMod.NETWORK_CHANNEL.sendTo(ownedChunksMessage, (EntityPlayerMP) event.player);
    }
}
