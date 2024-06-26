package me.mc.mods.smallbats.mixins;

import de.teamlapen.vampirism.client.renderer.RenderHandler;
import me.mc.mods.smallbats.ModSmallBats;
import me.mc.mods.smallbats.mixininterfaces.IVerticalState;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(RenderHandler.class)
public abstract class RenderHandlerMixin {
    @Shadow(remap=false) private @Nullable Bat entityBat;

    @Inject(method = "onRenderPlayerPreHigh", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ambient/Bat;setInvisible(Z)V", shift = At.Shift.AFTER), remap = false)
    public void onRenderPlayerPreHigh(RenderPlayerEvent.Pre event, CallbackInfo ci) {
        boolean isOnCeiling = ((IVerticalState)event.getEntity()).getIsOnCeiling();
        boolean isOnFloor = ((IVerticalState)event.getEntity()).getIsOnFloor();

        ModSmallBats.INSTANCE.Logger.info("we are rendering the entity");
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
