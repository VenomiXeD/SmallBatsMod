package me.mc.mods.smallbats.caps;

import net.minecraft.nbt.CompoundTag;

public class SmallBatsPlayerCapability implements ISmallBatsPlayerCapability {
    private boolean mistState;

    @Override
    public boolean getIsMist() {
        return mistState;
    }

    @Override
    public void setIsMist(boolean mist) {
        mistState = mist;
    }

    @Override
    public CompoundTag serializerNBT() {
        CompoundTag t = new CompoundTag();
        t.putBoolean("mist",getIsMist());
        return t;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.setIsMist(nbt.getBoolean("mist"));
    }

    @Override
    public Class getUnderlyingCapabilityProvider() {
        return SmallBatsPlayerCapabilityProvider.class;
    }
}
