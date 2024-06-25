package me.mc.mods.smallbats.mixins;

import de.teamlapen.vampirism.client.renderer.RenderHandler;
import me.mc.mods.smallbats.mixininterfaces.IVerticalState;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderHandler.class,remap = false)
public abstract class RenderHandlerMixin {
    private float linearInterpolation(float alpha, float a, float b) {
        return (1-alpha)*a + b*alpha;
    }
    @Shadow private @Nullable Bat entityBat;

    @Inject(method = "onRenderPlayerPreHigh", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ambient/Bat;setInvisible(Z)V", shift = At.Shift.AFTER))
    public void onRenderPlayerPreHigh(RenderPlayerEvent.Pre event, CallbackInfo ci) {
        boolean isOnCeiling = ((IVerticalState)event.getEntity()).getIsOnCeiling();
        boolean isOnFloor = ((IVerticalState)event.getEntity()).getIsOnFloor();

        // Set its position to resting
        entityBat.setResting(isOnCeiling || isOnFloor);

        ((IVerticalState)entityBat).setIsOnCeiling(isOnCeiling);
        ((IVerticalState)entityBat).setIsOnFloor(isOnFloor);
        // Flip look values
        if (isOnFloor) {
            entityBat.yHeadRot += (float) Math.PI;
            entityBat.yHeadRot *= -1;
        }
    }

}
