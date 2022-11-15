package io.github.dipo33.gtmapaddon.render.journeymap.owned;

import com.sinthoras.visualprospecting.integration.model.locations.IWaypointAndLocationProvider;
import com.sinthoras.visualprospecting.integration.model.waypoints.Waypoint;
import io.github.dipo33.gtmapaddon.Config;
import io.github.dipo33.gtmapaddon.Reference;
import io.github.dipo33.gtmapaddon.data.entity.OwnedChunk;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

public class OwnedChunkLocationProvider implements IWaypointAndLocationProvider {

    private final OwnedChunk ownedChunk;
    private final int color;

    private boolean isActiveAsWaypoint;

    public OwnedChunkLocationProvider(OwnedChunk ownedChunk) {
        this.ownedChunk = ownedChunk;
        this.color = Config.getColorForUser(ownedChunk.getOwner());
    }

    @Override
    public int getDimensionId() {
        return ownedChunk.getDimensionId();
    }

    @Override
    public double getBlockX() {
        return ownedChunk.getBlockX() + 0.5;
    }

    @Override
    public double getBlockZ() {
        return ownedChunk.getBlockZ() + 0.5;
    }

    public int getChunkX() {
        return ownedChunk.getChunkX();
    }

    public int getChunkZ() {
        return ownedChunk.getChunkZ();
    }

    public String getOwner() {
        return ownedChunk.getOwner();
    }

    public OwnedChunk.Status getStatus() {
        return ownedChunk.getStatus();
    }

    public int getColor() {
        return color;
    }

    @Override
    public Waypoint toWaypoint() {
        return new Waypoint(ownedChunk.getBlockX(), 96, ownedChunk.getBlockZ(), ownedChunk.getDimensionId(),
                I18n.format(Reference.MODID + ".tracked", ownedChunk.getChunkX(), ownedChunk.getChunkZ()),
                getColor());
    }

    @Override
    public boolean isActiveAsWaypoint() {
        return isActiveAsWaypoint;
    }

    @Override
    public void onWaypointCleared() {
        isActiveAsWaypoint = false;
    }

    @Override
    public void onWaypointUpdated(Waypoint waypoint) {
        isActiveAsWaypoint = waypoint.dimensionId == ownedChunk.getDimensionId()
                && waypoint.blockX == ownedChunk.getBlockX()
                && waypoint.blockZ == ownedChunk.getBlockZ();
    }

    public String getMainHint() {
        return I18n.format(Reference.MODID + "." + getStatus().name());
    }

    public String getOwnerHint() {
        return I18n.format(Reference.MODID + ".owned_by", EnumChatFormatting.GOLD + getOwner());
    }
}
