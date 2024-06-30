package me.mc.mods.smallbats.mixins;

import me.mc.mods.smallbats.events.VerticalStateChangedEvent;
import me.mc.mods.smallbats.mixininterfaces.IVerticalState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements IVerticalState {
    @Unique
    private static boolean mod_1_20_1_smallbats$verticalCollision(Entity e) {
        if (e.level().getBlockState(e.blockPosition().above(Mth.floor(e.getBbHeight())+1)).isAir()) {
            return false;
        }
        Vec3 start = e.position().add(0d,e.getBbHeight(),0d);
        Vec3 end = start.add(0,.01,0);
        BlockHitResult hitResult = e.level().clip(new ClipContext(start,end,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,e));
        Vec3 diff = hitResult.getLocation().subtract(start);
        return diff.y <= .0001f;
    }
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
    public boolean getIsOnCeiling(boolean newRayCheck) {
        return this.isOnCeiling || (newRayCheck && mod_1_20_1_smallbats$verticalCollision(((Entity)(Object)this)));
    }

    @Override
    public boolean getIsOnFloor() {
        return this.isOnFloor;
    }

    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setOnGroundWithKnownMovement(ZLnet/minecraft/world/phys/Vec3;)V"))
    public void move(CallbackInfo ci) {
        Entity e = (Entity)(Object)this;
        IVerticalState verticalState = (IVerticalState)e;
        // We need a custom vertical collision system for (UP) direction

        // Check if it is "hanging" upside down
        if(mod_1_20_1_smallbats$verticalCollision(e) && !e.onGround()) {
            verticalState.setIsOnCeiling(true);
        }
        // Check if it is on floor
        if(e.onGround()){
            verticalState.setIsOnFloor(true);
        }

        // Reset those states
        if (e.getDeltaMovement().y<0 || e.getDeltaMovement().y>=0.5) {
            if (verticalState.getIsOnCeiling(true)) {
                MinecraftForge.EVENT_BUS.post(
                        new VerticalStateChangedEvent(
                                false,
                                false,
                                false,
                                true,
                                e
                        )
                );
            }
            verticalState.setIsOnCeiling(false);
        }
        if (e.getDeltaMovement().y>0 || e.getDeltaMovement().y<=-0.5) {
            if (verticalState.getIsOnFloor()) {
                MinecraftForge.EVENT_BUS.post(new VerticalStateChangedEvent(false,true,false,false, e));
            }
            verticalState.setIsOnFloor(false);
        }
    }
}
