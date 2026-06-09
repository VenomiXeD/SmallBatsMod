package me.mc.mods.smallbats.caps;

import me.mc.mods.smallbats.network.PacketSynchronizeCapability;
import net.minecraft.world.entity.Entity;

public interface ISmallBatsPlayerCapability extends ISmallBatsBaseCapability {
    boolean getIsMist();

    void setIsMist(boolean mist);

    boolean getIsCeilingHanging();

    void setIsCeilingHanging(boolean isHanging);

    default void sync(Entity thisCapBelongsToThisEntityToSync) {
        PacketSynchronizeCapability.dispatchCapabilitySynchronization(this.serializerNBT(), this.getUnderlyingCapabilityProvider(), thisCapBelongsToThisEntityToSync);
    }
}
