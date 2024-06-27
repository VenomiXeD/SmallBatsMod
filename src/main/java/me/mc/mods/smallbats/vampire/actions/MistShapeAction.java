package me.mc.mods.smallbats.vampire.actions;

import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.actions.VampireActions;
import de.teamlapen.vampirism.util.DamageHandler;
import de.teamlapen.vampirism.world.ModDamageSources;
import de.teamlapen.vampirism.world.VampirismWorld;
import me.mc.mods.smallbats.ModSmallBats;
import me.mc.mods.smallbats.caps.ISmallBatsPlayerCapability;
import me.mc.mods.smallbats.caps.ISynchronizableCapabilityProvider;
import me.mc.mods.smallbats.caps.SmallBatsPlayerCapabilityProvider;
import me.mc.mods.smallbats.network.PacketSynchronizeCapability;
import me.mc.mods.smallbats.util.MathUtils;
import me.mc.mods.smallbats.vampire.SmallBatsVampireActions;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Optional;

public class MistShapeAction implements ILastingAction<IVampirePlayer> {

    public static final EntityDimensions MIST_DIMENSIONS = EntityDimensions.fixed(0.05f,0.05f);
    private DustColorTransitionOptions PARTICLE_MISTEFFECT = new DustColorTransitionOptions(
            new Vector3f(0.22f,0.22f,0.22f),
            new Vector3f(0.15f,0.15f,0.15f),
            2
    );
    public static boolean isMistShape(Player p) {
        LazyOptional<IVampirePlayer> vampirePlayer = VampirismAPI.getVampirePlayer(p);
        if (vampirePlayer.isPresent()) {
           return vampirePlayer.resolve().get().getActionHandler().isActionActive(SmallBatsVampireActions.MISTFORM.get()) ;
        }
        return false;
    }
    @Override
    public PERM canUse(IVampirePlayer player) {
        return player.getActionHandler().isActionActive(VampireActions.BAT.get()) ? PERM.DISALLOWED : PERM.ALLOWED;
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
        return "action.smallbats.mist";
    }

    @Override
    public boolean onActivated(IVampirePlayer player, ActivationContext context) {
        Player e = player.getRepresentingPlayer();
        updatePlayer(player, true);
        return true;
    }

    @Override
    public int getDuration(IVampirePlayer player) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onActivatedClient(IVampirePlayer player) {
        // ModSmallBats.INSTANCE.Logger.info("side: " + player.getRepresentingPlayer().level().isClientSide());
        player.getRepresentingPlayer().refreshDimensions();;
    }

    @Override
    public void onDeactivated(IVampirePlayer player) {
        if (!player.isRemote()) {
            Player e = player.getRepresentingPlayer();
            updatePlayer(player, false);
        }
        else {
            player.getRepresentingPlayer().refreshDimensions();;
        }
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
        Player e = player.getRepresentingPlayer();
        if(!player.isRemote()) {
            for (int i = 0; i < 50; i++) {
                Vec3 particlePos = MathUtils.randomSpherePositions(e.level().getRandom(), e.position(), 0.5f);
                double x = particlePos.x;
                double y = particlePos.y;
                double z = particlePos.z;

                ((ServerLevel) e.level()).sendParticles(PARTICLE_MISTEFFECT, x, y, z, 1, 0, 0, 0, 0.5f);
            }
            if (player.getRepresentingPlayer().isAlive() && player.isGettingSundamage(player.getRepresentingPlayer().level())) {
                VampirePlayer.getOpt(player.getRepresentingPlayer()).ifPresent(vp->vp.onDeath(new ModDamageSources(player.getRepresentingEntity().level().registryAccess()).sunDamage()));
                return true;
            }
        }
        else {
            return player.isGettingSundamage(player.getRepresentingEntity().level());
        }
        return false;
    }

    void updatePlayer(IVampirePlayer p, boolean activated) {
        Player player = p.getRepresentingPlayer();
        LazyOptional<ISmallBatsPlayerCapability> cap = player.getCapability(SmallBatsPlayerCapabilityProvider.SMALLBATS_PLAYER_CAP);
        cap.ifPresent(c->{
            c.setIsMist(activated);
        });

        //player.syn

        player.getAbilities().flying = p.getRepresentingPlayer().isSpectator();
        player.getAbilities().mayfly = activated || p.getRepresentingPlayer().isCreative() || p.getRepresentingPlayer().isSpectator();
        player.getAbilities().mayBuild = !activated;
        player.setForcedPose(activated ? Pose.STANDING : null);

        cap.ifPresent(c->c.sync(player));

        player.refreshDimensions();
        player.onUpdateAbilities();
    }
}
