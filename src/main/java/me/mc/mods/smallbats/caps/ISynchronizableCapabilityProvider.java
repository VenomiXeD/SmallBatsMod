package me.mc.mods.smallbats.caps;

import me.mc.mods.smallbats.ModSmallBats;
import me.mc.mods.smallbats.network.PacketSynchronizeCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISynchronizableCapabilityProvider extends INBTSerializable<CompoundTag> {
    public Capability<?> cap();
}
