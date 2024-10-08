package me.mc.mods.smallbats.mixins;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.teamlapen.vampirism.client.renderer.entity.layers.VampirePlayerHeadLayer;
import de.teamlapen.vampirism.config.VampirismConfig;
import de.teamlapen.vampirism.entity.player.VampirismPlayerAttributes;
import me.mc.mods.smallbats.ModSmallBats;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = VampirePlayerHeadLayer.class, remap = false, priority = Integer.MAX_VALUE)
// @OnlyIn(Dist.CLIENT)
public abstract class VampirePlayerHeadLayerMixin {
    static {
        ModSmallBats.INSTANCE.Logger.info("Player Head Layer Mixin overwrite");
    }

    @Shadow(remap = false)
    @Final
    private ResourceLocation @NotNull [] fangOverlays;

    @Shadow(remap = false)
    @Final
    private ResourceLocation @NotNull [] eyeOverlays;

    // @Unique
    // private VampirismPlayerAttributes mod_1_20_1_smallbats$getVampirismPlayerAtts(Player p) {
    //     return VampirismPlayerAttributes.get(p);
    // }
    // @Unique
    // private int mod_1_20_1_smallbats$getEyeType(Player p) {
    //     return Math.max(0, Math.min(mod_1_20_1_smallbats$getVampirismPlayerAtts(p).getVampSpecial().eyeType, eyeOverlays.length - 1));
    // }
    // @Unique
    // private int mod_1_20_1_smallbats$getFangtype(Player p) {
    //     return Math.max(0, Math.min(mod_1_20_1_smallbats$getVampirismPlayerAtts(p).getVampSpecial().fangType, fangOverlays.length - 1));
    // }


    /*@Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/player/Player;FFFFFF)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )*/

    /**
     * @author VenomiZeD
     * @reason New more compatible render system
     */
    @Overwrite
    public <T extends Player> void render(@NotNull PoseStack stack, @NotNull MultiBufferSource iRenderTypeBuffer, int i, @NotNull T player, float v, float v1, float v2, float v3, float v4, float v5) {
        if (!VampirismConfig.CLIENT.renderVampireEyes.get() || !player.isAlive()) return;
        VampirismPlayerAttributes atts = VampirismPlayerAttributes.get(player);
        if (atts.vampireLevel > 0 && !atts.getVampSpecial().disguised && !player.isInvisible()) {
            int eyeType = Math.max(0, Math.min(atts.getVampSpecial().eyeType, eyeOverlays.length - 1));
            int fangType = Math.max(0, Math.min(atts.getVampSpecial().fangType, fangOverlays.length - 1));
            RenderType eyeRenderType = atts.getVampSpecial().glowingEyes ? RenderType.eyes(eyeOverlays[eyeType]) : RenderType.entityCutoutNoCull(eyeOverlays[eyeType]);

            VertexConsumer vertexBuilderEye = iRenderTypeBuffer.getBuffer(eyeRenderType);
            VertexConsumer vertexBuilderFang = iRenderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(fangOverlays[fangType]));

            int packerOverlay = LivingEntityRenderer.getOverlayCoords(player, 0);
            ModelPart head = ((VampirePlayerHeadLayer<?, ?>) (Object) this).getParentModel().head;


            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            // RenderSystem.disableDepthTest();

            RenderSystem.setShaderTexture(0, fangOverlays[fangType]);
            head.render(stack, vertexBuilderFang, i, packerOverlay);


            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            // RenderSystem.disableDepthTest();

            RenderSystem.setShaderTexture(0, eyeOverlays[eyeType]);
            head.render(stack, vertexBuilderEye, i, packerOverlay);


            // vv latest change vv
            RenderSystem.enableBlend();
            RenderSystem.disableDepthTest();
        }
        // ci.cancel();
    }
}

