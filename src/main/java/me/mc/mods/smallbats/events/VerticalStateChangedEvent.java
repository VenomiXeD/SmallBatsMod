package me.mc.mods.smallbats.events;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.Event;

public class VerticalStateChangedEvent extends Event {
    public VerticalStateChangedEvent(boolean justLandedOnGround, boolean justTookOffFromGround, boolean justLandedOnCeiling, boolean justTookOffFromCeiling, Entity entity) {
        this.justLandedOnGround = justLandedOnGround;
        this.justTookOffFromGround = justTookOffFromGround;
        this.justLandedOnCeiling = justLandedOnCeiling;
        this.justTookOffFromCeiling = justTookOffFromCeiling;
        this.entity = entity;
    }

    public boolean justLandedOnGround;
    public boolean justTookOffFromGround;
    public boolean justLandedOnCeiling;
    public boolean justTookOffFromCeiling;
    public Entity entity;
}
