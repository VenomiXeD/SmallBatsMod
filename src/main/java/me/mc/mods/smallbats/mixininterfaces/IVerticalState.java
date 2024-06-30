package me.mc.mods.smallbats.mixininterfaces;

public interface IVerticalState {
    public void setIsOnCeiling(boolean b);
    public void setIsOnFloor(boolean b);
    public boolean getIsOnCeiling();
    public boolean getIsOnCeiling(boolean newRayCheck);
    public boolean getIsOnFloor();
}
