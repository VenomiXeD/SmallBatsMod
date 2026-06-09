package me.mc.mods.smallbats.caps;

import me.mc.mods.smallbats.ModSmallBats;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmallBatsLevelCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<ISmallbatsLevelCapability> SMALL_BATS_LEVEL_CAP = CapabilityManager.get(new CapabilityToken<ISmallbatsLevelCapability>() {
    });
    public static ResourceLocation SMALLBATS_LEVEL_CAP_LOC = new ResourceLocation(ModSmallBats.MODID, "levelcap");


    private ISmallbatsLevelCapability INSTANCE;
    private final LazyOptional<ISmallbatsLevelCapability> opt = LazyOptional.of(this::getInstance);

    /**
     * Retrieves the Optional handler for the capability requested on the specific side.
     * The return value <strong>CAN</strong> be the same for multiple faces.
     * Modders are encouraged to cache this value, using the listener capabilities of the Optional to
     * be notified if the requested capability get lost.
     *
     * @param cap  The capability to check
     * @param side The Side to check from,
     *             <strong>CAN BE NULL</strong>. Null is defined to represent 'internal' or 'self'
     * @return The requested an optional holding the requested capability.
     */
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return SMALL_BATS_LEVEL_CAP.orEmpty(cap, opt);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return SMALL_BATS_LEVEL_CAP.orEmpty(cap, LazyOptional.of(this::getInstance));
    }

    private @NotNull ISmallbatsLevelCapability getInstance() {
        if (INSTANCE == null) INSTANCE = new SmallBatsLevelCapability();
        return INSTANCE;
    }

    @Override
    public CompoundTag serializeNBT() {
        return getInstance().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getInstance().deserializeNBT(nbt);
    }
}
