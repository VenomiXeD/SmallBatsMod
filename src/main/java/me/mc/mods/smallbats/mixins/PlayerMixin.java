package me.mc.mods.smallbats.mixins;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.entity.player.VampirismPlayerAttributes;
import me.mc.mods.smallbats.mixininterfaces.IVerticalState;
import me.mc.mods.smallbats.vampire.SmallBatsVampireActions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = Player.class)
public abstract class PlayerMixin implements IVerticalState {
    @Inject(method = "stopSleepInBed",at=@At("HEAD"), cancellable = true)
    public void stopSleepInBed(boolean pWakeImmediately, boolean pUpdateLevelForSleepingPlayers, CallbackInfo ci) {
        Optional<IVampirePlayer> vp = VampirismAPI.getVampirePlayer((Player)(Object)this).resolve();
        vp.ifPresent((v)-> {
            if(v.getActionHandler().isActionActive(SmallBatsVampireActions.BATSLEEP.get())) {
                if(!vp.get().getRepresentingPlayer().level().isClientSide())
                    ((ServerLevel)vp.get().getRepresentingPlayer().level()).updateSleepingPlayerList();
                ci.cancel();
            };
        });
    }

}
