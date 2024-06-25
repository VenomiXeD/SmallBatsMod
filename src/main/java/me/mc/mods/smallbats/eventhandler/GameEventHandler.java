package me.mc.mods.smallbats.eventhandler;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.entity.player.VampirismPlayerAttributes;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.world.VampirismWorld;
import me.mc.mods.smallbats.ModSmallBats;
import me.mc.mods.smallbats.events.VerticalStateChangedEvent;
import me.mc.mods.smallbats.vampire.SmallBatsVampireActions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GameEventHandler {
    @SubscribeEvent
    public void onSleepingLocationCheckEvent(SleepingLocationCheckEvent e) {
        if (e.getEntity() instanceof Player player) {
            // ModSmallBats.INSTANCE.Logger.info("checking if player may sleep");
            e.setResult(VampirismPlayerAttributes.get(player).getVampSpecial().bat ? Event.Result.ALLOW : Event.Result.DEFAULT);
        }
    }

    @SubscribeEvent
    public void onVerticalStateChangedEvent(VerticalStateChangedEvent e) {
        if(e.entity instanceof Player player && e.entity.level().isClientSide())  {
            if(e.justTookOffFromCeiling && VampirismPlayerAttributes.get(player).getVampSpecial().bat) {
                e.entity.level().playLocalSound(
                        e.entity.blockPosition(),
                        SoundEvents.BAT_TAKEOFF,
                        SoundSource.PLAYERS,
                        .5f,
                        Mth.lerp(e.entity.level().random.nextFloat(),.9f,1.1f),
                        false
                );
            }
        }
    }
}
