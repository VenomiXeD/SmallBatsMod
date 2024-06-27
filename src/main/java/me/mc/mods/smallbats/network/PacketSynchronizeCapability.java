package me.mc.mods.smallbats.network;

import me.mc.mods.smallbats.ModSmallBats;
import me.mc.mods.smallbats.caps.ISynchronizableCapabilityProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class PacketSynchronizeCapability {
    public String getCapClassName() {
        return capClassName;
    }

    public CompoundTag getCapTag() {
        return capTag;
    }

    private String capClassName;
    private int capClassNameLength;
    private int entityIDCapBelongsTo;
    private CompoundTag capTag;
    public PacketSynchronizeCapability() {
    }
    public PacketSynchronizeCapability(CompoundTag tagsForCap, Class<? extends ISynchronizableCapabilityProvider> capabilityProviderClass, Entity entityCapBelongsToSynchronize) {
        capClassName = capabilityProviderClass.getName();
        capClassNameLength = capClassName.length();
        entityIDCapBelongsTo = entityCapBelongsToSynchronize.getId();
        capTag = tagsForCap;
    }
    public static void encode(PacketSynchronizeCapability msg, FriendlyByteBuf buf) {
        // Write the class name so it can be reconstructed later
        buf.writeInt(msg.capClassNameLength);
        buf.writeUtf(msg.capClassName, msg.capClassNameLength);

        // Write entity id
        buf.writeInt(msg.entityIDCapBelongsTo);

        // Write the tag data
        buf.writeNbt(msg.capTag);
    }

    public int getEntityIDCapBelongsTo() {
        return entityIDCapBelongsTo;
    }

    public static PacketSynchronizeCapability decode(FriendlyByteBuf buf) {
        PacketSynchronizeCapability decodedPacket = new PacketSynchronizeCapability();

        // first read the length of the class name
        decodedPacket.capClassNameLength = buf.readInt();
        // obtain the class name string
        decodedPacket.capClassName = buf.readUtf(decodedPacket.capClassNameLength);

        // obtain the id of entity we want to update the cap for
        decodedPacket.entityIDCapBelongsTo = buf.readInt();

        // obtain the tag
        decodedPacket.capTag = buf.readAnySizeNbt();

        return decodedPacket;
    }

    public static void dispatchCapabilitySynchronization(CompoundTag capabilitySerializedIntoNBT, Class<ISynchronizableCapabilityProvider> capabilityProviderClassvider, Entity entityToSynchronizeToClients) {
        PacketSynchronizeCapability packet = new PacketSynchronizeCapability(capabilitySerializedIntoNBT, capabilityProviderClassvider, entityToSynchronizeToClients);
        ModSmallBats.INSTANCE.Networking.modchannel.send(PacketDistributor.ALL.noArg(), packet);
    }

    public static void handle(PacketSynchronizeCapability msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(()->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->ClientBoundSynchronizeCapability.handle(msg, ctx))
        );
        ctx.get().setPacketHandled(true);
    }
}
