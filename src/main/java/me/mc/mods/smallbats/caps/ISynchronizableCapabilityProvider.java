package me.mc.mods.smallbats.caps;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISynchronizableCapabilityProvider extends INBTSerializable<CompoundTag> {
    public Capability<?> cap();
}
