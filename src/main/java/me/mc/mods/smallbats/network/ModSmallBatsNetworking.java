package me.mc.mods.smallbats.network;

import me.mc.mods.smallbats.ModSmallBats;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModSmallBatsNetworking {
    private static final String prot_version = "1";
    public final SimpleChannel modchannel = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ModSmallBats.MODID,"capsync"),
            ()->prot_version,
            prot_version::equals,
            prot_version::equals
    );
    private int pId=0;
    public void register() {
        modchannel.registerMessage(pId++, PacketSynchronizeCapability.class, PacketSynchronizeCapability::encode, PacketSynchronizeCapability::decode, PacketSynchronizeCapability::handle);
    }
}
