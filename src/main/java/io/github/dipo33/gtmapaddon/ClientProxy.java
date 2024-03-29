package io.github.dipo33.gtmapaddon;

import com.sinthoras.visualprospecting.VisualProspecting_API;
import com.sinthoras.visualprospecting.integration.journeymap.buttons.LayerButton;
import com.sinthoras.visualprospecting.integration.model.buttons.ButtonManager;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import io.github.dipo33.gtmapaddon.render.journeymap.mined.MinedChunkLayerManager;
import io.github.dipo33.gtmapaddon.render.journeymap.mined.MinedChunkLayerRenderer;
import io.github.dipo33.gtmapaddon.render.journeymap.owned.OwnedChunkLayerManager;
import io.github.dipo33.gtmapaddon.render.journeymap.owned.OwnedChunkLayerRenderer;

public class ClientProxy extends CommonProxy {

    public static final ButtonManager MINED_CHUNKS_BUTTON_MANAGER = new ButtonManager(Reference.MODID + ".button.minedChunks", "iconMinedChunks");
    public static final ButtonManager OWNED_CHUNKS_BUTTON_MANAGER = new ButtonManager(Reference.MODID + ".button.ownedChunks", "iconOwnedChunks");
    public static final LayerButton MINED_CHUNKS_BUTTON = new LayerButton(MINED_CHUNKS_BUTTON_MANAGER);
    public static final LayerButton OWNED_CHUNKS_BUTTON = new LayerButton(OWNED_CHUNKS_BUTTON_MANAGER);

    // preInit "Run before anything else. Read your config, create blocks, items,
    // etc, and register them with the GameRegistry."
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes."
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        VisualProspecting_API.LogicalClient.registerCustomButtonManager(MINED_CHUNKS_BUTTON_MANAGER);
        VisualProspecting_API.LogicalClient.registerCustomButtonManager(OWNED_CHUNKS_BUTTON_MANAGER);
        VisualProspecting_API.LogicalClient.registerJourneyMapButton(MINED_CHUNKS_BUTTON);
        VisualProspecting_API.LogicalClient.registerJourneyMapButton(OWNED_CHUNKS_BUTTON);

        VisualProspecting_API.LogicalClient.registerCustomLayer(MinedChunkLayerManager.instance);
        VisualProspecting_API.LogicalClient.registerCustomLayer(OwnedChunkLayerManager.instance);
        VisualProspecting_API.LogicalClient.registerJourneyMapRenderer(new MinedChunkLayerRenderer());
        VisualProspecting_API.LogicalClient.registerJourneyMapRenderer(new OwnedChunkLayerRenderer());
    }

    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        super.serverAboutToStart(event);
    }

    // register server commands in this event handler
    public void serverStarting(FMLServerStartingEvent event) {
        super.serverStarting(event);
    }

    public void serverStarted(FMLServerStartedEvent event) {
        super.serverStarted(event);
    }

    public void serverStopping(FMLServerStoppingEvent event) {
        super.serverStopping(event);
    }

    public void serverStopped(FMLServerStoppedEvent event) {
        super.serverStopped(event);
    }
}
