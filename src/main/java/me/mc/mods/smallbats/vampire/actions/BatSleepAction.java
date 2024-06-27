package me.mc.mods.smallbats.vampire.actions;

import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.entity.player.actions.IAction;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.entity.player.VampirismPlayerAttributes;
import me.mc.mods.smallbats.mixininterfaces.IVerticalState;
import me.mc.mods.smallbats.vampire.SmallBatsVampireActions;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BatSleepAction implements ILastingAction<IVampirePlayer>, IAction<IVampirePlayer> {
    public static boolean isSleepingAsBat(Player p) {
        LazyOptional<IVampirePlayer> vampirePlayer = VampirismAPI.getVampirePlayer(p);
        if (vampirePlayer.isPresent()) {
           return vampirePlayer.resolve().get().getActionHandler().isActionActive(SmallBatsVampireActions.BATSLEEP.get()) ;
        }
        return false;
    }
    @Override
    public PERM canUse(IVampirePlayer player) {
        return (player.getRepresentingPlayer().level().isDay() && ((IVerticalState)player.getRepresentingPlayer()).getIsOnCeiling()) ? PERM.ALLOWED : PERM.DISALLOWED;
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
        if (VampirismPlayerAttributes.get(player.getRepresentingPlayer()).getVampSpecial().bat && player.getRepresentingPlayer().level().isDay()) {
            boolean isOnCeiling = ((IVerticalState)player.getRepresentingPlayer()).getIsOnCeiling();
            //ModSmallBats.INSTANCE.Logger.info("server-side on ceiling: " + isOnCeiling);
            if (isOnCeiling) {
                BlockPos pos = player.getRepresentingEntity().blockPosition();
                //ModSmallBats.INSTANCE.Logger.info("start sleeping");
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
        return 20*10;
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
    public boolean showHudDuration(Player player) {
        return true;
    }

    @Override
    public boolean onUpdate(IVampirePlayer player) {
        if(!player.getRepresentingPlayer().level().isClientSide()) {
            return player.getRepresentingPlayer().level().isNight();
        }
        return Screen.hasShiftDown();
    }
}
