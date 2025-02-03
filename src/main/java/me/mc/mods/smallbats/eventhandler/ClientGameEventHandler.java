package me.mc.mods.smallbats.eventhandler;

import de.teamlapen.vampirism.api.VampirismAPI;
import me.mc.mods.smallbats.util.MathUtils;
import me.mc.mods.smallbats.vampire.actions.MistShapeAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
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
    public void onRenderLivingEventPre(RenderLivingEvent.Pre<Player, PlayerModel<Player>> e) {
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
    public void onRenderLivingEventPost(RenderLivingEvent.Post<Player, PlayerModel<Player>> e) {
        if (e.getEntity() instanceof Player p) {
            VampirismAPI.getVampirePlayer(p).ifPresent(player -> {
                int ticksInSun = player.getTicksInSun();
                if (ticksInSun == 0 || player.isIgnoringSundamage() || p.isCreative() || p.isSpectator()) { return; }
                int fireTicks = Math.max(1,ticksInSun - 20*30) * 10;

                if (p.level().getGameTime() % fireTicks == 0) {
                    RandomSource r = p.level().getRandom();
                    double rX = r.nextDouble() * p.getBbWidth() * (r.nextBoolean() ? 1 : -1) / 2d;
                    double rY = r.nextDouble() * p.getBbHeight();
                    double rZ = r.nextDouble() * p.getBbWidth() * (r.nextBoolean() ? 1 : -1) / 2d;


                    final double speedFactor = 0.02d;
                    e.getEntity().level().addParticle(ParticleTypes.SMALL_FLAME,
                            e.getEntity().getX() + rX,
                            e.getEntity().getY() + rY,
                            e.getEntity().getZ() + rZ,
                            r.nextDouble() * speedFactor * (r.nextBoolean() ? 1 : -1), r.nextDouble() * speedFactor, r.nextDouble() * speedFactor * (r.nextBoolean() ? 1 : -1)
                    );
                }
            });
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
