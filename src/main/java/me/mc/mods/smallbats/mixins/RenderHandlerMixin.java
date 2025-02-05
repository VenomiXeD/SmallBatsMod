package me.mc.mods.smallbats.mixins;

import de.teamlapen.vampirism.client.renderer.RenderHandler;
import me.mc.mods.smallbats.util.VerticalCollisionUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@OnlyIn(Dist.CLIENT)
@Mixin(value = RenderHandler.class)
public abstract class RenderHandlerMixin {
    @Shadow(remap = false)
    private Bat entityBat;

    @Unique
    private boolean mod_1_20_1_smallbats$lastTickUpsideDown = false;

    @Inject(
            method = "onRenderPlayerPreHigh",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ambient/Bat;setInvisible(Z)V",
                    shift = At.Shift.AFTER
            )
    )
    public void onRenderPlayerPreHigh(RenderPlayerEvent.Pre event, CallbackInfo ci) {
        if (VerticalCollisionUtil.verticalCollisionUp(event.getEntity())) {
            mod_1_20_1_smallbats$lastTickUpsideDown = true;
            entityBat.setResting(true);

            Player player = event.getEntity();
            // Flip values when we're upside down
            entityBat.yBodyRotO += 180;
            entityBat.yBodyRot += 180;

            entityBat.setYRot(player.getYRot() + 180);
            entityBat.yRotO += 180;

            entityBat.yHeadRot += 180;
            entityBat.yHeadRotO += 180;

            entityBat.setXRot(player.getXRot() * -1);
            entityBat.xRotO *= -1;

        } else {
            if (mod_1_20_1_smallbats$lastTickUpsideDown) {
                event.getEntity().level().playSound(event.getEntity(),
                        event.getEntity().blockPosition(),
                        SoundEvents.BAT_TAKEOFF,
                        SoundSource.PLAYERS,
                        .5f,
                        Mth.lerp(event.getEntity().level().random.nextFloat(), .9f, 1.1f)
                );
                mod_1_20_1_smallbats$lastTickUpsideDown = false;
            }
            entityBat.setResting(false);
        }
    }

}
