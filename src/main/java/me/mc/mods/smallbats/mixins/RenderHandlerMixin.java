package me.mc.mods.smallbats.mixins;

import de.teamlapen.vampirism.client.renderer.RenderHandler;
import me.mc.mods.smallbats.util.VerticalCollisionUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
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
    @Shadow(remap = false)
    private Bat entityBat;

    private boolean lastTickUpsideDown = false;

    @Inject(method = "onRenderPlayerPreHigh", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ambient/Bat;setInvisible(Z)V", shift = At.Shift.AFTER))
    public void onRenderPlayerPreHigh(RenderPlayerEvent.Pre event, CallbackInfo ci) {
        if (VerticalCollisionUtil.verticalCollisionUp(event.getEntity())) {
            lastTickUpsideDown = true;
            entityBat.setResting(true);
            entityBat.yBodyRot += Mth.PI;
            entityBat.yBodyRotO += Mth.PI;
        } else {
            if (lastTickUpsideDown) {
                event.getEntity().level().playSound(event.getEntity(),
                        event.getEntity().blockPosition(),
                        SoundEvents.BAT_TAKEOFF,
                        SoundSource.PLAYERS,
                        .5f,
                        Mth.lerp(event.getEntity().level().random.nextFloat(), .9f, 1.1f)
                );
                lastTickUpsideDown = false;
            }
            entityBat.setResting(false);
        }
    }

}
