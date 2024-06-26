package me.mc.mods.smallbats.mixins;

import de.teamlapen.vampirism.client.renderer.RenderHandler;
import me.mc.mods.smallbats.mixininterfaces.IVerticalState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(value = RenderHandler.class)
public abstract class RenderHandlerMixin {
    @Unique
    private float mod_1_20_1_smallbats$getFlippedRotation(float originalRotation, boolean invert) {
        originalRotation += Mth.PI;
        if(invert)
            originalRotation *= -1;
        return originalRotation;
    }
    @Unique
    private void mod_1_20_1_smallbats$flipBatEntityRotation(Bat bat, boolean flipHead, boolean flipBody, boolean invertHead, boolean invertBody) {
        if(flipHead) {
            bat.yHeadRotO = mod_1_20_1_smallbats$getFlippedRotation(entityBat.yHeadRotO, true);
            bat.yHeadRot = mod_1_20_1_smallbats$getFlippedRotation(entityBat.yHeadRot,true);

            bat.setXRot(mod_1_20_1_smallbats$getFlippedRotation(entityBat.getXRot(),invertHead));
            bat.xRotO = mod_1_20_1_smallbats$getFlippedRotation(entityBat.xRotO,invertHead);
        }

        bat.yBodyRotO = mod_1_20_1_smallbats$getFlippedRotation(entityBat.yBodyRotO,invertBody);
        bat.yBodyRot = mod_1_20_1_smallbats$getFlippedRotation(entityBat.yBodyRot,invertBody);
    }
    @Shadow(remap = false)
    private Bat entityBat;

    @Inject(method = "onRenderPlayerPreHigh", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ambient/Bat;setInvisible(Z)V", shift = At.Shift.AFTER))
    public void onRenderPlayerPreHigh(RenderPlayerEvent.Pre event, CallbackInfo ci) {
        boolean isOnCeiling = ((IVerticalState)event.getEntity()).getIsOnCeiling();
        boolean isOnFloor = ((IVerticalState)event.getEntity()).getIsOnFloor();

        // Set its position to resting
        entityBat.setResting(isOnCeiling || isOnFloor);

        ((IVerticalState)entityBat).setIsOnCeiling(isOnCeiling);
        ((IVerticalState)entityBat).setIsOnFloor(isOnFloor);
        // Flip look values
        /*
        if (isOnFloor) {
            mod_1_20_1_smallbats$flipBatEntityRotation(entityBat, true,true,false,false );
        }*/
        if (isOnCeiling) {
            mod_1_20_1_smallbats$flipBatEntityRotation(entityBat,false,false,false,false);
        }
    }

}
