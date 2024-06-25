package me.mc.mods.smallbats.mixins;

import me.mc.mods.smallbats.mixininterfaces.IVerticalState;
import net.minecraft.world.entity.ambient.Bat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Bat.class)
public class BatMixin implements IVerticalState {
    @Unique
    public boolean isOnFloor;
    @Unique
    public boolean isOnCeiling;

    @Override
    public void setIsOnCeiling(boolean b) {
        this.isOnCeiling = b;
    }

    @Override
    public void setIsOnFloor(boolean b) {
        this.isOnFloor = b;
    }

    @Override
    public boolean getIsOnCeiling() {
        return this.isOnCeiling;
    }

    @Override
    public boolean getIsOnFloor() {
        return this.isOnFloor;
    }
}
