package me.mc.mods.smallbats.caps;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.UUID;

public class SmallBatsLevelCapability implements ISmallbatsLevelCapability {
    private final HashMap<Integer, UUID> levels = new HashMap<>();

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        for (int i = 0; i < 5; i++) {
            UUID uuid = levels.get(i);
            tag.putString("level" + i, levels.get(i) == null ? "" : levels.get(i).toString());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        for (int i = 0; i < 5; i++) {
            levels.put(i, UUID.fromString(nbt.getString("level" + i)));
        }
    }

    @Override
    public boolean worldHasLordWithLevel(int level) {
        return levels.containsKey(level);
    }

    @Override
    public void worldNewLordPlayer(Player player, int level) {
        levels.put(level, player.getUUID());
    }
}
