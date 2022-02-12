package io.github.dipo33.gtmapaddon;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.relauncher.Side;
import io.github.dipo33.gtmapaddon.command.MinedCommand;
import io.github.dipo33.gtmapaddon.network.AddMinedChunkMessage;
import io.github.dipo33.gtmapaddon.network.RemoveMinedChunkClientMessage;
import io.github.dipo33.gtmapaddon.network.RemoveMinedChunkServerMessage;
import io.github.dipo33.gtmapaddon.storage.MinedChunkSerializer;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
        GTMapAddonMod.NETWORK_CHANNEL.registerMessage(AddMinedChunkMessage.Handler.class, AddMinedChunkMessage.class, 0, Side.CLIENT);
        GTMapAddonMod.NETWORK_CHANNEL.registerMessage(RemoveMinedChunkServerMessage.Handler.class, RemoveMinedChunkServerMessage.class, 1, Side.SERVER);
        GTMapAddonMod.NETWORK_CHANNEL.registerMessage(RemoveMinedChunkClientMessage.Handler.class, RemoveMinedChunkClientMessage.class, 2, Side.CLIENT);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes."
    public void init(FMLInitializationEvent event) {
    }

    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void postInit(FMLPostInitializationEvent event) {
    }

    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
    }

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new MinedCommand());
        MinedChunkSerializer.read();
    }

    public void serverStarted(FMLServerStartedEvent event) {
    }

    public void serverStopping(FMLServerStoppingEvent event) {
        MinedChunkSerializer.save();
    }

    public void serverStopped(FMLServerStoppedEvent event) {
    }
}
