package me.mc.mods.smallbats.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.entity.player.ModPlayerEventHandler;
import de.teamlapen.vampirism.entity.player.VampirismPlayerAttributes;
import me.mc.mods.smallbats.ModSmallBats;
import me.mc.mods.smallbats.vampire.SmallBatsVampireActions;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = ModPlayerEventHandler.class, remap = false)
public abstract class ModPlayerEventHandlerMixin {
    @Inject(method = "sleepTimeCheck", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/entity/player/SleepingTimeCheckEvent;getSleepingLocation()Ljava/util/Optional;"), cancellable = true)
    public void sleepTimeCheck(SleepingTimeCheckEvent event, CallbackInfo ci) {
        // Obtain the vampire player object
        Optional<IVampirePlayer> vampPlayer = VampirismAPI.getVampirePlayer(event.getEntity()).resolve();
        if(vampPlayer.isPresent()) {
            // Check if the ability is enabled
            boolean isBatSleeping = vampPlayer.get().getActionHandler().isActionActive(SmallBatsVampireActions.BATSLEEP.get());
            boolean isDay = event.getEntity().level().isDay();
            if (isBatSleeping && isDay) {
                ModSmallBats.INSTANCE.Logger.info("can sleep!");
                event.setResult(Event.Result.ALLOW);
                ci.cancel();
            }
        }
    }

    @Inject(method="sleepTimeFinish",at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/level/SleepFinishedTimeEvent;getLevel()Lnet/minecraft/world/level/LevelAccessor;"), cancellable = true)
    public void sleepTimeFinish(SleepFinishedTimeEvent event, CallbackInfo ci) {
        boolean isSleepingAsBats = event.getLevel().players().stream().anyMatch(player -> {
           LazyOptional<IVampirePlayer> vampPlayer = VampirismAPI.getVampirePlayer(player);
           if(vampPlayer.isPresent()) {
               return vampPlayer.resolve().get().getActionHandler().isActionActive(SmallBatsVampireActions.BATSLEEP.get());
           }
           return false;
        });
        if (isSleepingAsBats) {
            long dist = ((ServerLevel) event.getLevel()).getDayTime() % 24000L > 12000L ? 13000 : -11000; //Make sure we don't go backwards in time (in special case sleeping at 23500)
            event.setTimeAddition(event.getNewTime() + dist);
            ci.cancel();
        }
    }
}
