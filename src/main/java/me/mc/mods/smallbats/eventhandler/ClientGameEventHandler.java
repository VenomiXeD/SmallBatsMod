package me.mc.mods.smallbats.eventhandler;

import me.mc.mods.smallbats.util.MathUtils;
import me.mc.mods.smallbats.vampire.actions.MistShapeAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class ClientGameEventHandler {
    private static void spawnMistParticlesAt(Level l, Vec3 p) {
        Vec3 particlePos = MathUtils.randomSpherePositions(l.getRandom(), p, 0.5f);
        double x = particlePos.x;
        double y = particlePos.y;
        double z = particlePos.z;


        l.addParticle(MistShapeAction.PARTICLE_MISTEFFECT, x, y, z, 0, 0, 0);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRenderLivingEvent(RenderLivingEvent.Pre<Player, PlayerModel<Player>> e) {
        if(e.getEntity() instanceof Player p) {
            if (MistShapeAction.isMistShape(p)) {
                e.setCanceled(true);

                Level l = e.getEntity().level();

                if(e.getEntity().is(Minecraft.getInstance().player)) {
                    // Own entity, check if we in first person
                    if (!Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                        spawnMistParticlesAt(l,e.getEntity().position());
                    }
                }
                else {
                    spawnMistParticlesAt(l,e.getEntity().position());
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onHandRenderEVent(RenderHandEvent e) {
        if (MistShapeAction.isMistShape(Minecraft.getInstance().player)) {
            e.setCanceled(true);
        }
    }
}
