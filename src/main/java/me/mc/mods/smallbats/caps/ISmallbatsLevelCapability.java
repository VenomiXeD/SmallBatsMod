package me.mc.mods.smallbats.caps;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISmallbatsLevelCapability extends INBTSerializable<CompoundTag> {
    boolean worldHasLordWithLevel(int level);

    void worldNewLordPlayer(Player player, int level);
}
