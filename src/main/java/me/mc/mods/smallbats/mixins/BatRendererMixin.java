package me.mc.mods.smallbats.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.mc.mods.smallbats.mixininterfaces.IVerticalState;
import net.minecraft.client.renderer.entity.BatRenderer;
import net.minecraft.world.entity.ambient.Bat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BatRenderer.class)
public abstract class BatRendererMixin {
    @Inject(method = "setupRotations(Lnet/minecraft/world/entity/ambient/Bat;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/MobRenderer;setupRotations(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V"))
    public void setupRotations(Bat pEntityLiving, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks, CallbackInfo ci) {
        if(((IVerticalState)pEntityLiving).getIsOnFloor()) {
            pPoseStack.mulPose(Axis.YP.rotationDegrees(180f));
        }
        if(((IVerticalState)pEntityLiving).getIsOnFloor()) {
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(180f));
            pPoseStack.translate(0,-1.1f,0);
        }
    }
}
