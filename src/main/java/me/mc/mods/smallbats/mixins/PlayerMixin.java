package me.mc.mods.smallbats.mixins;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
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
public class PlayerMixin implements IVerticalState {
    private float linearInterpolation(float alpha, float a, float b) {
        return (1-alpha)*a + b*alpha;
    }
    @Unique
    private boolean isOnCeiling;
    @Unique
    private boolean isOnFloor;

    @Override
    public void setIsOnCeiling(boolean b) {
        this.isOnCeiling = b;
    }

    @Override
    public void setIsOnFloor(boolean b) {
        this.isOnFloor = b;
    }

    @Override
    public boolean getIsOnCeiling() {
        return this.isOnCeiling;
    }

    @Override
    public boolean getIsOnFloor() {
        return this.isOnFloor;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        Player pEntity = (Player)(Object)this;
        // Check if it is "hanging" upside down
        if(pEntity.verticalCollision && !pEntity.onGround()) {
            isOnCeiling = true;
        }
        // Check if it is on floor
        if(pEntity.onGround()){
            isOnFloor = true;
        }

        // Reset those states
        if (pEntity.getDeltaMovement().y<0) {
            if (isOnCeiling) {
                pEntity.level().playLocalSound(
                        pEntity.blockPosition(),
                        SoundEvents.BAT_TAKEOFF,
                        SoundSource.PLAYERS,
                        .5f,
                        linearInterpolation(pEntity.level().random.nextFloat(),.9f,1.1f),
                        false
                );
            }
            isOnCeiling = false;
        }
        if(pEntity.getDeltaMovement().y>0) {
            isOnFloor = false;
        }
    }
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
