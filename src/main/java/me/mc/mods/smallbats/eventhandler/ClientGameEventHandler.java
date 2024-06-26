package me.mc.mods.smallbats.eventhandler;

import me.mc.mods.smallbats.vampire.actions.MistShapeAction;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class ClientGameEventHandler {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRenderLivingEvent(RenderLivingEvent.Pre<Player, PlayerModel<Player>> e) {
        if(e.getEntity() instanceof Player p) {
            if (MistShapeAction.isMistShape(p)) {
                e.setCanceled(true);
            }
        }
    }
}
