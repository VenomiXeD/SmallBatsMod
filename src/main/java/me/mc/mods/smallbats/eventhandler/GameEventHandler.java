package me.mc.mods.smallbats.eventhandler;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.api.general.BloodConversionRegistry;
import de.teamlapen.vampirism.entity.player.VampirismPlayerAttributes;
import de.teamlapen.vampirism.items.VampirismItemBloodFoodItem;
import de.teamlapen.vampirism.world.ModDamageSources;
import me.mc.mods.smallbats.caps.SmallBatsPlayerCapabilityProvider;
import me.mc.mods.smallbats.events.VerticalStateChangedEvent;
import me.mc.mods.smallbats.mixininterfaces.IVampirismItemBloodFoodAccessor;
import me.mc.mods.smallbats.vampire.SmallBatsVampireActions;
import me.mc.mods.smallbats.vampire.actions.MistShapeAction;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
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
            // ModSmallBats.INSTANCE.Logger.info("checking if player may sleep");
            e.setResult(VampirismPlayerAttributes.get(player).getVampSpecial().bat ? Event.Result.ALLOW : Event.Result.DEFAULT);
        }
    }

    @SubscribeEvent
    public void onVerticalStateChangedEvent(VerticalStateChangedEvent e) {
        if(e.entity instanceof Player player) {
            if (e.entity.level().isClientSide()) {
                if (e.justTookOffFromCeiling && VampirismPlayerAttributes.get(player).getVampSpecial().bat) {
                    e.entity.level().playSound(player,
                            e.entity.blockPosition(),
                            SoundEvents.BAT_TAKEOFF,
                            SoundSource.PLAYERS,
                            .5f,
                            Mth.lerp(e.entity.level().random.nextFloat(), .9f, 1.1f)
                    );
                }
            }
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
    public void onToolTip(ItemTooltipEvent e) {
        if(e.getEntity() == null)
            return;
        if(VampirismAPI.getVampirePlayer(e.getEntity()).isPresent()) {
            int blood = BloodConversionRegistry.getImpureBloodValue(e.getItemStack().getItem());
            int bloodBarsFilledEaten = 0;
            if ((e.getItemStack().getItem() instanceof VampirismItemBloodFoodItem vampFoodItem)) {
                bloodBarsFilledEaten = ((IVampirismItemBloodFoodAccessor)vampFoodItem).getVampireFood().getNutrition() / 2;
            }
            if (blood>0 && Screen.hasShiftDown()) {
                e.getToolTip().add(1,Component.translatable("tooltips.smallbats.itembloodvalue",blood,(bloodBarsFilledEaten == 0 ? "-" :  String.valueOf(bloodBarsFilledEaten))).withStyle(ChatFormatting.DARK_RED));
            }
        }
    }

    @SubscribeEvent
    public void onEntityEventSize(EntityEvent.Size e) {
        if (e.getEntity() instanceof Player p) {
            p.getCapability(SmallBatsPlayerCapabilityProvider.SMALLBATS_PLAYER_CAP).resolve().ifPresent(cap -> {
                if (cap.getIsMist()) {
                    e.setNewSize(MistShapeAction.MIST_DIMENSIONS);
                    e.setNewEyeHeight(MistShapeAction.MIST_DIMENSIONS.height);
                }
            });
        }
    }

    @SubscribeEvent
    public void onAttachCapability(AttachCapabilitiesEvent<Entity> e) {
        // ModSmallBats.INSTANCE.Logger.info("we attach data: " + e.getObject());
        if(e.getObject() instanceof Player) {
            e.addCapability(SmallBatsPlayerCapabilityProvider.SMALLBATS_PLAYER_CAP_LOC, new SmallBatsPlayerCapabilityProvider());
        }
    }
}
