package me.mc.mods.smallbats.eventhandler;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.entity.player.VampirismPlayerAttributes;
import me.mc.mods.smallbats.caps.SmallBatsPlayerCapabilityProvider;
import me.mc.mods.smallbats.vampire.SmallBatsVampireActions;
import me.mc.mods.smallbats.vampire.actions.MistShapeAction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@SuppressWarnings("removal")
public class GameEventHandler {
    @SubscribeEvent
    public void onSleepingLocationCheckEvent(SleepingLocationCheckEvent e) {
        if (e.getEntity() instanceof Player player) {
            e.setResult(VampirismPlayerAttributes.get(player).getVampSpecial().bat ? Event.Result.ALLOW : Event.Result.DEFAULT);
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
    @SubscribeEvent
    public void onPlayerDamage(LivingDamageEvent e) {
        // TODO: implement damage handling mist and is holy water (idk what should happen)
    }

    @SubscribeEvent
    public void onEntityEventSize(EntityEvent.Size e) {
        if (e.getEntity() instanceof Player p) {
            p.getCapability(SmallBatsPlayerCapabilityProvider.SMALLBATS_PLAYER_CAP).ifPresent(cap -> {
                if (cap.getIsMist()) {
                    e.setNewSize(MistShapeAction.MIST_DIMENSIONS);
                    e.setNewEyeHeight(MistShapeAction.MIST_DIMENSIONS.height);
                }
            });
        }
    }

    @SubscribeEvent
    public void onAttachCapability(AttachCapabilitiesEvent<Entity> e) {
        if(e.getObject() instanceof Player) {
            e.addCapability(SmallBatsPlayerCapabilityProvider.SMALLBATS_PLAYER_CAP_LOC, new SmallBatsPlayerCapabilityProvider());
        }
    }
}
