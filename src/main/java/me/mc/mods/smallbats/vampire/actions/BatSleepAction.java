package me.mc.mods.smallbats.vampire.actions;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.vampire.DefaultVampireAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.entity.player.VampirismPlayerAttributes;
import de.teamlapen.vampirism.entity.player.vampire.actions.VampireActions;
import me.mc.mods.smallbats.ModSmallBats;
import me.mc.mods.smallbats.mixininterfaces.IVerticalState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Optional;

public class BatSleepAction implements ILastingAction<IVampirePlayer> {
    @Override
    public PERM canUse(IVampirePlayer player) {
        return PERM.ALLOWED;
    }

    @Override
    public int getCooldown(IVampirePlayer player) {
        return 20;
    }

    @Override
    public @NotNull Optional<IPlayableFaction<?>> getFaction() {
        return Optional.ofNullable(VReference.VAMPIRE_FACTION);
    }

    @Override
    public String getTranslationKey() {
        return "skill.smallbats.batsleep";
    }

    @Override
    public boolean onActivated(IVampirePlayer player, ActivationContext context) {
        if (VampirismPlayerAttributes.get(player.getRepresentingPlayer()).getVampSpecial().bat) {
            boolean isOnCeiling = ((IVerticalState)player.getRepresentingPlayer()).getIsOnCeiling();
            ModSmallBats.INSTANCE.Logger.info("server-side on ceiling: " + isOnCeiling);
            if (isOnCeiling) {
                BlockPos pos = player.getRepresentingEntity().blockPosition();
                ModSmallBats.INSTANCE.Logger.info("start sleeping");
                // We can sleep now
                player.getRepresentingPlayer().startSleeping(pos);
                ((ServerLevel)player.getRepresentingPlayer().level()).updateSleepingPlayerList();
                return true;
            }
        }
        return false;
    }

    @Override
    public int getDuration(IVampirePlayer player) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onActivatedClient(IVampirePlayer player) {

    }

    @Override
    public void onDeactivated(IVampirePlayer player) {

    }

    @Override
    public void onReActivated(IVampirePlayer player) {

    }

    @Override
    public boolean onUpdate(IVampirePlayer player) {
        if(player.getRepresentingPlayer().level().isClientSide()) {
            return false;
        }
        return player.getRepresentingEntity().level().isNight();
    }
}
