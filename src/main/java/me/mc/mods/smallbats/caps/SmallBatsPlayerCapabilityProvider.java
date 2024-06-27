package me.mc.mods.smallbats.caps;

import me.mc.mods.smallbats.ModSmallBats;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmallBatsPlayerCapabilityProvider implements ISynchronizableCapabilityProvider, ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<ISmallBatsPlayerCapability> SMALLBATS_PLAYER_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    public Capability<ISmallBatsPlayerCapability> cap() {
        return SMALLBATS_PLAYER_CAP;
    }

    public static final ResourceLocation SMALLBATS_PLAYER_CAP_LOC = new ResourceLocation(ModSmallBats.MODID,"cap");
    private ISmallBatsPlayerCapability INSTANCE;
    private final LazyOptional<ISmallBatsPlayerCapability> opt = LazyOptional.of(this::getInstance);
    private ISmallBatsPlayerCapability getInstance() {
        if (INSTANCE == null) INSTANCE = new SmallBatsPlayerCapability();
        return INSTANCE;
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return SMALLBATS_PLAYER_CAP.orEmpty(cap,opt);
    }

    @Override
    public CompoundTag serializeNBT() {
        return getInstance().serializerNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getInstance().deserializeNBT(nbt);
    }
}
