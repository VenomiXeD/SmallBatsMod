package me.mc.mods.smallbats.mixins;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import me.mc.mods.smallbats.vampire.SmallBatsVampireActions;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = Player.class)
public abstract class PlayerMixin {
    @Inject(method = "stopSleeping",at=@At("HEAD"), cancellable = true)
    public void stopSleepInBed(CallbackInfo ci) {
        Optional<IVampirePlayer> vp = VampirismAPI.getVampirePlayer((Player)(Object)this).resolve();
        vp.ifPresent((v)-> {
            if(v.getActionHandler().isActionActive(SmallBatsVampireActions.BATSLEEP.get())) {
                ci.cancel();
            };
        });
    }

}
