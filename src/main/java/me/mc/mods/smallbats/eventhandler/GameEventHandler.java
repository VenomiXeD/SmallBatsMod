package me.mc.mods.smallbats.eventhandler;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.entity.player.VampirismPlayerAttributes;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.world.VampirismWorld;
import me.mc.mods.smallbats.ModSmallBats;
import me.mc.mods.smallbats.vampire.SmallBatsVampireActions;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GameEventHandler {
    @SubscribeEvent
    public static void onSleepingLocationCheckEvent(SleepingLocationCheckEvent e) {
        if (e.getEntity() instanceof Player player) {
            ModSmallBats.INSTANCE.Logger.info("checking if player may sleep");
            e.setResult(VampirismPlayerAttributes.get(player).getVampSpecial().bat ? Event.Result.ALLOW : Event.Result.DEFAULT);
        }
    }
/*
    @SubscribeEvent
    public static void onSleepingTimeCheckEvent(SleepingTimeCheckEvent e) {
        if (VampirismAPI.getVampirePlayer(e.getEntity()).isPresent()) {
            Vampirism
            e.setResult(Event.Result.ALLOW);
        }
    }

 */
}
