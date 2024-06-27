    package me.mc.mods.smallbats.network;

    import me.mc.mods.smallbats.caps.ISmallBatsBaseCapability;
    import me.mc.mods.smallbats.caps.ISynchronizableCapabilityProvider;
    import net.minecraft.client.Minecraft;
    import net.minecraft.world.entity.Entity;
    import net.minecraftforge.network.NetworkEvent;

    import java.util.function.Supplier;

    public class ClientBoundSynchronizeCapability {
        public static void handle(PacketSynchronizeCapability msg, Supplier<NetworkEvent.Context> ctx) {
            // we get the capability class first
            try {
                Class<? extends ISynchronizableCapabilityProvider> isynchronizableCapProviderClass = (Class<? extends ISynchronizableCapabilityProvider>) Class.forName(msg.getCapClassName());
                ISynchronizableCapabilityProvider capInstance = (ISynchronizableCapabilityProvider) isynchronizableCapProviderClass.getConstructor().newInstance();
                Entity e = Minecraft.getInstance().level.getEntity(msg.getEntityIDCapBelongsTo());
                if (e != null) {
                    e.getCapability(capInstance.cap()).ifPresent(cap->((ISmallBatsBaseCapability)cap).deserializeNBT(msg.getCapTag()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
