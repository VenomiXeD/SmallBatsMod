package me.mc.mods.smallbats.mixins;

import de.teamlapen.vampirism.entity.player.VampirismPlayerAttributes;
import me.mc.mods.smallbats.vampire.actions.BatSleepAction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "getSleepingPos",at = @At("HEAD"), cancellable = true)
    public void getSleepingPos(CallbackInfoReturnable<Optional<BlockPos>> cir) {
        // Obtain the player
        if((LivingEntity)(Object)this instanceof Player player) {
            // Check if ability is enabled
            if (BatSleepAction.isSleepingAsBat(player)) {
                // Action is enabled, return the current player position as the sleeping spot
                cir.setReturnValue(Optional.of(player.blockPosition()));
                cir.cancel();

            }
        }
    }

    @Inject(method="startSleeping", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setPose(Lnet/minecraft/world/entity/Pose;)V"), cancellable = true)
    public void startSleeping(BlockPos pPos, CallbackInfo ci) {
        if((LivingEntity)(Object)this instanceof Player player) {
            if (VampirismPlayerAttributes.get(player).getVampSpecial().bat) {
                player.setSleepingPos(pPos);
                player.setDeltaMovement(0,0,0);
                player.hasImpulse = true;
                ci.cancel();
            }
        }
    }
}
