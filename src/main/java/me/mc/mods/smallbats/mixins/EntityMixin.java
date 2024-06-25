package me.mc.mods.smallbats.mixins;

import cpw.mods.util.Lazy;
import me.mc.mods.smallbats.events.VerticalStateChangedEvent;
import me.mc.mods.smallbats.mixininterfaces.IVerticalState;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements IVerticalState {
    private static boolean verticalCollision(Entity e) {
        Level l = e.level();
        BlockPos bPos = e.blockPosition().above();
        BlockState b = l.getBlockState(bPos);
        if(b.isAir()) {
            return false;
        }
        return e.getDimensions(e.getPose()).makeBoundingBox(e.position()).intersects(b.getCollisionShape(l,bPos).bounds());
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
    public boolean getIsOnFloor() {
        return this.isOnFloor;
    }

    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setOnGroundWithKnownMovement(ZLnet/minecraft/world/phys/Vec3;)V"))
    public void move(CallbackInfo ci) {
        Entity e = (Entity)(Object)this;
        IVerticalState verticalState = (IVerticalState)e;
        // We need a custom vertical collision system for (UP) direction

        // Check if it is "hanging" upside down
        if(verticalCollision(e) && !e.onGround()) {
            verticalState.setIsOnCeiling(true);
        }
        // Check if it is on floor
        if(e.onGround()){
            verticalState.setIsOnFloor(true);
        }

        // Reset those states
        if (e.getDeltaMovement().y<0 || e.getDeltaMovement().y>=0.5) {
            if (verticalState.getIsOnCeiling()) {
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
