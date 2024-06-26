package me.mc.mods.smallbats;

import me.mc.mods.smallbats.caps.SmallBatsPlayerCapabilityProvider;
import me.mc.mods.smallbats.datagen.SmallBatsSkillNodeProvider;
import me.mc.mods.smallbats.eventhandler.ClientGameEventHandler;
import me.mc.mods.smallbats.eventhandler.GameEventHandler;
import me.mc.mods.smallbats.network.ModSmallBatsNetworking;
import me.mc.mods.smallbats.vampire.SmallBatsVampireActions;
import me.mc.mods.smallbats.vampire.SmallBatsVampireSkills;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

import java.util.logging.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ModSmallBats.MODID)
public class ModSmallBats
{
    public static final String MODID = "smallbats";
    public static ModSmallBats INSTANCE;
    public final Logger Logger = java.util.logging.Logger.getLogger("Mod-Smallbats");


    public final GameEventHandler GameEventsHandler;
    public final ClientGameEventHandler cGameEventsHandler;
    public ModSmallBatsNetworking Networking;

    public ModSmallBats() {
        INSTANCE = this;
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.register(this);

        GameEventsHandler = new GameEventHandler();
        cGameEventsHandler = new ClientGameEventHandler();
        Networking = new ModSmallBatsNetworking();

        SmallBatsVampireSkills.VAMPIRE_SKILLS.register(bus);
        SmallBatsVampireActions.VAMPIRE_ACTIONS.register(bus);
    }
    @SubscribeEvent
    public void onFMLCommonSetupEvent(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(GameEventsHandler);
        MinecraftForge.EVENT_BUS.register(cGameEventsHandler);

        Networking.register();
    }

    @SubscribeEvent
    public void onRegisterEvent(RegisterEvent event) {
    }

    @SubscribeEvent
    public void onRegisterCapabilitesEvent(RegisterCapabilitiesEvent event) {
        event.register(SmallBatsPlayerCapabilityProvider.class);
    }

    @SubscribeEvent
    public void onGatherDataEvent(GatherDataEvent event){
        event.getGenerator().addProvider(true, new SmallBatsSkillNodeProvider(event.getGenerator().getPackOutput(), ModSmallBats.MODID));
    }
}
