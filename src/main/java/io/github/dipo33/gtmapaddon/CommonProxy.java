package io.github.dipo33.gtmapaddon;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.relauncher.Side;
import io.github.dipo33.gtmapaddon.command.ChunkCommand;
import io.github.dipo33.gtmapaddon.command.MinedCommand;
import io.github.dipo33.gtmapaddon.network.AddMinedChunkMessage;
import io.github.dipo33.gtmapaddon.network.BoughtChunkMessage;
import io.github.dipo33.gtmapaddon.network.RemoveMinedChunkClientMessage;
import io.github.dipo33.gtmapaddon.network.RemoveMinedChunkServerMessage;
import io.github.dipo33.gtmapaddon.network.SyncMinedChunksMessage;
import io.github.dipo33.gtmapaddon.network.SyncOwnedChunksMessage;
import io.github.dipo33.gtmapaddon.storage.mined.MinedChunkSerializer;
import io.github.dipo33.gtmapaddon.storage.owned.OwnedChunkSerializer;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
        GTMapAddonMod.NETWORK_CHANNEL.registerMessage(AddMinedChunkMessage.Handler.class, AddMinedChunkMessage.class, 0, Side.CLIENT);
        GTMapAddonMod.NETWORK_CHANNEL.registerMessage(RemoveMinedChunkServerMessage.Handler.class, RemoveMinedChunkServerMessage.class, 1, Side.SERVER);
        GTMapAddonMod.NETWORK_CHANNEL.registerMessage(RemoveMinedChunkClientMessage.Handler.class, RemoveMinedChunkClientMessage.class, 2, Side.CLIENT);
        GTMapAddonMod.NETWORK_CHANNEL.registerMessage(SyncMinedChunksMessage.Handler.class, SyncMinedChunksMessage.class, 3, Side.CLIENT);

        GTMapAddonMod.NETWORK_CHANNEL.registerMessage(BoughtChunkMessage.Handler.class, BoughtChunkMessage.class, 4, Side.CLIENT);
        GTMapAddonMod.NETWORK_CHANNEL.registerMessage(SyncOwnedChunksMessage.Handler.class, SyncOwnedChunksMessage.class, 5, Side.CLIENT);
    }

    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new EventHandler());
    }

    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void postInit(FMLPostInitializationEvent event) {
    }

    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
    }

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new MinedCommand());
        event.registerServerCommand(new ChunkCommand());
        MinedChunkSerializer.read();
        OwnedChunkSerializer.read();
    }

    public void serverStarted(FMLServerStartedEvent event) {
    }

    public void serverStopping(FMLServerStoppingEvent event) {
        MinedChunkSerializer.save();
        OwnedChunkSerializer.save();
    }

    public void serverStopped(FMLServerStoppedEvent event) {
    }
}
