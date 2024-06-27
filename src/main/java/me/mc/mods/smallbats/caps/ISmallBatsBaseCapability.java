package me.mc.mods.smallbats.caps;

import net.minecraft.nbt.CompoundTag;

public interface ISmallBatsBaseCapability {
    public CompoundTag serializerNBT();
    public void deserializeNBT(CompoundTag nbt);
    public Class getUnderlyingCapabilityProvider();
}
