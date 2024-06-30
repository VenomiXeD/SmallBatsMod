package me.mc.mods.smallbats.caps;

import net.minecraft.nbt.CompoundTag;

public class SmallBatsPlayerCapability implements ISmallBatsPlayerCapability {
    private boolean mistState;
    private boolean ceilingHangingState;

    @Override
    public boolean getIsMist() {
        return mistState;
    }

    @Override
    public void setIsMist(boolean mist) {
        mistState = mist;
    }

    @Override
    public boolean getIsCeilingHanging() {
        return ceilingHangingState;
    }

    @Override
    public void setIsCeilingHanging(boolean isHanging) {
        ceilingHangingState = isHanging;
    }

    @Override
    public CompoundTag serializerNBT() {
        CompoundTag t = new CompoundTag();
        t.putBoolean("mist",getIsMist());
        t.putBoolean("ceilinghanging",getIsCeilingHanging());
        return t;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.setIsMist(nbt.getBoolean("mist"));
        this.setIsCeilingHanging(nbt.getBoolean("ceilinghanging"));
    }

    @Override
    public Class getUnderlyingCapabilityProvider() {
        return SmallBatsPlayerCapabilityProvider.class;
    }
}
