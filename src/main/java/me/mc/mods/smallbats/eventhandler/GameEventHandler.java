package me.mc.mods.smallbats.eventhandler;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.entity.player.VampirismPlayerAttributes;
import me.mc.mods.smallbats.events.VerticalStateChangedEvent;
import me.mc.mods.smallbats.vampire.SmallBatsVampireActions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
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
    public void onSleepInBed(PlayerSleepInBedEvent e) {
        LazyOptional<IVampirePlayer> vampPlayer = VampirismAPI.getVampirePlayer(e.getEntity());
        if(vampPlayer.isPresent()) {
            if (vampPlayer.resolve().get().getActionHandler().isActionActive(SmallBatsVampireActions.BATSLEEP.get())) {
                e.setResult(Event.Result.ALLOW);
            }
        }
    }

    // Event priority is high so the event handler is called before vampirism
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void sleepTimeFinish(SleepFinishedTimeEvent event) {
        if (event.getLevel() instanceof ServerLevel && ((ServerLevel) event.getLevel()).isDay()) {
            boolean sleepingAsBat = event.getLevel().players().stream().anyMatch(player -> {
                LazyOptional<IVampirePlayer> vampPlayer = VampirismAPI.getVampirePlayer(player);
                if(vampPlayer.isPresent()) {
                    return vampPlayer.resolve().get().getActionHandler().isActionActive(SmallBatsVampireActions.BATSLEEP.get());
                }
                return false;
            });
            if (sleepingAsBat) {
                long dist = ((ServerLevel) event.getLevel()).getDayTime() % 24000L > 12000L ? 13000 : -11000; //Make sure we don't go backwards in time (in special case sleeping at 23500)
                event.setTimeAddition(event.getNewTime() + dist);
            }
        }
    }
}
