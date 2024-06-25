package me.mc.mods.smallbats.mixins;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.entity.player.VampirismPlayerAttributes;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import me.mc.mods.smallbats.mixininterfaces.IVerticalState;
import me.mc.mods.smallbats.vampire.SmallBatsVampireActions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "getSleepingPos",at = @At("HEAD"), cancellable = true)
    public void getSleepingPos(CallbackInfoReturnable<Optional<BlockPos>> cir) {
        // Obtain the player
        if((LivingEntity)(Object)this instanceof Player player) {
            Optional<IVampirePlayer> vampPlayer = VampirismAPI.getVampirePlayer(player).resolve();
            if(vampPlayer.isPresent()) {
                // Player is vampire
                // Check if ability is enabled
                if(vampPlayer.get().getActionHandler().isActionActive(SmallBatsVampireActions.BATSLEEP.get())) {
                    // Action is enabled, return the current player position as the sleeping spot
                    cir.setReturnValue(Optional.of(player.blockPosition()));
                    cir.cancel();
                }
            }
        }
        // check first if action is active
    }
}
