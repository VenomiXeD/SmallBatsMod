package me.mc.mods.smallbats.caps;

import me.mc.mods.smallbats.ModSmallBats;
import net.minecraft.core.Direction;
import net.minecraft.data.DataProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmallBatsPlayerCapability implements ICapabilitySerializable<CompoundTag> {
    public static final ResourceLocation SMALLBATS_CAP_LOC = new ResourceLocation(ModSmallBats.MODID,"smallbatsplayercap");
    public static final Capability<SmallBatsPlayerCapability> SMALLBATS_CAP = CapabilityManager.get(new CapabilityToken<>() {}                                                                  );
    private boolean isMist;
    public void setIsMist(boolean mist) {
        isMist = mist;
    }
    public boolean getIsMist() {
        return isMist;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return SMALLBATS_CAP.orEmpty(cap,LazyOptional.of(SmallBatsPlayerCapability::new));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag t = new CompoundTag();
        t.putBoolean("mist",getIsMist());
        return t;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        setIsMist(nbt.getBoolean("mist"));
    }
}
