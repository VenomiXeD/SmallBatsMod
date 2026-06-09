package me.mc.mods.smallbats.caps;

import net.minecraft.nbt.CompoundTag;

public interface ISmallBatsBaseCapability {
    CompoundTag serializerNBT();

    void deserializeNBT(CompoundTag nbt);

    Class getUnderlyingCapabilityProvider();
}
