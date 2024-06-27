package me.mc.mods.smallbats.caps;

import me.mc.mods.smallbats.network.PacketSynchronizeCapability;
import net.minecraft.world.entity.Entity;

public interface ISmallBatsPlayerCapability extends ISmallBatsBaseCapability {
    public boolean getIsMist();
    public void setIsMist(boolean mist);

    public default void sync(Entity thisCapBelongsToThisEntityToSync) {
        PacketSynchronizeCapability.dispatchCapabilitySynchronization(this.serializerNBT(), this.getUnderlyingCapabilityProvider(), thisCapBelongsToThisEntityToSync);
    }
}
